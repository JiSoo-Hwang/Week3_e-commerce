package kr.jsh.ecommerce.product.application;

import kr.jsh.ecommerce.product.domain.OperationType;
import kr.jsh.ecommerce.product.domain.Stock;
import kr.jsh.ecommerce.product.domain.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Stock findAndValidateStock(Long productId, int productSize, int productQuantity) {
        Stock stock = stockRepository.findStockForUpdate(productId, productSize)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품 재고가 없습니다"));
        if (stock.getProductQuantity() < productQuantity) {
            throw new IllegalArgumentException("재고가 부족합니다");
        }
        return stock;
    }

    @Transactional
    public void deductStock(Stock stock, int productQuantity) {
        stock.updateQuantity(productQuantity, OperationType.REMOVE);
    }
}
