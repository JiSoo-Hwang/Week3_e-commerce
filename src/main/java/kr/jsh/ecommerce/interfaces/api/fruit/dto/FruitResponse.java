package kr.jsh.ecommerce.interfaces.api.fruit.dto;

import kr.jsh.ecommerce.domain.fruit.Fruit;

public record FruitResponse(String fruitId, String fruitName, int fruitPrice, int stock) {
    public static FruitResponse from(Fruit fruit){
        return new FruitResponse(
          fruit.getFruitId().toString(),
          fruit.getFruitName(),
          fruit.getFruitPrice(),
          fruit.getFruitStock()
        );
    }
}
