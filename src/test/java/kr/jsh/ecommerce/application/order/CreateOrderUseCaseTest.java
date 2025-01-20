package kr.jsh.ecommerce.application.order;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.customer.CustomerService;
import kr.jsh.ecommerce.domain.fruit.Fruit;
import kr.jsh.ecommerce.domain.fruit.FruitService;
import kr.jsh.ecommerce.domain.order.Order;
import kr.jsh.ecommerce.domain.order.OrderFruit;
import kr.jsh.ecommerce.domain.order.OrderService;
import kr.jsh.ecommerce.interfaces.dto.order.OrderCreateResponse;
import kr.jsh.ecommerce.interfaces.dto.order.OrderFruitRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Mock
    private OrderService orderService;
    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    @Test
    @DisplayName("정상적으로 주문을 생성한다.")
    void createOrder_Success() {
        // Given
        String customerId = "1";
        List<OrderFruitRequest> orderFruitRequests = List.of(
                new OrderFruitRequest("1", "사과", 1000, 2),
                new OrderFruitRequest("2", "바나나", 500, 3)
        );

        Customer customer = new Customer(1L, "황지수", "서울시 관악구 문성로", "010-1234-5678", null);

        Order order = Order.builder()
                .customer(customer)
                .orderDate(LocalDateTime.now())
                .orderStatus("결제전")
                .orderFruits(new ArrayList<>())
                .build();

        OrderFruit orderFruit1 = new OrderFruit(order, customer,
                Fruit.builder()
                        .fruitId(1L)
                        .fruitName("사과")
                        .fruitStock(10)
                        .fruitPrice(1000)
                        .status("재고있음")
                        .build(),
                1000, 2);
        OrderFruit orderFruit2 = new OrderFruit(order, customer,
                Fruit.builder()
                        .fruitId(2L)
                        .fruitName("바나나")
                        .fruitStock(20)
                        .fruitPrice(500)
                        .status("재고있음")
                        .build(),
                500, 3);

        order.addOrderFruit(orderFruit1);
        order.addOrderFruit(orderFruit2);
        order.calculateTotalAmount(); // 총 금액 계산

        OrderCreateResponse expectedResponse = OrderCreateResponse.fromOrder(order);

        // When
        when(customerService.getCustomerById(1L)).thenReturn(customer);
        when(orderService.createOrder(customer)).thenReturn(order);
        when(orderService.saveOrder(order, orderFruitRequests)).thenReturn(expectedResponse);

        // Act
        OrderCreateResponse result = createOrderUseCase.createOrder(customerId, orderFruitRequests);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.orderId()).isEqualTo("1");
        assertThat(result.totalAmount()).isEqualTo(3500);

        verify(customerService).getCustomerById(1L);
        verify(orderService).createOrder(customer);
        verify(orderService).saveOrder(order, orderFruitRequests);
    }

    @Test
    @DisplayName("존재하지 않는 고객 ID로 주문 생성 시 예외를 던진다.")
    void createOrder_CustomerNotFound() {
        // Given
        String customerId = "999";
        List<OrderFruitRequest> orderFruitRequests = List.of(
                new OrderFruitRequest("1", "사과", 1000, 2)
        );

        when(customerService.getCustomerById(999L))
                .thenThrow(new BaseCustomException(BaseErrorCode.NOT_FOUND, new String[]{customerId}));

        // When & Then
        assertThatThrownBy(() -> createOrderUseCase.createOrder(customerId, orderFruitRequests))
                .isInstanceOf(BaseCustomException.class)
                .hasMessageContaining(BaseErrorCode.NOT_FOUND.getMessage());

        verify(customerService).getCustomerById(999L);
        verifyNoInteractions(orderService);
    }
}
