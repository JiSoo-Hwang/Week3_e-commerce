package kr.jsh.ecommerce.product.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kr.jsh.ecommerce.product.domain.Stock;
import kr.jsh.ecommerce.product.domain.StockRepository;

import static kr.jsh.ecommerce.product.domain.QProduct.product;
import static kr.jsh.ecommerce.product.domain.QStock.stock;

import java.util.List;

public class StockRepositoryImpl implements StockRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public StockRepositoryImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Stock> getInStockProducts() {
        return jpaQueryFactory
                .selectFrom(stock)
                .where(stock.productQuantity.gt(0))
                .fetch();
    }
}
