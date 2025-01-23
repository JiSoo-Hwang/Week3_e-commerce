package kr.jsh.ecommerce.domain.coupon;

import kr.jsh.ecommerce.domain.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CouponIssueRepository {
    CouponIssue save(CouponIssue issuedCoupon);
    boolean existsByCouponAndCustomer(Coupon couponTobeIssued, Customer customer);
}
