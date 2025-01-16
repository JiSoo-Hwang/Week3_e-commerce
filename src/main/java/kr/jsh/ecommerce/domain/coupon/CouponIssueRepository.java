package kr.jsh.ecommerce.domain.coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CouponIssueRepository {
    CouponIssue save(CouponIssue issuedCoupon);
    Page<CouponIssue> findByCustomerId(Long customerId, Pageable pageable);
    Optional<CouponIssue> findById(Long couponIssueId);
}
