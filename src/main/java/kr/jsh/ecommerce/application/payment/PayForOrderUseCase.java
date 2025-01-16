package kr.jsh.ecommerce.application.payment;

import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import kr.jsh.ecommerce.domain.coupon.CouponIssueService;
import kr.jsh.ecommerce.domain.order.Order;
import kr.jsh.ecommerce.domain.order.OrderService;
import kr.jsh.ecommerce.domain.payment.Payment;
import kr.jsh.ecommerce.domain.payment.PaymentService;
import kr.jsh.ecommerce.domain.wallet.Wallet;
import kr.jsh.ecommerce.domain.wallet.WalletService;
import kr.jsh.ecommerce.interfaces.dto.payment.PaymentRequest;
import kr.jsh.ecommerce.interfaces.dto.payment.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PayForOrderUseCase {
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final CouponIssueService couponIssueService;

    public PaymentResponse payForOrder(PaymentRequest paymentRequest){
        Order order = orderService.findOrderById(paymentRequest.orderId());
        CouponIssue issuedCoupon = null;
        // 쿠폰 조회 (null 허용)
        if (paymentRequest.couponIssueId() != null) {
            issuedCoupon = couponIssueService.findIssuedCouponById(paymentRequest.couponIssueId());
        }
        // 결제 생성
        Payment payment = paymentService.createPayment(order, issuedCoupon, paymentRequest.status());
        return PaymentResponse.fromEntity(payment);
    }
}
