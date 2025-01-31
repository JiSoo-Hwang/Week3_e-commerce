package kr.jsh.ecommerce.infrastructure.payment;

import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.order.Order;
import kr.jsh.ecommerce.domain.order.OrderStatus;
import kr.jsh.ecommerce.domain.payment.Payment;
import kr.jsh.ecommerce.domain.payment.PaymentStatus;
import kr.jsh.ecommerce.infrastructure.customer.CustomerJpaRepository;
import kr.jsh.ecommerce.infrastructure.order.OrderJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
public class PaymentJpaRepositoryTest {

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.32")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private CustomerJpaRepository customerJpaRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
    }

    private Order savedOrder;
    private Customer savedCustomer;

    @BeforeEach
    void setUp() {
        // 고객 정보 저장
        Customer customer = Customer.create("황지수", "서울시", "01012345678");
        savedCustomer = customerJpaRepository.save(customer);

        // 주문 정보 저장
        Order order = Order.builder()
                .customer(savedCustomer)
                .totalAmount(20000)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING)
                .build();
        savedOrder = orderJpaRepository.save(order);
    }

    @AfterEach
    void tearDown() {
        paymentJpaRepository.deleteAll();
        orderJpaRepository.deleteAll();
        customerJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("결제 정보를 저장하면 ID가 정상적으로 생성된다")
    void savePayment_shouldGenerateId() {
        // Given
        Payment payment = Payment.create(savedOrder, 20000); // 20,000원 결제

        // When
        Payment savedPayment = paymentJpaRepository.save(payment);

        // Then
        assertThat(savedPayment).isNotNull();
        assertThat(savedPayment.getPaymentId()).isNotNull(); // ID가 자동 생성되는지 확인
        assertThat(savedPayment.getAmount()).isEqualTo(20000);
        assertThat(savedPayment.getPaymentStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    @DisplayName("저장된 결제 정보를 ID로 조회할 수 있어야 한다")
    void findById_shouldReturnPayment() {
        // Given
        Payment payment = Payment.create(savedOrder, 20000);
        Payment savedPayment = paymentJpaRepository.save(payment);

        // When
        Optional<Payment> foundPayment = paymentJpaRepository.findById(savedPayment.getPaymentId());

        // Then
        assertThat(foundPayment).isPresent();
        assertThat(foundPayment.get().getPaymentId()).isEqualTo(savedPayment.getPaymentId());
        assertThat(foundPayment.get().getAmount()).isEqualTo(20000);
        assertThat(foundPayment.get().getPaymentStatus()).isEqualTo(PaymentStatus.PENDING);
    }
}

