package kr.jsh.ecommerce.order.facade;

import kr.jsh.ecommerce.customer.domain.Customer;
import kr.jsh.ecommerce.order.application.OrderService;
import kr.jsh.ecommerce.order.domain.Order;
import kr.jsh.ecommerce.order.domain.OrderSheet;
import kr.jsh.ecommerce.order.domain.OrderStatus;
import kr.jsh.ecommerce.order.presentation.dto.OrderRequest;
import kr.jsh.ecommerce.product.application.StockService;
import kr.jsh.ecommerce.product.domain.Product;
import kr.jsh.ecommerce.product.domain.ProductSize;
import kr.jsh.ecommerce.product.domain.ProductStatus;
import kr.jsh.ecommerce.product.domain.Stock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderFacadeTest {

    @InjectMocks
    private OrderFacade orderFacade;

    @Mock
    private OrderService orderService;

    @Mock
    private StockService stockService;

    @Test
    @DisplayName("재고 확인 후 주문 생성 성공")
    void testPlaceOrder() {
        // Given
        Customer customer = new Customer("황지수", "서울시 관악구");
        List<OrderRequest> orderRequests = List.of(
                new OrderRequest(1L, 235, 1),
                new OrderRequest(2L, 240, 1)
        );
        Stock stock1 = new Stock(new Product("알파플라이3", 329000, "나이키"), new ProductSize(235), 5, ProductStatus.IN_STOCK);
        Stock stock2 = new Stock(new Product("아디오스프로4", 299000, "아디다스"), new ProductSize(240), 3, ProductStatus.IN_STOCK);

        when(stockService.findAndValidateStock(1L, 235, 1)).thenReturn(stock1);
        when(stockService.findAndValidateStock(2L, 240, 1)).thenReturn(stock2);

        Order order = new Order(customer, OrderStatus.PENDING_PAYMENT);
        OrderSheet orderSheet = new OrderSheet(order);
        when(orderService.placeOrder(eq(customer), anyList())).thenReturn(orderSheet);

        // When
        OrderSheet result = orderFacade.placeOrder(customer, orderRequests);

        // Then
        Assertions.assertEquals(orderSheet, result);
    }
}
