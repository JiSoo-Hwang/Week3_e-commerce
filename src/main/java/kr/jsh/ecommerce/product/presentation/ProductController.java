package kr.jsh.ecommerce.product.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getProductList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        List<Map<String, Object>> products = List.of(
                Map.of("productId", 1, "productName", "상품명1", "productPrice", 10000, "stockQuantity", 50, "isPublished", true)
        );
        return ResponseEntity.ok(products);
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

