package kr.jsh.ecommerce.application.fruit;

import kr.jsh.ecommerce.domain.fruit.Fruit;
import kr.jsh.ecommerce.domain.fruit.FruitService;
import kr.jsh.ecommerce.interfaces.api.fruit.dto.FruitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FindFruitByIdUseCase {

    private final FruitService fruitService;

    public FruitResponse findFruitById(Long fruitId) {
        Fruit fruit = fruitService.getFruitById(fruitId);
        return FruitResponse.from(fruit);
    }
}
