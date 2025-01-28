package kr.jsh.ecommerce.interfaces.api.coupon.dto;

import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import kr.jsh.ecommerce.domain.coupon.CouponStatus;

import java.time.LocalDateTime;

public record CouponIssueResponse(
        Long couponIssueId,
        Long couponId,
        String couponName,
        LocalDateTime issuedAt,
        LocalDateTime usedAt,
        LocalDateTime expiredAt,
        CouponStatus status
) {
    public static CouponIssueResponse fromEntity(CouponIssue couponIssue) {
        return new CouponIssueResponse(
                couponIssue.getCouponIssueId(),
                couponIssue.getCoupon().getCouponId(),
                couponIssue.getCoupon().getCouponName(),
                couponIssue.getIssuedAt(),
                couponIssue.getUsedAt(),
                couponIssue.getExpiredAt(),
                couponIssue.getStatus()
        );
    }
}
