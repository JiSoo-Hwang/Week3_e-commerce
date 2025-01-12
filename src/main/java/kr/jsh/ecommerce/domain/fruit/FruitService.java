package kr.jsh.ecommerce.domain.fruit;

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
}
