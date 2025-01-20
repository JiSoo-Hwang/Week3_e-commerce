package kr.jsh.ecommerce.domain.coupon;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponIssueService {
    private final CouponIssueRepository couponIssueRepository;

    // 고객 ID로 발급된 쿠폰 목록 조회
    @Transactional(readOnly = true)
    public Page<CouponIssue> findIssuedCouponsByCustomerId(Long customerId, Pageable pageable) {
        return couponIssueRepository.findByCustomerId(customerId, pageable);
    }

    public CouponIssue findIssuedCouponById(Long couponIssueId) {
        return couponIssueRepository.findById(couponIssueId).orElseThrow(()->new BaseCustomException(BaseErrorCode.NOT_FOUND));
    }
}
