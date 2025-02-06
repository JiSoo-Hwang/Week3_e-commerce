package kr.jsh.ecommerce.domain.coupon;

import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.customer.CustomerRepository;
import kr.jsh.ecommerce.interfaces.api.coupon.dto.CouponIssueResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CouponIssueService 단위 테스트")
public class CouponIssueServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponIssueRepository couponIssueRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CouponIssueService couponIssueService;

    private Coupon mockCoupon;
    private Customer mockCustomer;
    private CouponIssue mockCouponIssue;

    @BeforeEach
    void setUp(){
        mockCoupon = Coupon.builder()
                .couponId(1L)
                .couponName("무료 배송 쿠폰")
                .discountAmount(2500)
                .maxQuantity(10)
                .issuedCount(5)
                .build();

        mockCustomer = Customer.create(300L,"황지수");

        mockCouponIssue = CouponIssue.create(mockCoupon,mockCustomer);
    }

    @Test
    @DisplayName("쿠폰을 정상적으로 발급해야 한다")
    void issueCoupon_success(){
        //Given : 쿠폰 및 고객 존재 여부 Mock 설정
        when(couponRepository.findById(1L)).thenReturn(Optional.of(mockCoupon));
        when(customerRepository.findById(300L)).thenReturn(Optional.of(mockCustomer));
        when(couponIssueRepository.save(any(CouponIssue.class))).thenReturn(mockCouponIssue);

        //When : 쿠폰 발급 실행
        CouponIssue issuedCoupon = couponIssueService.issueCoupon(1L,300L);

        //Then : 발급된 쿠폰 검증
        assertThat(issuedCoupon).isNotNull();
        assertThat(issuedCoupon.getCoupon().getCouponName()).isEqualTo("무료 배송 쿠폰");
        assertThat(issuedCoupon.getCustomer().getCustomerName()).isEqualTo("황지수");
        assertThat(issuedCoupon.getStatus()).isEqualTo(CouponStatus.ISSUED);

        //Verify : 필요한 메서드가 올바르게 호출되었는지 검증
        verify(couponRepository,times(1)).findById(1L);
        verify(customerRepository,times(1)).findById(300L);
        verify(couponIssueRepository,times(1)).save(any(CouponIssue.class));
    }
    @Test
    @DisplayName("존재하지 않는 쿠폰 발급 시 예외가 발생해야 한다")
    void issueCoupon_couponNotFound() {
        // Given: 쿠폰이 존재하지 않는 경우 Mock 설정
        when(couponRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then: 예외 발생 검증
        BaseCustomException exception = assertThrows(BaseCustomException.class,
                () -> couponIssueService.issueCoupon(1L, 100L));

        assertThat(exception.getMessage()).contains("쿠폰이 존재하지 않습니다.");

        // Verify: 쿠폰이 존재하지 않으므로 이후 메서드는 호출되지 않아야 함
        verify(customerRepository, never()).findById(anyLong());
        verify(couponIssueRepository, never()).save(any(CouponIssue.class));
    }

    @Test
    @DisplayName("존재하지 않는 고객에게 쿠폰을 발급하려 하면 예외가 발생해야 한다")
    void issueCoupon_customerNotFound() {
        // Given: 쿠폰은 존재하지만 고객이 존재하지 않는 경우
        when(couponRepository.findById(1L)).thenReturn(Optional.of(mockCoupon));
        when(customerRepository.findById(100L)).thenReturn(Optional.empty());

        // When & Then: 예외 발생 검증
        BaseCustomException exception = assertThrows(BaseCustomException.class,
                () -> couponIssueService.issueCoupon(1L, 100L));

        assertThat(exception.getMessage()).contains("고객이 존재하지 않습니다.");

        // Verify: 고객이 존재하지 않으므로 이후 메서드는 호출되지 않아야 함
        verify(couponIssueRepository, never()).save(any(CouponIssue.class));
    }

    @Test
    @DisplayName("이미 발급된 쿠폰을 다시 발급하려 하면 예외가 발생해야 한다")
    void issueCoupon_alreadyIssued() {
        // Given: 쿠폰 및 고객 존재, 하지만 이미 발급된 경우
        when(couponRepository.findById(1L)).thenReturn(Optional.of(mockCoupon));
        when(customerRepository.findById(100L)).thenReturn(Optional.of(mockCustomer));

        // When & Then: 예외 발생 검증
        BaseCustomException exception = assertThrows(BaseCustomException.class,
                () -> couponIssueService.issueCoupon(1L, 100L));

        assertThat(exception.getMessage()).contains("이미 발급된 쿠폰");

        // Verify: 쿠폰이 중복 발급된 경우 저장 메서드는 호출되지 않아야 함
        verify(couponIssueRepository, never()).save(any(CouponIssue.class));
    }

    @Test
    @DisplayName("특정 고객이 보유한 쿠폰 목록을 가져와야 한다")
    void getCouponsByCustomerId() {
        // Given: 특정 고객이 보유한 쿠폰 Mock 설정
        when(couponIssueRepository.findByCustomerId(100L)).thenReturn(List.of(mockCouponIssue));

        // When: 쿠폰 조회 실행
        List<CouponIssueResponse> responseList = couponIssueService.getCouponsByCustomerId(100L);

        // Then: 반환된 쿠폰 목록 검증
        assertThat(responseList).isNotEmpty();
        assertThat(responseList.get(0).couponName()).isEqualTo("무료 배송 쿠폰");

        // Verify: 쿠폰 조회가 한 번 호출되었는지 확인
        verify(couponIssueRepository, times(1)).findByCustomerId(100L);
    }

    @Test
    @DisplayName("발급된 쿠폰 ID로 쿠폰을 조회해야 한다")
    void findIssuedCouponById() {
        // Given: 특정 쿠폰 발급 ID로 조회
        when(couponIssueRepository.findById(10L)).thenReturn(Optional.of(mockCouponIssue));

        // When: 쿠폰 조회 실행
        CouponIssue result = couponIssueService.findIssuedCouponById(10L);

        // Then: 반환된 쿠폰 검증
        assertThat(result).isNotNull();
        assertThat(result.getCoupon().getCouponName()).isEqualTo("무료 배송 쿠폰");

        // Verify: 쿠폰 조회가 한 번 호출되었는지 확인
        verify(couponIssueRepository, times(1)).findById(10L);
    }

}

