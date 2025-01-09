package kr.jsh.ecommerce.product.infrastructure;

import jakarta.persistence.EntityManager;
import kr.jsh.ecommerce.product.domain.Product;
import kr.jsh.ecommerce.product.domain.ProductRepository;
import kr.jsh.ecommerce.product.domain.Stock;
import kr.jsh.ecommerce.product.domain.StockRepository;
import org.springframework.stereotype.Repository;

import static kr.jsh.ecommerce.product.domain.QProduct.product;
import static kr.jsh.ecommerce.product.domain.QStock.stock;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final StockRepository stockRepository;

    public ProductRepositoryImpl(StockRepository stockRepository){
        this.stockRepository=stockRepository;
    }

    @Override
    public List<Product> getInStockProducts() {
        return stockRepository.getInStockProducts()
                .stream()
                .map(Stock::getProduct)
                .distinct()
                .toList();
    }
}
