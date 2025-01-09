package kr.jsh.ecommerce.config.jpa;

import jakarta.persistence.EntityManager;
import kr.jsh.ecommerce.product.domain.ProductRepository;
import kr.jsh.ecommerce.product.domain.StockRepository;
import kr.jsh.ecommerce.product.infrastructure.ProductRepositoryImpl;
import kr.jsh.ecommerce.product.infrastructure.StockRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public ProductRepository productRepository(StockRepository stockRepository){
        return new ProductRepositoryImpl(stockRepository);
    }

    @Bean
    public StockRepository stockRepository(EntityManager entityManager){
        return new StockRepositoryImpl(entityManager);
    }
}
