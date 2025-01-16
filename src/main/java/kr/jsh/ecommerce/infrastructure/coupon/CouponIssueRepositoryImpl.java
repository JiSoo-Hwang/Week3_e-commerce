package kr.jsh.ecommerce.infrastructure.coupon;

import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import kr.jsh.ecommerce.domain.coupon.CouponIssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponIssueRepositoryImpl implements CouponIssueRepository {

    private final CouponIssueJpaRepository couponIssueJpaRepository;
    @Override
    public CouponIssue save(CouponIssue issuedCoupon) {
        return couponIssueJpaRepository.save(issuedCoupon);
    }

    @Override
    public List<CouponIssue> findByCustomerId(Long customerId, Pageable pageable) {
        return couponIssueJpaRepository.findByCustomerId(customerId);
    }
}
