package kr.jsh.ecommerce.infrastructure.coupon;

import kr.jsh.ecommerce.domain.coupon.Coupon;
import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import kr.jsh.ecommerce.domain.coupon.CouponIssueRepository;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.infrastructure.customer.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponIssueRepositoryImpl implements CouponIssueRepository {

    private final CouponIssueJpaRepository couponIssueJpaRepository;
    @Override
    public CouponIssue save(CouponIssue issuedCoupon) {
        return couponIssueJpaRepository.save(issuedCoupon);
    }

    @Override
    public List<CouponIssue> findByCustomerId(Long customerId) {
        return couponIssueJpaRepository.findByCustomerId(customerId);
    }

    @Override
    public Optional<CouponIssue> findIssuedCoupon(Long couponId, Long customerId) {
        return couponIssueJpaRepository.findIssuedCoupon(couponId,customerId);
    }

    @Override
    public Optional<CouponIssue> findById(Long couponIssueId) {
        return couponIssueJpaRepository.findById(couponIssueId);
    }

}
