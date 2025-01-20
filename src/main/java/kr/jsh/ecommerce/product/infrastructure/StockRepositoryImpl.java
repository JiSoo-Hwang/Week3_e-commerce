package kr.jsh.ecommerce.product.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import kr.jsh.ecommerce.product.domain.Stock;
import kr.jsh.ecommerce.product.domain.StockRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public class StockRepositoryImpl implements StockRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private  final EntityManager entityManager;

    public StockRepositoryImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
        this.entityManager = em;
    }

    @Override
    public List<Stock> getInStockProducts() {
        return  null; /*jpaQueryFactory
                .selectFrom(stock)
                .where(stock.productQuantity.gt(0))
                .fetch();*/
    }

    public Optional<Stock> findStockForUpdate(Long productId,int productSize){
        return Optional.ofNullable(
                entityManager.createQuery(
                                "SELECT s FROM Stock s WHERE s.product.productId = :productId AND s.productSize.productSize = :productSize",
                                Stock.class
                        )
                        .setParameter("productId", productId)
                        .setParameter("productSize", productSize)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE) // Pessimistic Lock 적용
                        .getSingleResult()
        );
    }
}
