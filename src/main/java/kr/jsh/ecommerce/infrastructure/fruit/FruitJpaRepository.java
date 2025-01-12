package kr.jsh.ecommerce.infrastructure.fruit;

import kr.jsh.ecommerce.domain.fruit.Fruit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FruitJpaRepository extends JpaRepository<Fruit,Long> {//TODO 패키지 다른 곳에 둘 것
}
