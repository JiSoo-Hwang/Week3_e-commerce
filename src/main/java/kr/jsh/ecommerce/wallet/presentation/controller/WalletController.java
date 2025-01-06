package kr.jsh.ecommerce.wallet.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    @GetMapping("/{customerId}")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable Long customerId) {
        Map<String, Object> wallet = Map.of(
                "walletId", 1,
                "customerId", customerId,
                "balance", 100000
        );
        return ResponseEntity.ok(wallet);
    }

    @PostMapping("/{customerId}/charge")
    public ResponseEntity<Map<String, Object>> chargeCash(
            @PathVariable Long customerId,
            @RequestBody Map<String, Object> request) {
        Double chargeAmount = (Double) request.get("chargeAmount");
        Map<String, Object> wallet = Map.of(
                "walletId", 1,
                "customerId", customerId,
                "balance", 150000 // 기존 잔액 + chargeAmount (Mock 데이터)
        );
        return ResponseEntity.ok(wallet);
    }

}

