package kr.jsh.ecommerce.infrastructure.coupon;

import kr.jsh.ecommerce.domain.coupon.Coupon;
import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import kr.jsh.ecommerce.domain.customer.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CouponIssueRepositoryImpl 단위 테스트")
public class CouponIssueRepositoryImplTest {

    @Mock // CouponIssueJpaRepository를 Mock으로 생성 (DB 접근 X)
    private CouponIssueJpaRepository couponIssueJpaRepository;

    @InjectMocks // couponIssueJpaRepository를 CouponIssueRepositoryImpl에 주입
    private CouponIssueRepositoryImpl couponIssueRepository;

    @Test
    @DisplayName("save()가 CouponIssueJpaRepository의 save()를 호출해야 한다")
    void save_success() {
        // Given: Mock CouponIssue 객체 생성
        Coupon mockCoupon = Coupon.builder()
                .couponId(1L)
                .couponName("무료 배송 쿠폰")
                .discountAmount(2500)
                .maxQuantity(10)
                .issuedCount(5)
                .build();

        Customer mockCustomer = Customer.create(1L, "황지수");

        CouponIssue mockCouponIssue = CouponIssue.create(mockCoupon, mockCustomer);

        when(couponIssueJpaRepository.save(any(CouponIssue.class))).thenReturn(mockCouponIssue);

        // When: save 실행
        CouponIssue savedCouponIssue = couponIssueRepository.save(mockCouponIssue);

        // Then: 결과 검증
        assertThat(savedCouponIssue).isNotNull();
        assertThat(savedCouponIssue.getCoupon().getCouponName()).isEqualTo("무료 배송 쿠폰");

        // Verify: save()가 JpaRepository에서 정확히 한 번 호출되었는지 검증
        verify(couponIssueJpaRepository, times(1)).save(mockCouponIssue);
    }

    @Test
    @DisplayName("findByCustomerId()가 CouponIssueJpaRepository의 findByCustomerId()를 호출해야 한다")
    void findByCustomerId_success() {
        // Given: Mock CouponIssue 리스트 생성
        Coupon mockCoupon = Coupon.builder()
                .couponId(2L)
                .couponName("할인 쿠폰")
                .discountAmount(3000)
                .maxQuantity(20)
                .issuedCount(10)
                .build();

        Customer mockCustomer = Customer.create(2L, "황지수");

        CouponIssue mockCouponIssue = CouponIssue.create(mockCoupon, mockCustomer);

        when(couponIssueJpaRepository.findByCustomerId(2L)).thenReturn(List.of(mockCouponIssue));

        // When: findByCustomerId 실행
        List<CouponIssue> result = couponIssueRepository.findByCustomerId(2L);

        // Then: 결과 검증
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCustomer().getCustomerName()).isEqualTo("황지수");

        // Verify: findByCustomerId()가 JpaRepository에서 정확히 한 번 호출되었는지 검증
        verify(couponIssueJpaRepository, times(1)).findByCustomerId(2L);
    }

    @Test
    @DisplayName("findById()가 CouponIssueJpaRepository의 findById()를 호출해야 한다")
    void findById_success() {
        // Given: Mock CouponIssue 객체 생성
        Coupon mockCoupon = Coupon.builder()
                .couponId(3L)
                .couponName("회원 전용 쿠폰")
                .discountAmount(7000)
                .maxQuantity(15)
                .issuedCount(3)
                .build();

        Customer mockCustomer = Customer.create(3L, "황지수");

        CouponIssue mockCouponIssue = CouponIssue.create(mockCoupon, mockCustomer);

        when(couponIssueJpaRepository.findById(3L)).thenReturn(Optional.of(mockCouponIssue));

        // When: findById 실행
        Optional<CouponIssue> result = couponIssueRepository.findById(3L);

        // Then: 결과 검증
        assertThat(result).isPresent();
        assertThat(result.get().getCustomer().getCustomerName()).isEqualTo("황지수");

        // Verify: findById()가 JpaRepository에서 정확히 한 번 호출되었는지 검증
        verify(couponIssueJpaRepository, times(1)).findById(3L);
    }
}
