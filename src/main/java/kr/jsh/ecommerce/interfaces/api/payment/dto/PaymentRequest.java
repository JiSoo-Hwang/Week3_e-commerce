package kr.jsh.ecommerce.interfaces.api.payment.dto;

import kr.jsh.ecommerce.domain.payment.PaymentStatus;

public record PaymentRequest(
        Long orderId,
        Long couponIssueId,
        PaymentStatus paymentStatus// 결제 상태
) {
}
