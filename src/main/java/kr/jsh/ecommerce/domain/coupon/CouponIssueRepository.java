package kr.jsh.ecommerce.domain.coupon;

import java.util.Optional;

public interface CouponIssueRepository {
    CouponIssue save(CouponIssue issuedCoupon);
    Optional<CouponIssue> findById(Long customerId);
}
