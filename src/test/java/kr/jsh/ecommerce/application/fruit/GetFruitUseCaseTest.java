package kr.jsh.ecommerce.application.fruit;

import kr.jsh.ecommerce.domain.fruit.Fruit;
import kr.jsh.ecommerce.domain.fruit.FruitService;
import kr.jsh.ecommerce.interfaces.api.fruit.dto.FruitResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;

class GetFruitsUseCaseTest {

    @Mock
    private FruitService fruitService;

    @InjectMocks
    private GetFruitsUseCase getFruitsUseCase;

    GetFruitsUseCaseTest() {
        MockitoAnnotations.openMocks(this); // @InjectMocks와 @Mock 초기화
    }

    @Test
    void testGetFruits() {
        // Given
        Pageable pageable = PageRequest.of(0, 9);
        Fruit fruit1 = Fruit.builder()
                .fruitId(1L)
                .fruitName("사과")
                .fruitStock(50)
                .fruitPrice(1000)
                .status("재고있음")
                .build();
        Fruit fruit2 = Fruit.builder()
                .fruitId(2L)
                .fruitName("바나나")
                .fruitStock(30)
                .fruitPrice(1500)
                .status("재고있음")
                .build();
        Page<Fruit> fruitsPage = new PageImpl<>(List.of(fruit1, fruit2));
        when(fruitService.getFruits(pageable)).thenReturn(fruitsPage);

        // When
        Page<FruitResponse> result = getFruitsUseCase.getFruits(pageable);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).fruitName()).isEqualTo("사과");
        assertThat(result.getContent().get(1).fruitName()).isEqualTo("바나나");
        verify(fruitService, times(1)).getFruits(pageable);
    }
}
