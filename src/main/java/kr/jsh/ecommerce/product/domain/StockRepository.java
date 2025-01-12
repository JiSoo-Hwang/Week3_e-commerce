package kr.jsh.ecommerce.product.domain;

import java.util.List;
import java.util.Optional;

public interface StockRepository {
    List<Stock> getInStockProducts();
    Optional<Stock> findStockForUpdate(Long productId, int productSize);
}
