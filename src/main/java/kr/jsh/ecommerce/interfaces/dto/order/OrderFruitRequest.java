package kr.jsh.ecommerce.interfaces.dto.order;

public record OrderFruitRequest(
        String fruitId,
        String fruitName,
        int fruitPrice,
        int quantity
        ) {
}
