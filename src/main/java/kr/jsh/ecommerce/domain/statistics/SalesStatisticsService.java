package kr.jsh.ecommerce.domain.statistics;

import kr.jsh.ecommerce.interfaces.api.statistics.dto.ProductSalesDTO;
import kr.jsh.ecommerce.interfaces.api.statistics.dto.ProductSalesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SalesStatisticsService {
    private final SalesStatisticsRepository statisticsRepository;
    public List<ProductSalesResponse> getTop5SellingProducts(LocalDateTime startDate) {
        List<ProductSalesDTO> productsSalesData = statisticsRepository.findTop5BestSellingProducts(startDate);
        return productsSalesData.stream()
                .map(ProductSalesResponse::from)
                .collect(Collectors.toList());
    }
}
