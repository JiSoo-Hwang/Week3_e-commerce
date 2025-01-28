package kr.jsh.ecommerce.domain.fruit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FruitRepository {
    Optional<Fruit> findById(Long fruitId);

    Optional<Fruit> findByIdForUpdate(Long fruitId); // 비관적 락을 사용하는 메서드

    Fruit save(Fruit fruit);

    Page<Fruit> findAll(Pageable pageable);
}
