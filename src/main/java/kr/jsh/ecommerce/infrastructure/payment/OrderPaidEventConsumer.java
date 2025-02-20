package kr.jsh.ecommerce.infrastructure.payment;

import kr.jsh.ecommerce.event.OrderPaidEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaidEventConsumer {

    @KafkaListener(topics = "order-paid-topic",groupId = "order-group")
    public void consume(OrderPaidEvent event){
        log.info("주문 이벤트 수신!: {}",event);
    }
}
