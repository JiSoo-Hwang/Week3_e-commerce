package kr.jsh.ecommerce.interfaces.api.coupon.dto;

import kr.jsh.ecommerce.domain.coupon.Coupon;

public record CouponResponse(
        Long couponId,
        String couponName,
        int discountAmount,
        int maxQuantity,
        int issuedCount
) {
    public static CouponResponse fromEntity(Coupon coupon) {
        return new CouponResponse(
                coupon.getCouponId(),
                coupon.getCouponName(),
                coupon.getDiscountAmount(),
                coupon.getMaxQuantity(),
                coupon.getIssuedCount()
        );
    }
}