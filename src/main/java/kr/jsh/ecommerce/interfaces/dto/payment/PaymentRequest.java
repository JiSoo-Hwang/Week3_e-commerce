package kr.jsh.ecommerce.interfaces.dto.payment;

public record PaymentRequest(
        Long orderId,
        Long couponIssueId,
        String status // 결제 상태 (SUCCESS/FAILED)
) {
}
