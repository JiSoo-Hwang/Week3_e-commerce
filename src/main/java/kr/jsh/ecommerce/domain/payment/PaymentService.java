package kr.jsh.ecommerce.domain.payment;

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
    public Payment createPayment(Order order, CouponIssue issuedCoupon, String paymentStatus) {
        Wallet wallet = order.getCustomer().getWallet();
        // 쿠폰 할인 적용
        int discount = 0;
        if (issuedCoupon != null) {
            issuedCoupon.markAsUsed();
            discount = issuedCoupon.getCoupon().getDiscountAmount();
        }
        // 최종 결제 금액 계산
        int finalAmount = Math.max(0, order.getTotalAmount() - discount);
        wallet.spendCash(finalAmount);

        Payment payment = new Payment(null, order, order.getTotalAmount(), paymentStatus, LocalDateTime.now());
        paymentRepository.save(payment);
        order.setPayment(payment);
        orderRepository.save(order);
        return payment;
    }
}
