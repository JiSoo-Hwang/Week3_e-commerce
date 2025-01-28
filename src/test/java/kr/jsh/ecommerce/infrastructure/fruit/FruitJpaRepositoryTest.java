package kr.jsh.ecommerce.infrastructure.fruit;

import kr.jsh.ecommerce.domain.fruit.Fruit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
class FruitJpaRepositoryTest {

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.32")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private FruitJpaRepository fruitJpaRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
    }

    @BeforeEach
    void setUp() {
        // 테스트 데이터를 미리 저장
        Fruit fruit = Fruit.builder()
                .fruitName("사과")
                .fruitStock(100)
                .fruitPrice(5000)
                .status("AVAILABLE")
                .build();
        fruitJpaRepository.save(fruit);
    }

    @AfterEach
    void tearDown() {
        // 데이터 정리
        fruitJpaRepository.deleteAll();
    }

    @Test
    void findByName_shouldReturnFruit() {
        // given
        String fruitName = "사과";

        // when
        Optional<Fruit> fruit = fruitJpaRepository.findByfruitName(fruitName);

        // then
        assertThat(fruit).isPresent();
        assertThat(fruit.get().getFruitName()).isEqualTo(fruitName);
    }
}