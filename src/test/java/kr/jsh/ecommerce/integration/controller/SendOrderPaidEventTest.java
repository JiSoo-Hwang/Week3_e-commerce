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
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Slf4j
public class SendOrderPaidEventTest {

    @Autowired
    private KafkaTemplate<String, OrderPaidEvent> kafkaTemplate;

    private Consumer<String, OrderPaidEvent> consumer;

    @BeforeEach
    public void setUp() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-1:9092,kafka-2:9093,kafka-3:9094");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("order-paid-topic"));
    }

    @AfterEach
    public void tearDown() {
        consumer.close();
    }

    @Test
    public void testKafkaMessageSendingAndReceiving() {
        // Given
        OrderPaidEvent event = new OrderPaidEvent(1L, 123L, 50000);

        // When - Producer가 메시지 전송
        kafkaTemplate.send("order-paid-topic", event);

        // Then - Consumer가 메시지 정상적으로 받았는지 검증
        ConsumerRecords<String, OrderPaidEvent> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(10));
        assertThat(records.count()).isGreaterThan(0);

        records.forEach(record -> {
            assertThat(record.value().getOrderId()).isEqualTo(1L);
            assertThat(record.value().getCustomerId()).isEqualTo(123L);
            assertThat(record.value().getTotalAmount()).isEqualTo(50000);
            log.info("주문 이벤트 수신!: {}", record.value());

        });
    }
}

