package kr.jsh.ecommerce.infrastructure.fruit;

import jakarta.persistence.LockModeType;
import kr.jsh.ecommerce.domain.fruit.Fruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FruitJpaRepository extends JpaRepository<Fruit, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT f FROM Fruit f WHERE f.fruitId = :fruitId")
    Optional<Fruit> findByIdForUpdate(@Param("fruitId") Long fruitId);
}
