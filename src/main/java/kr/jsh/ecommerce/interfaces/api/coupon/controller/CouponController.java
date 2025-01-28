package kr.jsh.ecommerce.interfaces.api.coupon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.jsh.ecommerce.application.coupon.GetCouponIssueUseCase;
import kr.jsh.ecommerce.application.coupon.IssueCouponUseCase;
import kr.jsh.ecommerce.base.dto.response.BaseResponseContent;
import kr.jsh.ecommerce.base.dto.response.BaseResponsePage;
import kr.jsh.ecommerce.interfaces.api.coupon.dto.CouponIssueRequest;
import kr.jsh.ecommerce.interfaces.api.coupon.dto.CouponIssueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Coupon API", description = "쿠폰 발급 및 조회 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final IssueCouponUseCase issueCouponUseCase;
    private final GetCouponIssueUseCase getCouponIssueUseCase;

    @Operation(summary = "쿠폰 발급", description = "특정 쿠폰을 고객에게 발급합니다.")
    @Parameter(name = "couponIssueRequest", description = "발급할 쿠폰과 고객 정보를 포함한 요청 객체", required = true)
    @PostMapping("/issue")
    public ResponseEntity<CouponIssueResponse> issueCoupon(@RequestBody CouponIssueRequest request) {
        CouponIssueResponse response = issueCouponUseCase.issueCoupon(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "쿠폰 조회", description = "특정 고객이 발급받은 쿠폰 목록을 조회합니다.")
    @Parameter(name = "customerId", description = "쿠폰을 조회할 고객의 ID", required = true)
    @GetMapping("/issues/{customerId}")
    public ResponseEntity<List<CouponIssueResponse>> getCouponsByCustomerId(@PathVariable Long customerId) {
        List<CouponIssueResponse> coupons = getCouponIssueUseCase.getCouponsByCustomerId(customerId);
        return ResponseEntity.ok(coupons);
    }
}
