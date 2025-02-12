package kr.jsh.ecommerce.infrastructure.coupon;

import jakarta.persistence.LockModeType;
import kr.jsh.ecommerce.domain.coupon.Coupon;
import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import kr.jsh.ecommerce.domain.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue, Long> {
    // 고객 ID로 발급된 쿠폰 목록 조회
    @Query("SELECT ci FROM CouponIssue ci WHERE ci.customer.customerId = :customerId")
    List<CouponIssue> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT ci FROM CouponIssue ci WHERE ci.coupon.id = :couponId AND ci.customer.id = :customerId")
    Optional<CouponIssue> findIssuedCoupon(@Param("couponId") Long couponId, @Param("customerId") Long customerId);

    boolean existsByCoupon_CouponIdAndCustomer_CustomerId(Long couponId, Long customerId);
}
