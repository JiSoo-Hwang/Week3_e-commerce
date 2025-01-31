package kr.jsh.ecommerce.domain.payment;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.coupon.Coupon;
import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.order.Order;
import kr.jsh.ecommerce.domain.order.OrderRepository;
import kr.jsh.ecommerce.domain.order.OrderStatus;
import kr.jsh.ecommerce.domain.wallet.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PaymentService 단위 테스트")
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Wallet mockWallet;

    @Mock
    private Order mockOrder;

    @Mock
    private CouponIssue mockCouponIssue;

    @Mock
    private Coupon mockCoupon;

    @BeforeEach
    void setUp() {
        // 기본 Order 설정
        when(mockOrder.getTotalAmount()).thenReturn(20000);
        when(mockOrder.getCustomer()).thenReturn(mock(Customer.class));
        when(mockOrder.getCustomer().getWallet()).thenReturn(mockWallet);
    }

    @Test
    @DisplayName("쿠폰 없이 정상 결제")
    void createPayment_withoutCoupon() {
        // Given
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Payment result = paymentService.createPayment(mockOrder, null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(20000); // 원래 금액
        assertThat(result.getPaymentStatus()).isEqualTo(PaymentStatus.SUCCESS);

        // Verify
        verify(mockWallet, times(1)).spendCash(20000);
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(orderRepository, times(1)).save(mockOrder);
    }

    @Test
    @DisplayName("쿠폰을 적용하여 정상 결제")
    void createPayment_withCoupon() {
        // Given
        when(mockCouponIssue.getCoupon()).thenReturn(mockCoupon);
        when(mockCoupon.getDiscountAmount()).thenReturn(5000); // 할인 금액 Stubbing
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Payment result = paymentService.createPayment(mockOrder, mockCouponIssue);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(15000); // 20,000 - 5,000 할인 적용
        assertThat(result.getPaymentStatus()).isEqualTo(PaymentStatus.SUCCESS);

        // Verify
        verify(mockCouponIssue, times(1)).markAsUsed(); // 쿠폰 사용 처리 검증
        verify(mockWallet, times(1)).spendCash(15000);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }


    @Test
    @DisplayName("잔액 부족으로 결제 실패")
    void createPayment_insufficientBalance() {
        // Given
        doThrow(new BaseCustomException(BaseErrorCode.INSUFFICIENT_BALANCE)) // 잔액 부족 예외
                .when(mockWallet).spendCash(anyInt());

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Payment result = paymentService.createPayment(mockOrder, null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);

        // Verify
        verify(mockWallet, times(1)).spendCash(20000);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }


    @Test
    @DisplayName("결제 후 Order에 Payment가 저장됨")
    void createPayment_setsOrderPayment() {
        // Given
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Payment result = paymentService.createPayment(mockOrder, null);

        // Then
        assertThat(result).isNotNull();
        verify(mockOrder, times(1)).setPayment(result);
        verify(orderRepository, times(1)).save(mockOrder);
    }

}
