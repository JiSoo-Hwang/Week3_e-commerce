package kr.jsh.ecommerce.infrastructure.statistics;

import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.fruit.Fruit;
import kr.jsh.ecommerce.domain.order.Order;
import kr.jsh.ecommerce.domain.order.OrderFruit;
import kr.jsh.ecommerce.domain.order.OrderStatus;
import kr.jsh.ecommerce.infrastructure.customer.CustomerJpaRepository;
import kr.jsh.ecommerce.infrastructure.fruit.FruitJpaRepository;
import kr.jsh.ecommerce.infrastructure.order.OrderJpaRepository;
import kr.jsh.ecommerce.interfaces.api.statistics.dto.ProductSalesDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
public class StatisticsJpaRepositoryTest {

    @Container
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.32")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private CustomerJpaRepository customerJpaRepository;

    @Autowired
    private FruitJpaRepository fruitJpaRepository;

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @Autowired
    private StatisticsJpaRepository statisticsJpaRepository;


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", mysql::getDriverClassName);
    }

    @BeforeEach
    void setUp() {
        // 고객 생성 및 저장
        Customer customer = Customer.create("김철수", "서울시 강남구", "010-1234-5678");
        Customer savedCustomer = customerJpaRepository.save(customer);

        // 과일 생성 및 저장
        Fruit apple = Fruit.create("사과", 3000, 50, "재고있음");
        Fruit banana = Fruit.create("바나나", 2000, 40, "재고있음");
        Fruit strawberry = Fruit.create("딸기", 5000, 30, "재고있음");
        Fruit mango = Fruit.create("망고", 6000, 20, "재고있음");
        Fruit cherry = Fruit.create("체리", 7000, 10, "재고있음");

        Fruit savedApple = fruitJpaRepository.save(apple);
        Fruit savedBanana = fruitJpaRepository.save(banana);
        Fruit savedStrawberry = fruitJpaRepository.save(strawberry);
        Fruit savedMango = fruitJpaRepository.save(mango);
        Fruit savedCherry = fruitJpaRepository.save(cherry);

        // 주문 생성 (이제 Order 자체는 통계를 내는 데 직접적으로 필요 없음)
        Order order1 = Order.builder()
                .customer(savedCustomer)
                .orderDate(LocalDateTime.now().minusDays(2)) // 최근 3일 내 주문
                .orderStatus(OrderStatus.PAID)
                .build();

        Order order2 = Order.builder()
                .customer(savedCustomer)
                .orderDate(LocalDateTime.now().minusDays(1)) // 최근 3일 내 주문
                .orderStatus(OrderStatus.PAID)
                .build();


        // OrderFruit 생성 및 저장 (통계를 내는 기준)
        OrderFruit orderFruit1 = new OrderFruit(order1, savedApple, savedApple.getFruitPrice(), 7);
        OrderFruit orderFruit2 = new OrderFruit(order1, savedBanana, savedBanana.getFruitPrice(), 2);
        OrderFruit orderFruit3 = new OrderFruit(order1, savedMango, savedMango.getFruitPrice(), 1);
        OrderFruit orderFruit4 = new OrderFruit(order2, savedApple, savedApple.getFruitPrice(), 4);
        OrderFruit orderFruit5 = new OrderFruit(order2, savedCherry, savedCherry.getFruitPrice(), 3);
        OrderFruit orderFruit6 = new OrderFruit(order2, savedStrawberry, savedStrawberry.getFruitPrice(), 10);

        order1.addOrderFruit(orderFruit1);
        order1.addOrderFruit(orderFruit2);
        order1.addOrderFruit(orderFruit3);

        order2.addOrderFruit(orderFruit4);
        order2.addOrderFruit(orderFruit5);
        order2.addOrderFruit(orderFruit6);

        order1.calculateTotalAmount();
        order2.calculateTotalAmount();

        orderJpaRepository.save(order1);
        orderJpaRepository.save(order2);
    }

    @Test
    @DisplayName("최근 3일간 가장 많이 팔린 상품 조회 테스트")
    void findTop5SellingProducts() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(3);
        Pageable top5 = PageRequest.of(0, 5);
        List<ProductSalesDTO> result = statisticsJpaRepository.findTop5BestSellingProducts(startDate);

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isLessThanOrEqualTo(5);

        // 가장 많이 팔린 상품이 "사과(7 + 4 = 11개)"인지 확인
        assertThat(result.get(0).getProductName()).isEqualTo("사과");
        assertThat(result.get(0).getTotalQuantity()).isEqualTo(11);

        // 두 번째로 많이 팔린 상품이 "딸기(10개)"인지 확인
        assertThat(result.get(1).getProductName()).isEqualTo("딸기");
        assertThat(result.get(1).getTotalQuantity()).isEqualTo(10);
    }
}