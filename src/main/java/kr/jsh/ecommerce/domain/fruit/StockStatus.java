package kr.jsh.ecommerce.domain.fruit;

public enum StockStatus {
    IN_STOCK,       // ✅ 충분한 재고가 있음
    LOW_STOCK,      // ⚠️ 재고가 부족함 (예: 5개 이하)
    OUT_OF_STOCK    // ❌ 재고가 소진됨 (0개)
}
