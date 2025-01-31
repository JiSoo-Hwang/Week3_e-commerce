package kr.jsh.ecommerce.infrastructure.coupon;

import kr.jsh.ecommerce.domain.coupon.Coupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CouponRepositoryImpl 단위 테스트")
public class CouponRepositoryImplTest {

    @Mock
    private CouponJpaRepository couponJpaRepository;

    @InjectMocks
    private CouponRepositoryImpl couponRepository;

    @Test
    @DisplayName("findById()가 CouponJpaRepository의 findById()를 호출해야 한다")
    void findById_success() {
        // Given: Mock Coupon 객체 생성
        Coupon mockCoupon = Coupon.builder()
                .couponId(1L)
                .couponName("무료 배송 쿠폰")
                .discountAmount(2500)
                .maxQuantity(10)
                .issuedCount(5)
                .build();

        when(couponJpaRepository.findById(1L)).thenReturn(Optional.of(mockCoupon));

        // When: findById 실행
        Optional<Coupon> result = couponRepository.findById(1L);

        // Then: 결과 검증
        assertThat(result).isPresent();
        assertThat(result.get().getCouponName()).isEqualTo("무료 배송 쿠폰");

        // Verify: findById()가 JpaRepository에서 정확히 한 번 호출되었는지 검증
        verify(couponJpaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("save()가 CouponJpaRepository의 save()를 호출해야 한다")
    void save_success() {
        // Given: Mock Coupon 객체 생성
        Coupon mockCoupon = Coupon.builder()
                .couponId(2L)
                .couponName("할인 쿠폰")
                .discountAmount(3000)
                .maxQuantity(20)
                .issuedCount(0)
                .build();

        when(couponJpaRepository.save(any(Coupon.class))).thenReturn(mockCoupon);

        // When: save 실행
        Coupon savedCoupon = couponRepository.save(mockCoupon);

        // Then: 결과 검증
        assertThat(savedCoupon).isNotNull();
        assertThat(savedCoupon.getCouponName()).isEqualTo("할인 쿠폰");

        // Verify: save()가 JpaRepository에서 정확히 한 번 호출되었는지 검증
        verify(couponJpaRepository, times(1)).save(mockCoupon);
    }

    @Test
    @DisplayName("findByIdForUpdate()가 CouponJpaRepository의 findByIdForUpdate()를 호출해야 한다")
    void findByIdForUpdate_success() {
        // Given: Mock Coupon 객체 생성
        Coupon mockCoupon = Coupon.builder()
                .couponId(3L)
                .couponName("신규회원 전용 쿠폰")
                .discountAmount(10000)
                .maxQuantity(15)
                .issuedCount(3)
                .build();

        when(couponJpaRepository.findByIdForUpdate(3L)).thenReturn(Optional.of(mockCoupon));

        // When: findByIdForUpdate 실행
        Optional<Coupon> result = couponRepository.findByIdForUpdate(3L);

        // Then: 결과 검증
        assertThat(result).isPresent();
        assertThat(result.get().getDiscountAmount()).isEqualTo(10000);

        // Verify: findByIdForUpdate()가 JpaRepository에서 정확히 한 번 호출되었는지 검증
        verify(couponJpaRepository, times(1)).findByIdForUpdate(3L);
    }
}
