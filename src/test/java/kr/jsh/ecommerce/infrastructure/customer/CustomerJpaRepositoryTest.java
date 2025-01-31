package kr.jsh.ecommerce.infrastructure.customer;


import kr.jsh.ecommerce.domain.customer.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
public class CustomerJpaRepositoryTest {

    @Container
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.32")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private CustomerJpaRepository customerJpaRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url",mysql::getJdbcUrl);
        registry.add("spring.datasource.username",mysql::getUsername);
        registry.add("spring.datasource.password",mysql::getPassword);
        registry.add("spring.datasource.driver-class-name",mysql::getDriverClassName);
    }

    private Customer savedCustomer;

    @BeforeEach
    void setUp(){
        //Given
        Customer customer = Customer.create("임희은","서울시","01011112222");

        //When
        savedCustomer = customerJpaRepository.save(customer);
    }

    @AfterEach
    void tearDown(){
        customerJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("JPA를 이용한 고객 저장 및 조회")
    void saveAndFindCustomer(){
        //Given
        Long customerId = savedCustomer.getCustomerId();

        //When
        Optional<Customer> foundCustomer = customerJpaRepository.findById(customerId);

        //Then
        Assertions.assertThat(foundCustomer).isPresent();
        Assertions.assertThat(foundCustomer.get().getCustomerId()).isEqualTo(customerId);
    }
}
