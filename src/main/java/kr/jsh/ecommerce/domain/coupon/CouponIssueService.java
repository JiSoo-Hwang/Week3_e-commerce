package kr.jsh.ecommerce.domain.coupon;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.customer.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponIssueRepository couponIssueRepository;

    @Transactional
    public CouponIssue issueCoupon(Coupon coupon, Customer customer) {
        // 1. 중복 발급 여부 확인
        if (couponIssueRepository.existsByCouponAndCustomer(coupon, customer)) {
            throw new BaseCustomException(BaseErrorCode.ALREADY_ISSUED_COUPON, new String[]{coupon.getCouponName()});
        }

        // 2. 쿠폰 발급
        CouponIssue couponIssue = CouponIssue.create(coupon, customer);

        // 3. 저장
        return couponIssueRepository.save(couponIssue);
    }
}
