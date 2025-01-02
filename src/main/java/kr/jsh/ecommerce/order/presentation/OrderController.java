package kr.jsh.ecommerce.order.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = Map.of(
                "orderId", 1001,
                "customerId", request.get("customerId"),
                "orderDate", "2025-01-01T12:00:00",
                "orderSheets", List.of(
                        Map.of("productId", 1, "productName", "상품명1", "quantity", 2, "productPrice", 10000, "subTotal", 20000),
                        Map.of("productId", 2, "productName", "상품명2", "quantity", 1, "productPrice", 15000, "subTotal", 15000)
                ),
                "totalAmount", 35000
        );
        return ResponseEntity.ok(response);
    }
}
