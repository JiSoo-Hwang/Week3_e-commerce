package kr.jsh.ecommerce.interfaces.api.payment.dto;

public record PaymentRequest(
        Long orderId,
        Long couponIssueId,
        String status // 결제 상태 (SUCCESS/FAILED)
) {
}
