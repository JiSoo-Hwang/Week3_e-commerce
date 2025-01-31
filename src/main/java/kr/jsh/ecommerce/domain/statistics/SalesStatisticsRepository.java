package kr.jsh.ecommerce.domain.statistics;

import kr.jsh.ecommerce.interfaces.api.statistics.dto.ProductSalesDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface SalesStatisticsRepository {
    List<ProductSalesDTO> findTop5BestSellingProducts(LocalDateTime startDate);
}
