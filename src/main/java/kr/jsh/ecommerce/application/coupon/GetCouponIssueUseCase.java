package kr.jsh.ecommerce.application.coupon;

import kr.jsh.ecommerce.domain.coupon.CouponIssueService;
import kr.jsh.ecommerce.interfaces.dto.coupon.CouponIssueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetCouponIssueUseCase {

    private final CouponIssueService couponIssueService;

    public Page<CouponIssueResponse> findIssuedCouponsByCustomerId(Long customerId, Pageable pageable) {
        return couponIssueService.findIssuedCouponsByCustomerId(customerId, pageable).map(CouponIssueResponse::fromIssuedCoupon);
    }
}
