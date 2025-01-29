package kr.jsh.ecommerce.infrastructure.order;

import kr.jsh.ecommerce.domain.fruit.Fruit;
import kr.jsh.ecommerce.domain.order.Order;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.order.OrderFruit;
import kr.jsh.ecommerce.domain.order.OrderStatus;
import kr.jsh.ecommerce.infrastructure.customer.CustomerJpaRepository;
import kr.jsh.ecommerce.infrastructure.fruit.FruitJpaRepository;
import org.assertj.core.api.Assertions;
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

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Order Repository 및 관련있는 Repository 통합 테스트")
public class OrderJpaRepositoryIntegrationTest {

    @Container
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.32")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private CustomerJpaRepository customerJpaRepository;

    @Autowired
    private FruitJpaRepository fruitJpaRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url",mysql::getJdbcUrl);
        registry.add("spring.datasource.username",mysql::getUsername);
        registry.add("spring.datasource.password",mysql::getPassword);
        registry.add("spring.datasource.driver-class-name",mysql::getDriverClassName);
    }

    private Order savedOrder;
    private Customer savedCustomer;
    private Fruit savedFruit;

    @BeforeEach
    void setUp(){
        Customer customer = Customer.create("김경덕","서울시","01033334444");
        savedCustomer = customerJpaRepository.save(customer);

        Fruit strawberry = Fruit.create("딸기",5000,10,"재고있음");
        strawberry.deductStock(2);
        savedFruit = fruitJpaRepository.save(strawberry);

        Order order = Order.builder()
                .customer(savedCustomer)
                .orderDate(LocalDateTime.of(2025,01,29,21,17))
                .orderStatus(OrderStatus.PAID)
                .build();

        OrderFruit orderFruit = new OrderFruit(
                order,
                savedFruit,
                savedFruit.getFruitPrice(),
                2
        );
        order.addOrderFruit(orderFruit);
        order.calculateTotalAmount();
        savedOrder = orderJpaRepository.save(order);
    }

    @AfterEach
    void tearDown(){
        orderJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("JPA를 이용한 주문 저장 및 조회 테스트")
    void saveAndFindOrder(){
        //When
        Optional<Order> foundOrder = orderJpaRepository.findById(savedOrder.getOrderId());

        //Then
        Assertions.assertThat(foundOrder).isPresent();
        Assertions.assertThat(foundOrder.get().getTotalAmount()).isEqualTo(10000);
        Assertions.assertThat(foundOrder.get().getOrderFruits().size()).isEqualTo(1);
        Assertions.assertThat(foundOrder.get().getTotalAmount()).isEqualTo(10000);
    }
}
