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
    private Fruit savedFruit1;
    private Fruit savedFruit2;
    private Fruit savedFruit3;

    @BeforeEach
    void setUp(){
        Customer customer = Customer.create("박혜림","서울시","01033334444");
        savedCustomer = customerJpaRepository.save(customer);

        Fruit hanrabong = Fruit.create("한라봉",5000,100,"재고있음");
        Fruit tangerine = Fruit.create("귤",300,150,"재고있음");
        Fruit cheonhyehyang = Fruit.create("천혜향",2000,120,"재고있음");
        hanrabong.deductStock(20);
        tangerine.deductStock(80);
        cheonhyehyang.deductStock(30);
        savedFruit1 = fruitJpaRepository.save(hanrabong);
        savedFruit2 = fruitJpaRepository.save(tangerine);
        savedFruit3 = fruitJpaRepository.save(cheonhyehyang);

        Order order = Order.builder()
                .customer(savedCustomer)
                .orderDate(LocalDateTime.of(2025,01,29,21,17))
                .orderStatus(OrderStatus.PAID)
                .build();

        OrderFruit orderFruit1 = new OrderFruit(
                order,
                savedFruit1,
                savedFruit1.getFruitPrice(),
                20
        );
        OrderFruit orderFruit2 = new OrderFruit(
                order,
                savedFruit2,
                savedFruit2.getFruitPrice(),
                80
        );
        OrderFruit orderFruit3 = new OrderFruit(
                order,
                savedFruit3,
                savedFruit3.getFruitPrice(),
                30
        );
        order.addOrderFruit(orderFruit1);
        order.addOrderFruit(orderFruit2);
        order.addOrderFruit(orderFruit3);
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
        Assertions.assertThat(foundOrder.get().getTotalAmount()).isEqualTo(184000);
        Assertions.assertThat(foundOrder.get().getOrderFruits().size()).isEqualTo(3);
    }
}
