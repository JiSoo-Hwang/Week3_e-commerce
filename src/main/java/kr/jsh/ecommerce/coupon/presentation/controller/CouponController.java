package kr.jsh.ecommerce.coupon.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customers/{customerId}/coupons")
public class CouponController {

    @PostMapping
    public ResponseEntity<Map<String, Object>> issueCoupon(@PathVariable Long customerId) {
        Map<String, Object> coupon = Map.of(
                "couponId", 1,
                "customerId", customerId,
                "status", "ISSUED",
                "issuedAt", "2025-01-01T10:00:00",
                "expiredAt", "2025-01-31T23:59:59"
        );
        return ResponseEntity.ok(coupon);
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getCouponList(@PathVariable Long customerId) {
        List<Map<String, Object>> coupons = List.of(
                Map.of("couponId", 1, "name", "FIRST_COME", "discountAmount", 1000, "status", "ISSUED", "issuedAt", "2025-01-01T10:00:00", "expiredAt", "2025-01-31T23:59:59"),
                Map.of("couponId", 2, "name", "WELCOME", "discountAmount", 2000, "status", "ISSUED", "issuedAt", "2025-01-01T10:00:00", "expiredAt", "2025-01-31T23:59:59")
        );
        return ResponseEntity.ok(coupons);
    }

}

