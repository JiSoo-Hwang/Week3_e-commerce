package kr.jsh.ecommerce.application.payment;

import kr.jsh.ecommerce.domain.coupon.Coupon;
import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import kr.jsh.ecommerce.domain.coupon.CouponIssueService;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.order.Order;
import kr.jsh.ecommerce.domain.order.OrderService;
import kr.jsh.ecommerce.domain.order.OrderStatus;
import kr.jsh.ecommerce.domain.payment.Payment;
import kr.jsh.ecommerce.domain.payment.PaymentService;
import kr.jsh.ecommerce.domain.payment.PaymentStatus;
import kr.jsh.ecommerce.domain.wallet.Wallet;
import kr.jsh.ecommerce.interfaces.api.payment.dto.PaymentRequest;
import kr.jsh.ecommerce.interfaces.api.payment.dto.PaymentResponse;
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

@ExtendWith(MockitoExtension.class) // Mockito 확장 사용
@DisplayName("PayForOrderUseCase 단위 테스트")
class PayForOrderUseCaseTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private OrderService orderService;

    @Mock
    private CouponIssueService couponIssueService;

    @InjectMocks
    private PayForOrderUseCase payForOrderUseCase;

    private Order mockOrder;
    private Customer mockCustomer;
    private Wallet mockWallet;
    private Coupon mockCoupon;
    private CouponIssue mockCouponIssue;
    private Payment mockPayment;

    @Test
    @DisplayName("쿠폰 없이 정상 결제")
    void payForOrder_withoutCoupon() {
        // Given
        Long orderId = 1L;
        int totalAmount = 20000;

        PaymentRequest request = new PaymentRequest(orderId, null, PaymentStatus.PENDING);

        // Mock Customer & Wallet 설정
        Customer spyCustomer = spy(Customer.create("황지수","서울시","01012345678"));
        Wallet mockWallet = mock(Wallet.class);
        when(spyCustomer.getWallet()).thenReturn(mockWallet);
        when(mockWallet.getBalance()).thenReturn(50000); // 지갑 잔액 설정

        // Order 객체 생성 (Customer 포함)
        Order spyOrder = spy(Order.builder()
                .customer(spyCustomer) // Customer 추가
                .totalAmount(totalAmount)
                .orderStatus(OrderStatus.PENDING)
                .build());

        when(spyOrder.getOrderId()).thenReturn(orderId);
        when(orderService.findOrderById(orderId)).thenReturn(spyOrder);

        // 결제 객체를 실제 Order와 함께 생성
        Payment mockPayment = new Payment(1L, spyOrder, totalAmount, PaymentStatus.SUCCESS, LocalDateTime.now());

        when(paymentService.createPayment(spyOrder, null)).thenReturn(mockPayment);

        // When
        PaymentResponse response = payForOrderUseCase.payForOrder(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.amount()).isEqualTo(20000);
        assertThat(response.paymentStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(response.orderId()).isEqualTo(orderId); // 주문 ID 검증

        // Verify
        verify(orderService, times(1)).findOrderById(orderId);
        verify(paymentService, times(1)).createPayment(spyOrder, null);
    }

    //여러 번 방법을 바꿔가며 시도하고 있지만, 실패. 갈 수록 테스트를 위한 테스트가 되어가는 것 같아 포기!
    @Test
    @DisplayName("쿠폰을 적용하여 정상 결제")
    void payForOrder_withCoupon() {
        // Given
        Long orderId = 1L;
        Long couponIssueId = 10L;
        int totalAmount = 20000;
        int discountAmount = 5000;
        int expectedFinalAmount = totalAmount - discountAmount;

        PaymentRequest request = new PaymentRequest(orderId, couponIssueId, PaymentStatus.PENDING);

        // ✅ Mock 객체 생성
        Customer mockCustomer = mock(Customer.class);
        Wallet mockWallet = mock(Wallet.class);
        when(mockCustomer.getWallet()).thenReturn(mockWallet);
        when(mockWallet.getBalance()).thenReturn(50000);

        Order mockOrder = mock(Order.class);
        when(mockOrder.getCustomer()).thenReturn(mockCustomer);
        when(mockOrder.getTotalAmount()).thenReturn(totalAmount);
        when(mockOrder.getOrderId()).thenReturn(orderId);
        when(orderService.findOrderById(orderId)).thenReturn(mockOrder);

        // ✅ Coupon & CouponIssue Spy 설정 (Mock → Spy)
        Coupon mockCoupon = mock(Coupon.class);
        when(mockCoupon.getDiscountAmount()).thenReturn(discountAmount);

        CouponIssue spyCouponIssue = spy(CouponIssue.create(mockCoupon, mockCustomer));
        when(couponIssueService.findIssuedCouponById(couponIssueId)).thenReturn(spyCouponIssue);
        when(spyCouponIssue.getCoupon()).thenReturn(mockCoupon);

        // ✅ `markAsUsed()`를 Stub 처리 (실제 실행)
        doNothing().when(spyCouponIssue).markAsUsed();

        // ✅ 결제 객체 Mock 설정
        Payment mockPayment = new Payment(1L, mockOrder, expectedFinalAmount, PaymentStatus.SUCCESS, LocalDateTime.now());
        when(paymentService.createPayment(mockOrder, spyCouponIssue)).thenReturn(mockPayment);

        // When
        PaymentResponse response = payForOrderUseCase.payForOrder(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.amount()).isEqualTo(expectedFinalAmount);
        assertThat(response.paymentStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(response.orderId()).isEqualTo(orderId);

        // ✅ Verify
        verify(orderService, times(1)).findOrderById(orderId);
        verify(couponIssueService, times(1)).findIssuedCouponById(couponIssueId);
        verify(paymentService, times(1)).createPayment(mockOrder, spyCouponIssue);

        // ✅ markAsUsed()가 호출되었는지 검증
        verify(spyCouponIssue, times(1)).markAsUsed();
    }

}
