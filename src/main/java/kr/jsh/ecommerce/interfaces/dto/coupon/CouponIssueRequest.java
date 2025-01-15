package kr.jsh.ecommerce.interfaces.dto.coupon;

import kr.jsh.ecommerce.domain.coupon.Coupon;

public record CouponIssueRequest(
        Coupon coupon,
        Long customerId
) {
}
