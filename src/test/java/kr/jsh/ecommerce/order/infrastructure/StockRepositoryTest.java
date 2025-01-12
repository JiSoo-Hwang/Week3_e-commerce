package kr.jsh.ecommerce.order.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.jsh.ecommerce.product.domain.*;
import kr.jsh.ecommerce.product.infrastructure.StockRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(StockRepositoryImpl.class)
@Transactional
public class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Product testProduct;

    @BeforeEach
    void setup() {
        // 상품 엔터티 생성
        testProduct = new Product("아디오스프로4", 299000, "아디다스");
        entityManager.persist(testProduct);

        // 재고 엔터티 생성 및 영속화
        ProductSize size = new ProductSize(235);
        Stock stock = new Stock(testProduct, size, 5, ProductStatus.IN_STOCK);
        entityManager.persist(stock);

        entityManager.flush(); // 변경 사항을 DB에 반영
        entityManager.clear(); // 영속성 컨텍스트 초기화
    }

    @Test
    @DisplayName("특정 상품 사이즈의 재고 검색")
    void testFindStockForUpdate() {
        // When: 재고 검색
        Optional<Stock> result = stockRepository.findStockForUpdate(testProduct.getProductId(), 235);

        // Then: 검증
        Assertions.assertTrue(result.isPresent());
    }
}

