package kr.jsh.ecommerce.domain.coupon;

import java.util.Optional;

public interface CouponRepository {
    Optional<Coupon> findById(Long couponId);
    Coupon save(Coupon issuedCoupon);
    Optional<Coupon> findByIdForUpdate(Long couponId);
}
