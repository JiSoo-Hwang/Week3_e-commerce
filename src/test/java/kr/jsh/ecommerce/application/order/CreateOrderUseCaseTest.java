package kr.jsh.ecommerce.application.order;



import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.customer.CustomerService;
import kr.jsh.ecommerce.domain.fruit.Fruit;
import kr.jsh.ecommerce.domain.fruit.FruitService;
import kr.jsh.ecommerce.domain.order.Order;
import kr.jsh.ecommerce.domain.order.OrderFruit;
import kr.jsh.ecommerce.domain.order.OrderService;
import kr.jsh.ecommerce.domain.order.OrderStatus;
import kr.jsh.ecommerce.interfaces.api.order.dto.OrderCreateRequest;
import kr.jsh.ecommerce.interfaces.api.order.dto.OrderCreateResponse;
import kr.jsh.ecommerce.interfaces.api.order.dto.OrderFruitRequest;
import kr.jsh.ecommerce.interfaces.api.order.dto.OrderFruitResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateOrderUseCaseTest {

    @Mock
    private OrderService orderService;

    @Mock
    private CustomerService customerService;

    @Mock
    private FruitService fruitService;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    private OrderCreateRequest orderCreateRequest;
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

        orderCreateRequest = new OrderCreateRequest(
                "1",
                List.of(new OrderFruitRequest("1","사과",1000,2),
                        new OrderFruitRequest("2","바나나",500,1))
        );
    }

    @Test
    @DisplayName("createOrder()가 정상적으로 주문을 생성해야 한다")
    void createOrderTest(){
        //Given : Mock 객체 동작 정의
        when(customerService.findCustomerById(1L)).thenReturn(mockCustomer);
        when(orderService.createOrder(mockCustomer)).thenReturn(mockOrder);

        //Given : OrderCreateResponse 생성시 DTO 변환
        OrderCreateResponse mockResponse = OrderCreateResponse.fromOrder(mockOrder);
        when(orderService.saveOrder(any(Order.class),any())).thenReturn(mockResponse);

        //When : createOrder() 실행
        OrderCreateResponse response = createOrderUseCase.createOrder(orderCreateRequest);

        //Then : 응답 검증
        assertThat(response).isNotNull();
        assertThat(response.orderId()).isEqualTo(String.valueOf(mockOrder.getOrderId()));
        assertThat(response.customerName()).isEqualTo(mockCustomer.getCustomerName());
        assertThat(response.orderDate()).isEqualTo(mockOrderDate);
        assertThat(response.totalAmount()).isEqualTo(2500);
        assertThat(response.orderStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(response.orderFruits()).hasSize(2);

        //Verify : 각 메서드가 올바르게 호출되었는지 검증
        verify(customerService,times(1)).findCustomerById(1L);
        verify(orderService,times(1)).createOrder(mockCustomer);
        verify(orderService,times(1)).saveOrder(any(Order.class),any());

    }
}
