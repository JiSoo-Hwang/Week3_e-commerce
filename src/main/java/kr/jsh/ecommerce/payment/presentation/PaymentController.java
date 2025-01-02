package kr.jsh.ecommerce.payment.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @PostMapping
    public ResponseEntity<Map<String, Object>> payForOrder(@RequestBody Map<String, Object> request) {
        Map<String, Object> payment = Map.of(
                "paymentId", 5001,
                "purchaseId", request.get("purchaseId"),
                "amount", 30000,
                "paidAt", "2025-01-01T13:00:00",
                "status", "SUCCESS"
        );
        return ResponseEntity.ok(payment);
    }
}

