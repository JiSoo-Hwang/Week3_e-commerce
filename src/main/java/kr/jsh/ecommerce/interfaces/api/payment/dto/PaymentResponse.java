package kr.jsh.ecommerce.interfaces.api.payment.dto;

import kr.jsh.ecommerce.domain.payment.Payment;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long paymentId,
        Long orderId,
        int amount,
        String paymentStatus,
        LocalDateTime paidAt,
        int remainingBalance) {
    public static PaymentResponse fromEntity(Payment payment) {
        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getOrder().getOrderId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaidAt(),
                payment.getOrder().getCustomer().getWallet().getBalance()
        );
    }
}
