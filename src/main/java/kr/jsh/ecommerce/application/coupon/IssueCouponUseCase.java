package kr.jsh.ecommerce.application.coupon;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.coupon.Coupon;
import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import kr.jsh.ecommerce.domain.coupon.CouponIssueService;
import kr.jsh.ecommerce.domain.coupon.CouponService;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.customer.CustomerService;
import kr.jsh.ecommerce.interfaces.api.coupon.dto.CouponIssueRequest;
import kr.jsh.ecommerce.interfaces.api.coupon.dto.CouponIssueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class IssueCouponUseCase {

    private final CouponIssueService couponIssueService;

    public CouponIssueResponse issueCoupon(CouponIssueRequest request) {
        CouponIssue issuedCoupon = couponIssueService.issueCoupon(request.couponId(), request.customerId());
        return CouponIssueResponse.fromEntity(issuedCoupon);
    }
}