package kr.jsh.ecommerce.application.coupon;

import kr.jsh.ecommerce.domain.coupon.Coupon;
import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import kr.jsh.ecommerce.domain.coupon.CouponIssueService;
import kr.jsh.ecommerce.interfaces.api.coupon.dto.CouponIssueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GetCouponIssueUseCase {

    private final CouponIssueService couponIssueService;

    public List<CouponIssueResponse> getCouponsByCustomerId(Long customerId) {
        return couponIssueService.getCouponsByCustomerId(customerId);
    }
}
