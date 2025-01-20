package kr.jsh.ecommerce.interfaces.api.coupon.controller;

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

@Tag(name = "Coupon API",description = "쿠폰 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    private final IssueCouponUseCase issueCouponUseCase;
    private final GetCouponIssueUseCase getCouponIssueUseCase;
    @PostMapping("/issue")
    public ResponseEntity<BaseResponseContent>issueCoupon(
            @RequestBody CouponIssueRequest couponIssueRequest
            ){
        CouponIssueResponse response = issueCouponUseCase.issueCoupon(couponIssueRequest);
        BaseResponseContent responseContent = new BaseResponseContent(response);
        responseContent.setMessage("쿠폰 발급 성공! 즐거운 쇼핑되세요 :)");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseContent);
    }

    @GetMapping("/issues")
    public ResponseEntity<BaseResponsePage> getIssuedCoupons(
            @PageableDefault(page = 0, size = 9) Pageable pageable,
            @RequestParam Long customerId) {
        Page<CouponIssueResponse> responses = getCouponIssueUseCase.findIssuedCouponsByCustomerId(customerId,pageable);

        return ResponseEntity.ok(new BaseResponsePage(responses));
    }
}
