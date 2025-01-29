package kr.jsh.ecommerce.interfaces.fruit;

import kr.jsh.ecommerce.domain.fruit.Fruit;
import kr.jsh.ecommerce.infrastructure.fruit.FruitJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FruitControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FruitJpaRepository fruitJpaRepository;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();

        // 테스트 데이터 초기화
        fruitJpaRepository.deleteAll(); // 기존 데이터 삭제

        Fruit fruit1 = Fruit.builder()
                .fruitName("사과")
                .fruitStock(50)
                .fruitPrice(1000)
                .status("AVAILABLE")
                .build();
        Fruit fruit2 = Fruit.builder()
                .fruitName("바나나")
                .fruitStock(30)
                .fruitPrice(1500)
                .status("AVAILABLE")
                .build();

        fruitJpaRepository.saveAll(List.of(fruit1, fruit2));
    }

    @Test
    void testGetFruits() {
        // Given
        String url = "http://localhost:" + port + "/api/items?page=0&size=9";

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("사과", "바나나");
    }

    @Test
    void testFindFruitById() {
        // Given
        Long fruitId = fruitJpaRepository.findAll().get(0).getFruitId();
        String url = "http://localhost:" + port + "/api/items/" + fruitId;

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("사과");
    }
}

