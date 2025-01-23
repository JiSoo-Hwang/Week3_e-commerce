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

@Service
@RequiredArgsConstructor
public class IssueCouponUseCase {

    private final CouponService couponService;
    private final CouponIssueService couponIssueService;
    private final CustomerService customerService;

    @Transactional
    public CouponIssueResponse issueCoupon(CouponIssueRequest request) {
        // 1. 쿠폰 조회 및 락 설정
        Coupon coupon = couponService.findCouponWithLock(request.couponId());

        // 2. 고객 조회
        Customer customer = customerService.findCustomerById(request.customerId());

        // 3. 쿠폰 발급 처리 (중복 여부 확인 포함)
        CouponIssue couponIssue = couponIssueService.issueCoupon(coupon, customer);

        // 5. 응답 반환
        return CouponIssueResponse.fromIssuedCoupon(couponIssue);
    }
}