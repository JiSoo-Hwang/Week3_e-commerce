package kr.jsh.ecommerce.infrastructure.customer;

import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {
    private final CustomerJpaRepository customerJpaRepository;

    @Override
    public Optional<Customer> findById(Long id) {
        return customerJpaRepository.findById(id);
    }

    @Override
    public Customer save(Customer customer) {
        return customerJpaRepository.save(customer);
    }
}
