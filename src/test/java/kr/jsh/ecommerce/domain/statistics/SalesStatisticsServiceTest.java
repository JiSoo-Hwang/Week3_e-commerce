package kr.jsh.ecommerce.domain.statistics;

import kr.jsh.ecommerce.interfaces.api.statistics.dto.ProductSalesDTO;
import kr.jsh.ecommerce.interfaces.api.statistics.dto.ProductSalesResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SalesStatisticsServiceTest {

    @Mock
    private SalesStatisticsRepository statisticsRepository;

    @InjectMocks
    private SalesStatisticsService salesStatisticsService;

    @Test
    @DisplayName("최근 3일간 가장 많이 팔린 상품 5개 상품 조회")
    void getTop5SellingProducts() {
        //Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(3);
        List<ProductSalesDTO> mockData = List.of(
                new ProductSalesDTO(1L, "사과", 50),
                new ProductSalesDTO(2L, "바나나", 40),
                new ProductSalesDTO(3L, "딸기", 30),
                new ProductSalesDTO(4L, "망고스틴", 20),
                new ProductSalesDTO(5L, "무화과", 10)
        );
        when(statisticsRepository.findTop5BestSellingProducts(startDate)).thenReturn(mockData);

        //When
        List<ProductSalesResponse> result = salesStatisticsService.getTop5SellingProducts(startDate);

        //Then
        assertThat(result).hasSize(5);
        assertThat(result.get(0).fruitName()).isEqualTo("사과");
        assertThat(result.get(0).totalQuantity()).isEqualTo(50);
        assertThat(result.get(1).fruitName()).isEqualTo("바나나");
        assertThat(result.get(1).totalQuantity()).isEqualTo(40);
        assertThat(result.get(2).fruitName()).isEqualTo("딸기");
        assertThat(result.get(2).totalQuantity()).isEqualTo(30);
        assertThat(result.get(3).fruitName()).isEqualTo("망고스틴");
        assertThat(result.get(3).totalQuantity()).isEqualTo(20);
        assertThat(result.get(4).fruitName()).isEqualTo("무화과");
        assertThat(result.get(4).totalQuantity()).isEqualTo(10);

        verify(statisticsRepository,times(1)).findTop5BestSellingProducts(startDate);
    }
}
