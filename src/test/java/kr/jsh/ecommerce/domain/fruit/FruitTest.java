package kr.jsh.ecommerce.domain.fruit;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FruitTest {

    @Test
    void testDeductStock_Success() {
        // Given
        Fruit fruit = Fruit.builder()
                .fruitName("사과")
                .fruitStock(50)
                .fruitPrice(1000)
                .status("AVAILABLE")
                .build();

        // When
        fruit.deductStock(10);

        // Then
        assertEquals(40, fruit.getFruitStock());
    }

    @Test
    void testDeductStock_InsufficientStock() {
        // Given
        Fruit fruit = Fruit.builder()
                .fruitName("사과")
                .fruitStock(5)
                .fruitPrice(1000)
                .status("AVAILABLE")
                .build();

        // When & Then
        BaseCustomException exception = assertThrows(BaseCustomException.class, () -> fruit.deductStock(10));
        assertEquals(BaseErrorCode.OUT_OF_STOCK, exception.getBaseErrorCode());
        assertEquals("사과 의 재고가 부족합니다.", exception.formatMessage());
    }

    @Test
    void testRestoreStock_Success() {
        // Given
        Fruit fruit = Fruit.builder()
                .fruitName("사과")
                .fruitStock(20)
                .fruitPrice(1000)
                .status("AVAILABLE")
                .build();

        // When
        fruit.restoreStock(10);

        // Then
        assertEquals(30, fruit.getFruitStock());
    }

    @Test
    void testRestoreStock_NegativeQuantity() {
        // Given
        Fruit fruit = Fruit.builder()
                .fruitName("사과")
                .fruitStock(20)
                .fruitPrice(1000)
                .status("AVAILABLE")
                .build();

        // When & Then
        BaseCustomException exception = assertThrows(BaseCustomException.class, () -> fruit.restoreStock(-10));
        assertEquals(BaseErrorCode.INVALID_PARAMETER, exception.getBaseErrorCode());
        assertEquals("Invalid parameter : -10", exception.formatMessage());
    }
}
