package kr.jsh.ecommerce.domain.payment;

import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.fruit.Fruit;
import kr.jsh.ecommerce.domain.order.Order;
import kr.jsh.ecommerce.domain.order.OrderFruit;
import kr.jsh.ecommerce.domain.order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Payment 엔터티 테스트")
class PaymentTest {

    private Customer mockCustomer;
    private Order mockOrder;
    private List<OrderFruit> mockOrderFruits;
    private LocalDateTime mockOrderDate;

    @BeforeEach
    void setUp(){
        mockCustomer = Customer.create(1L, "황지수");

        mockOrderDate = LocalDateTime.of(2025,01,29,16,46);


        Fruit apple = Fruit.create("사과",1000,10,"재고있음");
        Fruit banana = Fruit.create("바나나", 500, 5,"재고있음");
        mockOrderFruits = List.of(
                new OrderFruit(mockOrder,apple, 1000,2),
                new OrderFruit(mockOrder,banana,500,1)
        );
        mockOrder = Order.builder()
                .orderId(100L)
                .customer(mockCustomer)
                .orderDate(mockOrderDate)
                .totalAmount(2500)
                .orderStatus(OrderStatus.PENDING)
                .orderFruits(mockOrderFruits)
                .build();
    }

    @Test
    @DisplayName("새 결제 객체를 생성하면 상태가 PENDING이어야 한다")
    void createPayment_defaultPendingStatus() {
        // Given
        int paymentAmount = 20000;

        // When
        Payment payment = Payment.create(mockOrder, paymentAmount);

        // Then
        assertThat(payment).isNotNull();
        assertThat(payment.getOrder()).isEqualTo(mockOrder);
        assertThat(payment.getAmount()).isEqualTo(paymentAmount);
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.PENDING); // 기본 상태 검증
        assertThat(payment.getPaidAt()).isNotNull(); // 결제 시간이 자동으로 설정됨
    }

    @Test
    @DisplayName("결제 완료 시 상태가 SUCCESS로 변경되어야 한다")
    void completePayment_shouldChangeStatusToSuccess() {
        // Given
        Payment payment = Payment.create(mockOrder, 20000);

        // When
        payment.completePayment();

        // Then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.SUCCESS);
    }

    @Test
    @DisplayName("결제 실패 시 상태가 FAILED로 변경되어야 한다")
    void failPayment_shouldChangeStatusToFailed() {
        // Given
        Payment payment = Payment.create(mockOrder, 20000);

        // When
        payment.failPayment();

        // Then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);
    }

    @Test
    @DisplayName("결제 환불 시 상태가 REFUNDED로 변경되어야 한다")
    void refundPayment_shouldChangeStatusToRefunded() {
        // Given
        Payment payment = Payment.create(mockOrder, 20000);
        payment.completePayment(); // 먼저 결제 완료 상태로 변경

        // When
        payment.refundPayment();

        // Then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
    }
}