package kr.jsh.ecommerce.domain.fruit;

import kr.jsh.ecommerce.infrastructure.fruit.FruitRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FruitServiceTest {

    @Mock
    private FruitRepositoryImpl fruitRepositoryImpl;

    @InjectMocks
    private FruitService fruitService;

    FruitServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFruits() {
        // Given
        Pageable pageable = PageRequest.of(0, 9);
        Fruit fruit1 = Fruit.builder()
                .fruitId(1L)
                .fruitName("딸기")
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
        when(fruitRepositoryImpl.findAll(pageable)).thenReturn(fruitsPage);

        // When
        Page<Fruit> result = fruitService.getFruits(pageable);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getFruitName()).isEqualTo("딸기");
        assertThat(result.getContent().get(1).getFruitName()).isEqualTo("바나나");
        verify(fruitRepositoryImpl, times(1)).findAll(pageable);
    }

    @Test
    void testGetFruitById_found() {
        // Given
        Fruit fruit = Fruit.builder()
                .fruitId(1L)
                .fruitName("딸기")
                .fruitStock(50)
                .fruitPrice(1000)
                .status("재고있음")
                .build();
        when(fruitRepositoryImpl.findById(1L)).thenReturn(Optional.of(fruit));

        // When
        Fruit result = fruitService.getFruitById(1L);

        // Then
        assertThat(result.getFruitName()).isEqualTo("딸기");
        verify(fruitRepositoryImpl, times(1)).findById(1L);
    }

    @Test
    void testGetFruitById_notFound() {
        // Given
        when(fruitRepositoryImpl.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(RuntimeException.class, () -> fruitService.getFruitById(1L));
        verify(fruitRepositoryImpl, times(1)).findById(1L);
    }
}
