package kr.jsh.ecommerce.domain.payment;

import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import kr.jsh.ecommerce.domain.order.Order;
import kr.jsh.ecommerce.domain.order.OrderRepository;
import kr.jsh.ecommerce.domain.payment.Payment;
import kr.jsh.ecommerce.domain.payment.PaymentRepository;
import kr.jsh.ecommerce.domain.wallet.Wallet;
import kr.jsh.ecommerce.event.OrderPaidEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String,OrderPaidEvent> kafkaTemplate;

    @Transactional
    public Payment createPayment(Order order, CouponIssue issuedCoupon) {
        Payment payment = decreaseBalance(order, issuedCoupon);
        Order updatedOrder = updateOrderStatus(order, payment);
        paymentRepository.save(payment);
        orderRepository.save(updatedOrder);

        //주문 결제 완료 이벤트 발행
        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            OrderPaidEvent event = new OrderPaidEvent(order);
            sendOrderPaidEvent(event);
        }
        return payment;
    }

    public Payment decreaseBalance(Order order, CouponIssue issuedCoupon) {
        Wallet wallet = order.getCustomer().getWallet();
        // 쿠폰 할인 적용
        int discount = 0;
        if (issuedCoupon != null) {
            issuedCoupon.markAsUsed();  // 쿠폰 사용 처리
            discount = issuedCoupon.getCoupon().getDiscountAmount();
        }
        // 최종 결제 금액 계산
        int finalAmount = Math.max(0, order.getTotalAmount() - discount);

        //결제 객체 생성
        Payment payment = Payment.create(order, finalAmount);

        //잔액 차감
        try {
            // 결제 시도
            wallet.spendCash(finalAmount);
            payment.completePayment();
        } catch (BaseCustomException e) {
            // 결제 실패시 예외 던지기
            throw e;
        }
        return payment;
    }

    public Order updateOrderStatus(Order order, Payment payment) {
        order.setPayment(payment);
        return order;
    }

    private void sendOrderPaidEvent(OrderPaidEvent event) {
        try {
            kafkaTemplate.send("order-paid-topic", event).get(); // 메시지 전송이 완료될 때까지 대기
            log.info("Kafka 메시지 전송 성공: {}", event);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Kafka 메시지 전송 실패", e);
            throw new RuntimeException("Kafka 메시지 전송 실패", e);
        }
    }
}
