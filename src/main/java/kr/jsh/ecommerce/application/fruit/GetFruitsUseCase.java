package kr.jsh.ecommerce.application.fruit;

import kr.jsh.ecommerce.domain.fruit.FruitService;

import kr.jsh.ecommerce.interfaces.dto.fruit.FruitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class GetFruitsUseCase {

    private final FruitService fruitService;

    public Page<FruitResponse> getFruits(Pageable pageable){
        return fruitService.getFruits(pageable).map(FruitResponse::from);
    }
}
