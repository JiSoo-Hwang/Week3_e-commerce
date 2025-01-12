package kr.jsh.ecommerce.interfaces.dto.fruit;

import kr.jsh.ecommerce.domain.fruit.Fruit;

public record FruitResponse(String fruitId, String fruitName, int fruitPrice, int stock) {
    public static FruitResponse from(Fruit fruit){
        return new FruitResponse(
          fruit.getId().toString(),
          fruit.getFruitName(),
          fruit.getFruitPrice(),
          fruit.getFruitStock()
        );
    }
}
