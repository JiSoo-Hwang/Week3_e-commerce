package kr.jsh.ecommerce.order.domain;

public enum OrderStatus {
    PENDING_PAYMENT, // 결제 대기
    COMPLETED,       // 결제 완료
    CANCELLED        // 주문 취소
}
