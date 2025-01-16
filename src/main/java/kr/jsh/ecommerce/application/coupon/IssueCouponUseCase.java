package kr.jsh.ecommerce.application.coupon;

import kr.jsh.ecommerce.domain.coupon.Coupon;
import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import kr.jsh.ecommerce.domain.coupon.CouponIssueService;
import kr.jsh.ecommerce.domain.coupon.CouponService;
import kr.jsh.ecommerce.interfaces.dto.coupon.CouponIssueRequest;
import kr.jsh.ecommerce.interfaces.dto.coupon.CouponIssueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IssueCouponUseCase {
    private final CouponService couponService;
    private final CouponIssueService couponIssueService;
    public CouponIssueResponse issueCoupon(CouponIssueRequest couponIssueRequest){
        Coupon coupon = couponService.findCoupon(couponIssueRequest.coupon());
        CouponIssue issuedCoupon =  couponService.issueCoupon(couponIssueRequest);
        return couponService.saveIssuedCoupon(issuedCoupon);
    }

}
