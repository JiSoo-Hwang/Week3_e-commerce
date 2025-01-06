package kr.jsh.ecommerce.presentation.api;

import kr.jsh.ecommerce.product.application.ProductService;
import kr.jsh.ecommerce.product.domain.ProductStatus;
import kr.jsh.ecommerce.product.presentation.controller.ProductController;
import kr.jsh.ecommerce.product.presentation.dto.ProductInfoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Test
    void getInStockProducts_ShouldReturnProductList() throws Exception {
        // Given
        List<ProductInfoResponse> mockProducts = List.of(
                new ProductInfoResponse(1L, "Product1", "Category1", 10, 100.0, ProductStatus.IN_STOCK),
                new ProductInfoResponse(2L, "Product2", "Category2", 5, 50.0, ProductStatus.IN_STOCK)
        );
        Mockito.when(productService.getInStockProducts()).thenReturn(mockProducts);

        // When
        List<ProductInfoResponse> result = productController.getInStockProducts(0, 9);

        // Then
        assertEquals(2, result.size());
        assertEquals("Product1", result.get(0).productName());
        assertEquals("Product2", result.get(1).productName());
    }
}