package kr.jsh.ecommerce.infrastructure.payment;

import kr.jsh.ecommerce.domain.payment.DataPlatFormClient;
import kr.jsh.ecommerce.domain.payment.OrderData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MockDataPlatformClient implements DataPlatFormClient {
    @Async
    @Override
    public void sendOrderData(OrderData orderData) {
        try{
            log.info("주문 정보 전송. {}",orderData);
        }catch (Exception e){
            log.error("주문 정보 전송 실패. 주문아이디={}",orderData.getOrderId(),e);
        }
    }
}
