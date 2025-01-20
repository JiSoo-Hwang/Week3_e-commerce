package kr.jsh.ecommerce.interfaces.dto.coupon;

import kr.jsh.ecommerce.domain.coupon.Coupon;
import kr.jsh.ecommerce.domain.customer.Customer;

public record CouponIssueRequest(
        Coupon coupon,
        Customer customer
) {
}
