package kr.jsh.ecommerce.interfaces.api.coupon.dto;

import kr.jsh.ecommerce.domain.coupon.Coupon;
import kr.jsh.ecommerce.domain.customer.Customer;

public record CouponIssueRequest(
        Long couponId,
        Long customerId
) {
}
