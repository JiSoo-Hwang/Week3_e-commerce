package kr.jsh.ecommerce.product.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.jsh.ecommerce.product.application.ProductService;
import kr.jsh.ecommerce.product.presentation.dto.ProductInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@Tag(name="Product API", description = "상품 관련 API")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/in-stock")
    @Operation(summary = "상품 목록 조회",description = "재고가 있는 상품 목록을 조회합니다.")
    public List<ProductInfoResponse> getInStockProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        return productService.getInStockProducts();
    }

    @GetMapping("/top-sold")
    public ResponseEntity<List<Map<String, Object>>> getTopSoldProducts(
            @RequestParam(defaultValue = "3") int days,
            @RequestParam(defaultValue = "5") int limit) {
        List<Map<String, Object>> mockResponse = List.of(
                Map.of("productId", 1, "productName", "상품명1", "totalQuantitySold", 70, "totalSalesAmount", 700000),
                Map.of("productId", 2, "productName", "상품명2", "totalQuantitySold", 60, "totalSalesAmount", 600000)
        );
        return ResponseEntity.ok(mockResponse);
    }
}

