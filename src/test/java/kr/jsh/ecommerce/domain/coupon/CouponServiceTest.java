package kr.jsh.ecommerce.domain.coupon;

import kr.jsh.ecommerce.base.exception.BaseCustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CouponService 단위 테스트")
public class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    private Coupon mockCoupon;

    @BeforeEach
    void setUp(){
        mockCoupon = Coupon.builder()
                .couponId(3L)
                .couponName("설 연휴 특별 할인 쿠폰")
                .discountAmount(10000)
                .maxQuantity(10)
                .issuedCount(3)
                .build();
    }

    @Test
    @DisplayName("쿠폰을 조회해올 수 있어야 한다")
    void findCouponWithLock_success(){
        //Given : 특정 쿠폰 Mock 설정
        when(couponRepository.findByIdForUpdate(3L)).thenReturn(Optional.of(mockCoupon));

        //When : 쿠폰 조회 실행
        Coupon selectedCoupon = couponService.findCouponWithLock(3L);

        //Then : 반환된 쿠폰 검증
        assertThat(selectedCoupon).isNotNull();
        assertThat(selectedCoupon.getCouponName()).isEqualTo("설 연휴 특별 할인 쿠폰");
        assertThat(selectedCoupon.getIssuedCount()).isEqualTo(3);
        assertThat(selectedCoupon.getDiscountAmount()).isEqualTo(10000);

        //Verify
        verify(couponRepository,times(1)).findByIdForUpdate(3L);
    }

    @Test
    @DisplayName("findCouponWithLock()에서 존재하지 않는 쿠폰을 조회하면 예외가 발생해야 한다")
    void findCouponWithLock_couponNotFound() {
        // Given: 쿠폰이 존재하지 않는 경우
        when(couponRepository.findByIdForUpdate(3L)).thenReturn(Optional.empty());

        // When & Then: 예외 발생 검증
        BaseCustomException exception = assertThrows(BaseCustomException.class,
                () -> couponService.findCouponWithLock(3L));

        assertThat(exception.getMessage()).contains("쿠폰을 찾을 수 없습니다.");

        // Verify: findByIdForUpdate()가 정확히 한 번 호출되었는지 검증
        verify(couponRepository, times(1)).findByIdForUpdate(3L);
    }

    @Test
    @DisplayName("updateIssuedCount()가 쿠폰 발급 수량을 증가시키고 저장해야 한다")
    void updateIssuedCount_success() {
        // Given: Spy 객체 생성
        Coupon spyCoupon = spy(Coupon.builder()
                .couponId(1L)
                .couponName("무료 배송 쿠폰")
                .discountAmount(5000)
                .maxQuantity(10)
                .issuedCount(5) // 기존 발급 수량
                .build());

        when(couponRepository.save(any(Coupon.class))).thenReturn(spyCoupon);

        Coupon updatedCoupon = couponService.updateIssuedCount(spyCoupon);

        // Then: 발급 수량이 증가했는지 검증
        assertThat(updatedCoupon).isNotNull();
        assertThat(updatedCoupon.getIssuedCount()).isEqualTo(6); // 5 → 6 증가해야 함

        // Verify: issueCoupon()이 호출되었는지 검증
        verify(spyCoupon, times(1)).issueCoupon();
        verify(couponRepository, times(1)).save(spyCoupon);
    }

    //Mock 객체의 성질을 연구하기 위한 추가 테스트 1 (순서는 이 테스트가 제일 먼저 실행되었음^^;)
    @Test
    @DisplayName("updateIssuedCount()가 쿠폰 발급 수량을 증가시키고 저장해야 한다")
    void updateIssuedCount_success2() {
        // Given: Coupon 객체의 issueCoupon() 메서드 실행 후 저장
        when(couponRepository.save(any(Coupon.class))).thenReturn(mockCoupon);

        // When: 쿠폰 발급 수량 증가
        Coupon updatedCoupon = couponService.updateIssuedCount(mockCoupon);

        // Then: 반환된 쿠폰 검증
        assertThat(updatedCoupon).isNotNull();
        assertThat(updatedCoupon.getIssuedCount()).isEqualTo(4); //발급한 쿠폰 수 증가

        // Verify: save()가 정확히 한 번 호출되었는지 검증
        verify(couponRepository, times(1)).save(mockCoupon);
    }

    //Mock 객체의 성질을 연구하기 위한 추가 테스트 2
    @Test
    @DisplayName("updateIssuedCount()가 쿠폰 발급 수량을 증가시키고 저장해야 한다")
    void updateIssuedCount_success3() {
        // Mock 객체 명시적으로 생성
        Coupon mockCoupon = mock(Coupon.class);

        when(couponRepository.save(any(Coupon.class))).thenReturn(mockCoupon);

        // When: 쿠폰 발급 수량 증가
        Coupon updatedCoupon = couponService.updateIssuedCount(mockCoupon);

        // Then: 반환된 쿠폰 검증
        assertThat(updatedCoupon).isNotNull();
        assertThat(updatedCoupon.getIssuedCount()).isEqualTo(0);

        //  Mock 객체는 상태를 유지하지 않으므로 issuedCount가 증가하지 않음 (0일 가능)
        verify(mockCoupon, times(1)).issueCoupon(); // 실행 여부 검증
        verify(couponRepository, times(1)).save(mockCoupon);
    }
}
