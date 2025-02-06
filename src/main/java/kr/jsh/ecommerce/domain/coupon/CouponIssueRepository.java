package kr.jsh.ecommerce.domain.coupon;

import kr.jsh.ecommerce.domain.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CouponIssueRepository {
    CouponIssue save(CouponIssue couponIssue);
    List<CouponIssue> findByCustomerId(Long customerId);
    Optional<CouponIssue> findById( Long couponIssueId);
    Optional<CouponIssue> findIssuedCoupon(Long couponId, Long customerId);
    boolean existsByCoupon_CouponIdAndCustomer_CustomerId(Long couponId, Long customerId);
}
