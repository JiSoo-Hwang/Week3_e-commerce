package kr.jsh.ecommerce.domain.fruit;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Service
public class FruitService {

    private final FruitRepository fruitRepository;

    public Page<Fruit> getFruits(Pageable pageable){
        return fruitRepository.findAll(pageable);
    }

    public Fruit getFruitById(Long fruitId){
        return fruitRepository.findById(fruitId)
                .orElseThrow(()->new BaseCustomException(BaseErrorCode.NOT_FOUND,new String[]{String.valueOf(fruitId)}));
    }

    public Fruit save(Fruit fruit){return fruitRepository.save(fruit);}
}
