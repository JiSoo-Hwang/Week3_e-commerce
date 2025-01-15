package kr.jsh.ecommerce.domain.coupon;

import kr.jsh.ecommerce.interfaces.dto.coupon.CouponIssueRequest;
import kr.jsh.ecommerce.interfaces.dto.coupon.CouponIssueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponIssueService {
    private final CouponIssueRepository couponIssueRepository;



    public CouponIssueResponse saveIssuedCoupon(Coupon issuedCoupon) {
        CouponIssue couponIssue = couponIssueRepository.save(issuedCoupon);
        return CouponIssueResponse.fromIssuedCoupon(issuedCoupon);
    }
}
