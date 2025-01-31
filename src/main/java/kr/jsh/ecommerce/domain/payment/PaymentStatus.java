package kr.jsh.ecommerce.domain.payment;

public enum PaymentStatus {
    PENDING,    //  결제 대기 중 (기본값)
    SUCCESS,    //  결제 완료
    FAILED,     //  결제 실패
    REFUNDED    //  결제 취소/환불됨
}
