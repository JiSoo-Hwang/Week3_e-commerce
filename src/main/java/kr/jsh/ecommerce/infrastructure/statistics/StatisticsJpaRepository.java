package kr.jsh.ecommerce.infrastructure.statistics;

import kr.jsh.ecommerce.domain.fruit.Fruit;
import kr.jsh.ecommerce.interfaces.api.statistics.dto.ProductSalesDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticsJpaRepository extends JpaRepository<Fruit,Long> {
    @Query(value = """
        SELECT new kr.jsh.ecommerce.interfaces.api.statistics.dto.ProductSalesDTO(
            of.fruit.fruitId,
            of.fruit.fruitName,
            SUM(of.quantity)
        )
        FROM OrderFruit of
        WHERE of.order.orderDate >= :startDate
        GROUP BY of.fruit.fruitId, of.fruit.fruitName
        ORDER BY SUM(of.quantity) DESC
    """)
    List<ProductSalesDTO> findTop5BestSellingProducts(@Param("startDate")LocalDateTime startDate);
}
