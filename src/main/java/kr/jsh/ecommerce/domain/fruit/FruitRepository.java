package kr.jsh.ecommerce.domain.fruit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FruitRepository {
    Page<Fruit> findAll(Pageable pageable);
    Optional<Fruit> findById(Long fruitId);
    Fruit save(Fruit fruit);
}
