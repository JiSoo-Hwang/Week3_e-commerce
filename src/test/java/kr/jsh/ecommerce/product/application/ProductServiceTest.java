package kr.jsh.ecommerce.product.application;

import kr.jsh.ecommerce.product.domain.Product;
import kr.jsh.ecommerce.product.domain.ProductRepository;
import kr.jsh.ecommerce.product.presentation.dto.ProductListInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
        productService = new ProductService(productRepository);

        // 테스트용 상품 생성
        Product product1 = new Product("아디다스아디오스프로4", 299000, "아디다스");
        Product product2 = new Product("나이키알파플라이3", 329000, "나이키");

        // Reflection으로 productId 설정
        setId(product1, "productId", 1L);
        setId(product2, "productId", 2L);

        // Mock Repository 설정
        when(productRepository.getInStockProducts()).thenReturn(List.of(product1, product2));
    }

    private void setId(Object entity, String fieldName, Long value) throws Exception {
        Field field = entity.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(entity, value);
    }

    @Test
    @DisplayName("재고가 있는 상품 조회")
    void testGetInStockProducts() {
        // When: 재고가 있는 상품 조회
        List<ProductListInfoResponse> inStockProducts = productService.getInStockProducts();

        // Then: 검증
        assertNotNull(inStockProducts);
        assertEquals(2, inStockProducts.size());

        assertEquals(1L, inStockProducts.get(0).productId());
        assertEquals("아디다스아디오스프로4", inStockProducts.get(0).productName());

        assertEquals(2L, inStockProducts.get(1).productId());
        assertEquals("나이키알파플라이3", inStockProducts.get(1).productName());
    }
}
