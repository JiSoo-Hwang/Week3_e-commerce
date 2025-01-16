package kr.jsh.ecommerce.domain.coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CouponIssueRepository {
    CouponIssue save(CouponIssue issuedCoupon);
    Page<CouponIssue> findByCustomerId(Long customerId, Pageable pageable);
}
