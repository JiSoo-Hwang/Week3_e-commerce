package kr.jsh.ecommerce.product.infrastructure;

import jakarta.persistence.EntityManager;
import kr.jsh.ecommerce.config.jpa.RepositoryConfig;
import kr.jsh.ecommerce.product.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Import(RepositoryConfig.class)
public class ProductRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private ProductRepositoryImpl productRepository;

    @BeforeEach
    void setUp(){
        Product product1 = new Product("아디다스아디오스프로4",299000,"아디다스");
        Product product2 = new Product("나이키알파플라이3",329000,"나이키");

        ProductSize productSize3 = new ProductSize(230);
        ProductSize productSize1 = new ProductSize(235);
        ProductSize productSize2 = new ProductSize(240);

        Stock stock1 = new Stock(product1,productSize1,5, ProductStatus.IN_STOCK);
        Stock stock3 = new Stock(product1,productSize3,0, ProductStatus.OUT_OF_STOCK);
        Stock stock2 = new Stock(product2,productSize2,3, ProductStatus.IN_STOCK);

        product1.addStock(stock1);
        product1.addStock(stock3);
        product2.addStock(stock2);

        em.persist(product1);
        em.persist(product2);

        em.flush();
    }

    @Test
    @DisplayName("재고가 있는 상품 조회")
    void testGetInStockProducts(){
        //When: 재고가 있는 상품 조회
        List<Product> inStockProducts = productRepository.getInStockProducts();

        //Then: 검증
        assertFalse(inStockProducts.isEmpty());
        assertEquals(2,inStockProducts.size());
        assertEquals("아디다스아디오스프로4",inStockProducts.get(0).getProductName());
        assertEquals("나이키알파플라이3",inStockProducts.get(1).getProductName());
    }
}
