package kr.jsh.ecommerce.infrastructure.fruit;

import kr.jsh.ecommerce.domain.fruit.Fruit;
import kr.jsh.ecommerce.domain.fruit.FruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class FruitRepositoryImpl implements FruitRepository {

    private final FruitJpaRepository fruitJpaRepository;

    @Override
    public Optional<Fruit> findById(Long fruitId) {
        return fruitJpaRepository.findById(fruitId);
    }

    @Override
    public Optional<Fruit> findByIdForUpdate(Long fruitId) {
        return fruitJpaRepository.findByIdForUpdate(fruitId);
    }

    @Override
    public Fruit save(Fruit fruit) {
        return fruitJpaRepository.save(fruit);
    }

    @Override
    public Page<Fruit> findAll(Pageable pageable) {
        return fruitJpaRepository.findAll(pageable);
    }
}
