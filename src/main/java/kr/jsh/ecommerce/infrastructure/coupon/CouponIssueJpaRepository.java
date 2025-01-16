package kr.jsh.ecommerce.infrastructure.coupon;

import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue,Long> {
    // 고객 ID로 발급된 쿠폰 목록 조회
    @Query("SELECT ci FROM CouponIssue ci WHERE ci.customer.customerId = :customerId")
    List<CouponIssue> findByCustomerId(@Param("customerId") Long customerId);

    // 쿠폰 발급 ID로 상세 정보 조회
    @Query("SELECT ci FROM CouponIssue ci WHERE ci.couponIssueId = :couponIssueId")
    CouponIssue findByIdWithDetails(@Param("couponIssueId") Long couponIssueId);
}
