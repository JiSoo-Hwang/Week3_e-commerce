package kr.jsh.ecommerce.infrastructure.fruit;

import kr.jsh.ecommerce.domain.fruit.Fruit;
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
import static org.mockito.Mockito.*;

class FruitRepositoryImplTest {

    @Mock
    private FruitJpaRepository fruitJpaRepository;

    @InjectMocks
    private FruitRepositoryImpl fruitRepository;

    FruitRepositoryImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Given
        Pageable pageable = PageRequest.of(0, 9);
        Fruit fruit1 = Fruit.builder()
                .fruitId(1L)
                .fruitName("망고스틴")
                .fruitStock(50)
                .fruitPrice(10000)
                .status("AVAILABLE")
                .build();
        Fruit fruit2 = Fruit.builder()
                .fruitId(2L)
                .fruitName("파인애플")
                .fruitStock(30)
                .fruitPrice(15000)
                .status("AVAILABLE")
                .build();
        Page<Fruit> fruitsPage = new PageImpl<>(List.of(fruit1, fruit2));
        when(fruitJpaRepository.findAll(pageable)).thenReturn(fruitsPage);

        // When
        Page<Fruit> result = fruitRepository.findAll(pageable);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getFruitName()).isEqualTo("망고스틴");
        assertThat(result.getContent().get(1).getFruitName()).isEqualTo("파인애플");
        verify(fruitJpaRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindById_found() {
        // Given
        Fruit fruit = Fruit.builder()
                .fruitId(1L)
                .fruitName("망고스틴")
                .fruitStock(50)
                .fruitPrice(10000)
                .status("AVAILABLE")
                .build();
        when(fruitJpaRepository.findById(1L)).thenReturn(Optional.of(fruit));

        // When
        Optional<Fruit> result = fruitRepository.findById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getFruitName()).isEqualTo("망고스틴");
        verify(fruitJpaRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_notFound() {
        // Given
        when(fruitJpaRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Fruit> result = fruitRepository.findById(1L);

        // Then
        assertThat(result).isEmpty();
        verify(fruitJpaRepository, times(1)).findById(1L);
    }
}

