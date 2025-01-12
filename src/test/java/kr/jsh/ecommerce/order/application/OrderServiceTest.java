package kr.jsh.ecommerce.order.application;

import kr.jsh.ecommerce.customer.domain.Customer;
import kr.jsh.ecommerce.order.domain.*;
import kr.jsh.ecommerce.product.domain.Product;
import kr.jsh.ecommerce.product.domain.ProductSize;
import kr.jsh.ecommerce.product.domain.Stock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Test
    @DisplayName("주문 생성 성공")
    void testPlaceOrder() throws Exception {
        // Given
        Customer customer = new Customer("황지수", "서울시 관악구");
        Order order = new Order(customer, OrderStatus.PENDING_PAYMENT);
        OrderSheet expectedOrderSheet = new OrderSheet(order);

        // 이렇게까지 하고 싶진 않았어여,,,^_ㅠ
        Constructor<Stock> stockConstructor = Stock.class.getDeclaredConstructor();
        stockConstructor.setAccessible(true);
        Stock stock = stockConstructor.newInstance();

        ReflectionTestUtils.setField(stock, "productQuantity", 10); // 재고 수량 설정
        ReflectionTestUtils.setField(stock, "stockId", 1L);         // Stock ID 설정
        ReflectionTestUtils.setField(stock, "product", new Product("아디오스프로4", 299000, "아디다스"));
        ReflectionTestUtils.setField(stock, "productSize", new ProductSize(250));

        OrderItem orderItem = new OrderItem(stock, 1);
        List<OrderItem> orderItems = List.of(orderItem);

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        OrderSheet result = orderService.placeOrder(customer, orderItems);

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedOrderSheet.getOrder().getCustomer().getCustomerName(),
                result.getOrder().getCustomer().getCustomerName());
    }
}