package kr.jsh.ecommerce.domain.coupon;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.interfaces.dto.coupon.CouponIssueRequest;
import kr.jsh.ecommerce.interfaces.dto.coupon.CouponIssueResponse;
import kr.jsh.ecommerce.interfaces.dto.coupon.CouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    public Coupon findCoupon(Coupon coupon) {
    return couponRepository.findById(coupon.getCouponId())
            .orElseThrow(()->new BaseCustomException(BaseErrorCode.NOT_FOUND,new String[]{coupon.getCouponName()}));
    }
    public Coupon issue(CouponIssueRequest couponIssueRequest) {
        Coupon couponToBeIssued = couponIssueRequest.coupon();
        couponToBeIssued.issueCoupon();
        return couponToBeIssued;
    }
    public CouponResponse saveIssuedCoupon(Coupon issuedCoupon){
        couponRepository.save(issuedCoupon);
        return CouponIssueResponse.fromCoupon(issuedCoupon);
    }
}
