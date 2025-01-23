package kr.jsh.ecommerce.interfaces.api.order.dto;

public record OrderFruitRequest(
        String fruitId,
        String fruitName,
        int fruitPrice,
        int quantity
        ) {
}
