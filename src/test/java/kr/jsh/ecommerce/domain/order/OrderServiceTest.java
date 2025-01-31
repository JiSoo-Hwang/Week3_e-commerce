package kr.jsh.ecommerce.domain.order;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.fruit.Fruit;
import kr.jsh.ecommerce.domain.fruit.FruitRepository;
import kr.jsh.ecommerce.interfaces.api.order.dto.OrderCreateResponse;
import kr.jsh.ecommerce.interfaces.api.order.dto.OrderFruitRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private FruitRepository fruitRepository;

    @InjectMocks
    private OrderService orderService;

    private Customer mockCustomer;
    private Order mockOrder;
    private Fruit mockFruit;
    private OrderFruitRequest mockOrderFruitRequest;

    @BeforeEach
    void setUp(){
        //Given : 가짜 Customer 객체 생성
        mockCustomer = Customer.create(5L,"이호민");

        //Given : 가짜 Order 객체 생성
        mockOrder = Order.builder()
                .customer(mockCustomer)
                .orderDate(LocalDateTime.of(2025,01,29,17,45))
                .orderStatus(OrderStatus.PENDING)
                .totalAmount(0)
                .build();

        //Given : 가짜 Fruit 객체 생성
        mockFruit = Fruit.create("딸기",10000,10,"재고있음");

        //Given : 가짜 OrderFruitRequest 생성 (테스트용 상품 주문)
        mockOrderFruitRequest = new OrderFruitRequest("3","딸기",10000,3);
    }

    @Test
    @DisplayName("createOrder()는 새로운 주문 객체를 생성하여야 한다")
    void createOrder_success(){
        //When : 주문 생성
        Order order = orderService.createOrder(mockCustomer);

        //Then : 검증 (주문이 정상적으로 생성되었는지 확인)
        assertThat(order).isNotNull();
        assertThat(order.getCustomer()).isEqualTo(mockCustomer);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.getTotalAmount()).isEqualTo(0);
    }

    @Test
    @DisplayName("saveOrder()는 상품 재고를 차감하고 주문을 저장해야 한다")
    void saveOrder_success(){
        // Given : Mock 설정(상품 조회)
        when(fruitRepository.findByIdForUpdate(3L)).thenReturn(Optional.of(mockFruit));

        //Given : Mock 설정(상품 재고 차감 후 저장)
        when(fruitRepository.save(any(Fruit.class))).thenReturn(mockFruit);

        //Given : Mock 설정 (주문 저장)
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        //When : 주문 저장 실행
        OrderCreateResponse response = orderService.saveOrder(mockOrder, List.of(mockOrderFruitRequest));

        //Then : 응답 검증
        assertThat(response).isNotNull();
        assertThat(response.orderId()).isEqualTo(String.valueOf(mockOrder.getOrderId()));
        assertThat(response.customerName()).isEqualTo(mockCustomer.getCustomerName());
        assertThat(response.totalAmount()).isEqualTo(30000);

        //Verify : 각 메서드가 올바르게 호출되었는지 확인
        verify(fruitRepository,times(1)).findByIdForUpdate(3L);
        verify(fruitRepository,times(1)).save(any(Fruit.class));
        verify(orderRepository,times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("findOrderById()는 존재하는 주문을 반환해야 한다")
    void findOrderById_success(){
        //Given : Mock 설정 (주문 조회)
        when(orderRepository.findById(100L)).thenReturn(Optional.of(mockOrder));

        //When : 주문 조회 실행
        Order order = orderService.findOrderById(100L);

        //Then : 응답 검증
        assertThat(order).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PENDING);

        //Verify : Repository 메서드가 한 번 호출되었는지 확인
        verify(orderRepository,times(1)).findById(100L);
    }

    @Test
    @DisplayName("findByOrderId()는 존재하지 않는 주문ID 조회 시 예외를 던져야 한다")
    void findOrderById_notFound(){
        //Given : Mock 설정 (주문 없음)
        when(orderRepository.findById(300L)).thenReturn(Optional.empty());

        //When & Then : 예외 발생 검증
        BaseCustomException exception = assertThrows(BaseCustomException.class,
                ()->orderService.findOrderById(300L));

        assertThat(exception.getBaseErrorCode()).isEqualTo(BaseErrorCode.NOT_FOUND);

        //Verify : Repository 메서드가 한 번 호출되었는지 확인
        verify(orderRepository,times(1)).findById(300L);
    }
}
