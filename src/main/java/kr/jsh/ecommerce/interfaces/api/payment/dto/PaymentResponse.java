package kr.jsh.ecommerce.interfaces.api.payment.dto;

import kr.jsh.ecommerce.domain.payment.Payment;
import kr.jsh.ecommerce.domain.payment.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long paymentId,
        Long orderId,
        int amount,
        PaymentStatus paymentStatus,
        LocalDateTime paidAt,
        int remainingBalance) {
    public static PaymentResponse fromEntity(Payment payment) {
        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getOrder().getOrderId(),
                payment.getAmount(),
                payment.getPaymentStatus(),
                payment.getPaidAt(),
                payment.getOrder().getCustomer().getWallet().getBalance()
        );
    }
}
