package kr.jsh.ecommerce.domain.payment;

import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import kr.jsh.ecommerce.domain.order.Order;
import kr.jsh.ecommerce.domain.order.OrderRepository;
import kr.jsh.ecommerce.domain.payment.Payment;
import kr.jsh.ecommerce.domain.payment.PaymentRepository;
import kr.jsh.ecommerce.domain.wallet.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Payment createPayment(Order order, CouponIssue issuedCoupon) {
        Wallet wallet = order.getCustomer().getWallet();
        // 쿠폰 할인 적용
        int discount = 0;
        if (issuedCoupon != null) {
            issuedCoupon.markAsUsed();  // ✅ 쿠폰 사용 처리
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
            // 예외 발생 시 결제 실패 처리
            payment.failPayment();
        }

        //결제 정보 저장
        paymentRepository.save(payment);
        order.setPayment(payment);
        orderRepository.save(order);

        return payment;
    }
}
