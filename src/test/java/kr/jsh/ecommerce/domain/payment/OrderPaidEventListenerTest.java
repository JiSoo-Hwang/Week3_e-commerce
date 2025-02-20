package kr.jsh.ecommerce.domain.payment;

import kr.jsh.ecommerce.domain.order.Order;
import kr.jsh.ecommerce.event.OrderPaidEvent;
import kr.jsh.ecommerce.event.OrderPaidEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderPaidEventListenerTest {

    @Mock
    private DataPlatFormClient dataPlatFormClient;

    @InjectMocks
    private OrderPaidEventListener orderPaidEventListener;

    private Order order;

    @BeforeEach
    void setUp(){
        order = mock(Order.class);
        when(order.getOrderId()).thenReturn(100L);
    }

    @Test
    void sendOrderInfoAfterPayment(){
        //given
        OrderPaidEvent event = new OrderPaidEvent(order);

        //when
        orderPaidEventListener.sendOrderInfo(event);

        //then
        //외부 API가 한 번 호출되었는지 확인
        verify(dataPlatFormClient,times(1)).sendOrderData(any(Long.class));
    }
}
