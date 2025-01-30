package kr.jsh.ecommerce.application.coupon;

import kr.jsh.ecommerce.domain.coupon.Coupon;
import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import kr.jsh.ecommerce.domain.coupon.CouponIssueService;
import kr.jsh.ecommerce.domain.coupon.CouponStatus;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.interfaces.api.coupon.dto.CouponIssueRequest;
import kr.jsh.ecommerce.interfaces.api.coupon.dto.CouponIssueResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("IssueCouponUseCase 단위 테스트")
public class IssueCouponUseCaseTest {

    @Mock
    private CouponIssueService couponIssueService;

    @InjectMocks
    private IssueCouponUseCase issueCouponUseCase;

    private CouponIssueRequest couponIssueRequest;
    private CouponIssue mockIssuedCoupon;

    @BeforeEach
    void setUp(){
        //테스트용 요청 객체 생성
        couponIssueRequest = new CouponIssueRequest(5L,500L);

        //Mock CouponIssue 객체 생성
        mockIssuedCoupon = CouponIssue.builder()
                .coupon(Coupon.builder()
                        .couponId(1L)
                        .couponName("배송비 할인 쿠폰")
                        .discountAmount(2500)
                        .maxQuantity(10)
                        .issuedCount(5)
                        .build())
                .customer(Customer.create(500L, "이호민"))
                .issuedAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusDays(30))
                .status(CouponStatus.ISSUED)
                .build();
    }

    @Test
    @DisplayName("쿠폰을 정상적으로 발급해야 한다")
    void issueCoupon_success(){
        //Given: "couponIssueService.issueCoupon()" 호출 시 Mock 객체 반환 설정
        when(couponIssueService.issueCoupon(5L,500L)).thenReturn(mockIssuedCoupon);

        //When: 테스트 대상 실행
        CouponIssueResponse response = issueCouponUseCase.issueCoupon(couponIssueRequest);

        // Then: 반환된 응답이 올바른지 검증
        assertThat(response).isNotNull();
        assertThat(response.couponName()).isEqualTo("배송비 할인 쿠폰");
        assertThat(response.status()).isEqualTo(CouponStatus.ISSUED);

        // Verify: couponIssueService.issueCoupon()이 정확히 한 번 호출되었는지 검증
        verify(couponIssueService, times(1)).issueCoupon(5L, 500L);
    }

}
