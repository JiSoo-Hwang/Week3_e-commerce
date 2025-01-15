package kr.jsh.ecommerce.interfaces.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.jsh.ecommerce.application.IssueCouponUseCase;
import kr.jsh.ecommerce.base.dto.response.BaseResponseContent;
import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import kr.jsh.ecommerce.domain.coupon.CouponIssueService;
import kr.jsh.ecommerce.interfaces.dto.coupon.CouponIssueRequest;
import kr.jsh.ecommerce.interfaces.dto.coupon.CouponIssueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Coupon API",description = "쿠폰 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    private final IssueCouponUseCase issueCouponUseCase;

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
}
