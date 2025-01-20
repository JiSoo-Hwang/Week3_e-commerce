package kr.jsh.ecommerce.infrastructure.order;

import kr.jsh.ecommerce.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
