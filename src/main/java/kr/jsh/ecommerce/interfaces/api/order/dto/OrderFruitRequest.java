package kr.jsh.ecommerce.interfaces.api.order.dto;

public record OrderFruitRequest(
        String fruitId,
        int fruitPrice,
        int quantity
        ) {
}
