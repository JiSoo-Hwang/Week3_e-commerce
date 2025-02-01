package kr.jsh.ecommerce.interfaces.api.statistics.controller;

import io.swagger.v3.oas.annotations.Operation;
import kr.jsh.ecommerce.domain.statistics.SalesStatisticsService;
import kr.jsh.ecommerce.interfaces.api.statistics.dto.ProductSalesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    private final SalesStatisticsService statisticsService;

    @Operation(summary = "최근 3일간 가장 많이 팔린 상위 5개 상품 정보 조회")
    @GetMapping("/top-products")
    public ResponseEntity<List<ProductSalesResponse>> getTopSellingProducts(){
        LocalDateTime startDate = LocalDateTime.now().minusDays(3);
        List<ProductSalesResponse> topProducts = statisticsService.getTop5SellingProducts(startDate);
        return ResponseEntity.ok(topProducts);
    }
}
