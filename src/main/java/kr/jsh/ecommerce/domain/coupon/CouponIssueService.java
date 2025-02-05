package kr.jsh.ecommerce.domain.coupon;

import io.micrometer.observation.ObservationFilter;
import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.customer.CustomerRepository;
import kr.jsh.ecommerce.interfaces.api.coupon.dto.CouponIssueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CouponIssueService {

    private final CouponRepository couponRepository;
    private final CouponIssueRepository couponIssueRepository;
    private final CustomerRepository customerRepository; // Customer 조회용

    @Transactional(propagation = Propagation.REQUIRED)
    public CouponIssue issueCoupon(Long couponId, Long customerId) {
        // 1. 쿠폰 조회
        Coupon coupon = couponRepository.findByIdForUpdate(couponId)
                .orElseThrow(() -> new BaseCustomException(BaseErrorCode.NOT_FOUND, new String[]{"쿠폰이 존재하지 않습니다."}));

        // 2. 고객 조회
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BaseCustomException(BaseErrorCode.NOT_FOUND, new String[]{"고객이 존재하지 않습니다."}));

        // 3. 중복 발급 확인
        Optional<CouponIssue> issuedCoupon = couponIssueRepository.findIssuedCoupon(couponId,customerId);
        if(issuedCoupon.isPresent()){
            throw new BaseCustomException(BaseErrorCode.ALREADY_ISSUED_COUPON,new String[]{coupon.getCouponName()});
        }

        // 4. 쿠폰 발급 처리
        coupon.issueCoupon(); // 발급 가능 여부 확인 및 발급 수량 증가
        CouponIssue couponIssue = CouponIssue.create(coupon, customer);
        return couponIssueRepository.save(couponIssue);
    }

    public List<CouponIssueResponse> getCouponsByCustomerId(Long customerId) {
        List<CouponIssue> issuedCoupons = couponIssueRepository.findByCustomerId(customerId);
        return issuedCoupons.stream()
                .map(CouponIssueResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public CouponIssue findIssuedCouponById(Long couponIssueId) {
    return couponIssueRepository.findById(couponIssueId).orElseThrow(() -> new BaseCustomException(BaseErrorCode.NOT_FOUND, new String[]{"쿠폰이 존재하지 않습니다."}));
    }
}