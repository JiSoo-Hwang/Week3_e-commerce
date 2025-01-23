package kr.jsh.ecommerce.interfaces.api.coupon.dto;

import kr.jsh.ecommerce.domain.coupon.CouponIssue;

import java.time.LocalDateTime;

public record CouponIssueResponse(
        Long couponIssueId,      // 발급된 쿠폰의 ID
        Long couponId,           // 쿠폰 ID
        String couponName,       // 쿠폰 이름
        int discountAmount,      // 할인 금액
        Long customerId,
        String customerName,// 고객 ID
        LocalDateTime issuedAt,  // 발급일
        LocalDateTime expiredAt, // 만료일
        String status            // 상태 (ISSUED, USED, EXPIRED)
) {
    // CouponIssue 엔터티에서 DTO 생성
    public static CouponIssueResponse fromIssuedCoupon(CouponIssue couponIssue) {
        return new CouponIssueResponse(
                couponIssue.getCouponIssueId(),
                couponIssue.getCoupon().getCouponId(),
                couponIssue.getCoupon().getCouponName(),
                couponIssue.getCoupon().getDiscountAmount(),
                couponIssue.getCustomer().getCustomerId(),
                couponIssue.getCustomer().getCustomerName(),
                couponIssue.getIssuedAt(),
                couponIssue.getExpiredAt(),
                couponIssue.getStatus().toString() // Enum 값을 문자열로 변환
        );
    }
}
