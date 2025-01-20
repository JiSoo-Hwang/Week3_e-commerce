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
    private final CouponIssueRepository couponIssueRepository;

    public Coupon findCoupon(Coupon coupon) {
        return couponRepository.findById(coupon.getCouponId())
                .orElseThrow(() -> new BaseCustomException(BaseErrorCode.NOT_FOUND, new String[]{coupon.getCouponName()}));
    }

    public CouponIssue issueCoupon(CouponIssueRequest couponIssueRequest) {
        Coupon couponToBeIssued = couponIssueRequest.coupon();
        couponToBeIssued.issueCoupon();
        couponRepository.save(couponToBeIssued);
        return CouponIssue.create(couponToBeIssued,couponIssueRequest.customer());
    }

    public CouponIssueResponse saveIssuedCoupon(CouponIssue issuedCoupon) {
        couponIssueRepository.save(issuedCoupon);
        return CouponIssueResponse.fromIssuedCoupon(issuedCoupon);
    }
}
