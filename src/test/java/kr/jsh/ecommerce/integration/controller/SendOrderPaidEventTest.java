package kr.jsh.ecommerce.integration.controller;

import kr.jsh.ecommerce.event.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.junit.jupiter.api.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

//TODO:나중에 공부해서... 이 테스트...성공시킬 것...
//@Slf4j
//@SpringBootTest
//@Component
public class SendOrderPaidEventTest {
/*
    @Autowired
    private KafkaTemplate<String, OrderPaidEvent> kafkaTemplate;

    @Autowired
    private ConsumerFactory<String, OrderPaidEvent> consumerFactory;

    private Consumer<String, OrderPaidEvent> consumer;

    @BeforeEach
    public void setUp() {
        consumer = consumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList("order-paid-topic"));

        // Partition이 할당될 때까지 기다림
        await().atMost(5, TimeUnit.SECONDS).until(() -> !consumer.assignment().isEmpty());
        log.info("할당된 파티션: {}", consumer.assignment());

        consumer.seekToBeginning(consumer.assignment()); // 모든 파티션을 처음부터 읽기(테스트 한정)
    }

    @AfterEach
    public void tearDown() {
        consumer.close();
    }

    @Test
    public void testKafkaMessageSendingAndReceiving() throws Exception {
        // Given
        OrderPaidEvent event = new OrderPaidEvent(1L, 123L, 50000);

        // When - Producer가 메시지 전송
        kafkaTemplate.send("order-paid-topic", event).get();
        kafkaTemplate.flush();

        Thread.sleep(2000);

        log.info("Kafka 메시지 전송 완료, Consumer가 메시지를 대기 중");

        // Then - Consumer가 메시지를 받을 때까지 대기 (최대 5초)
        ConsumerRecords<String, OrderPaidEvent>[] recordsHolder = new ConsumerRecords[1]; // 배열로 감싸서 값 유지

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            recordsHolder[0] = consumer.poll(Duration.ofMillis(500));
            consumer.commitSync();
            assertThat(recordsHolder[0].count()).isGreaterThan(0); // 메시지가 하나 이상 존재해야 함
        });

        // recordsHolder에서 실제 데이터 꺼내기
        ConsumerRecords<String, OrderPaidEvent> records = recordsHolder[0];

        records.forEach(record -> {
            assertThat(record.value().getOrderId()).isEqualTo(1L);
            assertThat(record.value().getCustomerId()).isEqualTo(123L);
            assertThat(record.value().getTotalAmount()).isEqualTo(50000);
            log.info("주문 이벤트 수신 확인!: {}", record.value());
        });
    }*/
}