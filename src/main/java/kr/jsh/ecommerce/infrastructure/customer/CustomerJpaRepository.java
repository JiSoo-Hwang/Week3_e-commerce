package kr.jsh.ecommerce.infrastructure.customer;

import kr.jsh.ecommerce.domain.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerJpaRepository extends JpaRepository<Customer,Long>{
}
