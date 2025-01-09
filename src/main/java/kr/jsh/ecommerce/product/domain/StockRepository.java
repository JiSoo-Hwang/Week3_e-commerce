package kr.jsh.ecommerce.product.domain;

import java.util.List;

public interface StockRepository {
    List<Stock> getInStockProducts();
}
