package kr.jsh.ecommerce.domain.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponStatus {
    ISSUED("발급됨"), // 발급된 상태
    USED("사용됨"),   // 이미 사용된 상태
    EXPIRED("만료됨"); // 만료된 상태

    private final String description;
}
