package kr.jsh.ecommerce.application;

import kr.jsh.ecommerce.domain.coupon.Coupon;
import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import kr.jsh.ecommerce.domain.coupon.CouponIssueService;
import kr.jsh.ecommerce.domain.coupon.CouponService;
import kr.jsh.ecommerce.interfaces.dto.coupon.CouponIssueRequest;
import kr.jsh.ecommerce.interfaces.dto.coupon.CouponIssueResponse;
import kr.jsh.ecommerce.interfaces.dto.coupon.CouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IssueCouponUseCase {
    private final CouponService couponService;
    private final CouponIssueService couponIssueService;
    public CouponIssueResponse issueCoupon(CouponIssueRequest couponIssueRequest){
        Coupon coupon = couponService.findCoupon(couponIssueRequest.coupon());
        Coupon issuedCoupon =  couponService.issue(couponIssueRequest);
        CouponResponse couponResponse = couponService.saveIssuedCoupon(issuedCoupon);
        return couponIssueService.saveIssuedCoupon(issuedCoupon);
    }

}
