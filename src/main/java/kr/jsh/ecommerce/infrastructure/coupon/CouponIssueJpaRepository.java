package kr.jsh.ecommerce.infrastructure.coupon;

import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue,Long> {
    // 고객 ID로 발급된 쿠폰 목록 조회
    @Query("SELECT ci FROM CouponIssue ci WHERE ci.customer.customerId = :customerId")
    Page<CouponIssue> findByCustomerId(@Param("customerId") Long customerId);

}
