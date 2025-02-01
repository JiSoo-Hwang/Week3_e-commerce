package kr.jsh.ecommerce.infrastructure.statistics;

import kr.jsh.ecommerce.domain.statistics.SalesStatisticsRepository;
import kr.jsh.ecommerce.interfaces.api.statistics.dto.ProductSalesDTO;
import kr.jsh.ecommerce.interfaces.api.statistics.dto.ProductSalesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatisticsRepositoryImpl implements SalesStatisticsRepository {

    private StatisticsJpaRepository statisticsJpaRepository;

    @Override
    public List<ProductSalesDTO> findTop5BestSellingProducts(LocalDateTime startDate) {
        return statisticsJpaRepository.findTop5BestSellingProducts(startDate);
    }
}
