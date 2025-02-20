package kr.jsh.ecommerce.event;

import kr.jsh.ecommerce.domain.order.Order;
import kr.jsh.ecommerce.domain.payment.DataPlatFormClient;
import kr.jsh.ecommerce.domain.payment.OrderData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaidEventListener {
    private final DataPlatFormClient dataPlatFormClient;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendOrderInfo(OrderPaidEvent event){
      log.info("결제 완료 감지! 결제 완료된 주문 id:{}",event.getOrder().getOrderId());
      sendOrderToExternalAPI(event.getOrder());
    }

    private void sendOrderToExternalAPI(Order order){
        dataPlatFormClient.sendOrderData(OrderData.from(order));
        log.info("외부 API로 주문 정보 전송 완료!");
    }
}
