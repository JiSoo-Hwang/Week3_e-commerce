package kr.jsh.ecommerce.integration.controller;

import kr.jsh.ecommerce.event.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.junit.jupiter.api.*;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Slf4j
@TestPropertySource(properties = "spring.kafka.bootstrap-servers=localhost:10002,localhost:10003,localhost:10004")
public class SendOrderPaidEventTest {

    @Autowired
    private KafkaTemplate<String, OrderPaidEvent> kafkaTemplate;

    private Consumer<String, OrderPaidEvent> consumer;

    @BeforeEach
    public void setUp() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:10002,localhost:10003,localhost:10004");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("order-paid-topic"));

        consumer.poll(Duration.ofMillis(100)); // 초기 poll
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

        // Then - Consumer가 메시지를 받을 때까지 대기 (최대 5초)
        ConsumerRecords<String, OrderPaidEvent>[] recordsHolder = new ConsumerRecords[1]; // 배열로 감싸서 값 유지

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            recordsHolder[0] = consumer.poll(Duration.ofMillis(500));
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
    }
}