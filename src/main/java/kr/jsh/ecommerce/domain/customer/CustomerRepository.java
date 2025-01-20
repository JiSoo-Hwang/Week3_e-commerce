package kr.jsh.ecommerce.domain.customer;

import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findById(Long id);
    Customer save(Customer customer);
}
