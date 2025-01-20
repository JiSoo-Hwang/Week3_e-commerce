package kr.jsh.ecommerce.order.presentation;

import kr.jsh.ecommerce.customer.domain.Customer;
import kr.jsh.ecommerce.order.domain.Order;
import kr.jsh.ecommerce.order.domain.OrderSheet;
import kr.jsh.ecommerce.order.domain.OrderStatus;
import kr.jsh.ecommerce.order.facade.OrderFacade;
import kr.jsh.ecommerce.order.presentation.controller.OrderController;
import kr.jsh.ecommerce.order.presentation.dto.OrderClientRequest;
import kr.jsh.ecommerce.order.presentation.dto.OrderRequest;
// JUnit 5 (Jupiter) 관련
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

// Mockito 관련
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Spring MockMvc 관련
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

// Mockito BDD 스타일
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

// Spring Test WebMvc 요청 관련
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderFacade orderFacade;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    @DisplayName("주문 요청 성공")
    void testPlaceOrder() throws Exception {
        // Given
        Customer customer = new Customer("황지수", "서울시 관악구");
        List<OrderRequest> orderRequests = List.of(
                new OrderRequest(1L, 235, 1),
                new OrderRequest(2L, 240, 1)
        );
        OrderClientRequest request = new OrderClientRequest(customer, orderRequests);
        OrderSheet orderSheet = new OrderSheet(new Order(customer, OrderStatus.PENDING_PAYMENT));

        // 현타 씨게 온다... 이건 어떻게 바꾸지ㅠㅠㅠ
        when(orderFacade.placeOrder(
                argThat(c -> c.getCustomerName().equals(customer.getCustomerName()) &&
                        c.getCustomerAddress().equals(customer.getCustomerAddress())),
                argThat(req -> req.size() == 2 &&
                        req.get(0).productId().equals(1L) &&
                        req.get(0).productSize() == 235 &&
                        req.get(0).productQuantity() == 1 &&
                        req.get(1).productId().equals(2L) &&
                        req.get(1).productSize() == 240 &&
                        req.get(1).productQuantity() == 1)
        )).thenReturn(orderSheet);

        // When & Then
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
