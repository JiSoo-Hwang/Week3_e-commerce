package kr.jsh.ecommerce.infrastructure.wallet;

import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.wallet.Wallet;
import kr.jsh.ecommerce.infrastructure.customer.CustomerJpaRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
public class WalletJpaRepositoryTest {

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.32")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private WalletJpaRepository walletJpaRepository;

    @Autowired
    private CustomerJpaRepository customerJpaRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
    }

    private Customer customer;
    private Customer savedCustomer;

    @BeforeEach
    void setUp(){
        //테스트 데이터를 미리 저장
        customer = Customer.create("황지수","서울시","01055557777");
        savedCustomer = customerJpaRepository.save(customer);

        Wallet wallet = Wallet.builder()
                .balance(10000000)
                .customer(customer)
                .build();
        walletJpaRepository.save(wallet);
    }

    @AfterEach
    void tearDown(){
        //데이터정리
        walletJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("저장한 지갑이 반환되어야 한다")
    void findByCustomerId_shoudReturnWallet(){
        //given
        Long customerId = savedCustomer.getCustomerId();

        //when
        Optional<Wallet> wallet = walletJpaRepository.findByCustomerId(customerId);

        //then
        assertThat(wallet).isPresent();
        assertThat(wallet.get().getCustomer().getCustomerName()).isEqualTo("황지수");
        assertThat(wallet.get().getCustomer().getCustomerAddress()).isEqualTo("서울시");
        assertThat(wallet.get().getCustomer().getCustomerPhone()).isEqualTo("01055557777");
        assertThat(wallet.get().getBalance()).isEqualTo(10000000);

    }

    @Test
    @DisplayName("존재하지 않는 고객 ID로 Wallet을 조회하면 Optional.empty()를 반환해야 한다")
    void findByCustomerId_shouldReturnEmpty() {
        // Given
        Long nonExistingCustomerId = 9999L; // 존재하지 않는 고객 ID

        // When
        Optional<Wallet> wallet = walletJpaRepository.findByCustomerId(nonExistingCustomerId);

        // Then
        assertThat(wallet).isEmpty(); // Optional.empty()가 반환되어야 함
    }

    @Test
    @DisplayName("Wallet의 balance를 변경하면 업데이트가 정상적으로 이루어져야 한다")
    void updateWalletBalance_success() {
        // Given
        Long customerId = savedCustomer.getCustomerId();
        Wallet wallet = walletJpaRepository.findByCustomerId(customerId).orElseThrow();
        int addAmount = 5000000;

        // When
        wallet.chargeCash(addAmount); // 잔액 변경
        walletJpaRepository.save(wallet);

        // Then
        Wallet updatedWallet = walletJpaRepository.findByCustomerId(customerId).orElseThrow();
        assertThat(updatedWallet.getBalance()).isEqualTo(15000000); // 10000000 + 5000000 = 15000000
    }

    @Test
    @DisplayName("Wallet을 삭제하면 조회되지 않아야 한다")
    void deleteWallet_success() {
        // Given
        Long customerId = savedCustomer.getCustomerId();
        Wallet wallet = walletJpaRepository.findByCustomerId(customerId).orElseThrow();

        // When
        walletJpaRepository.delete(wallet);

        // Then
        Optional<Wallet> deletedWallet = walletJpaRepository.findByCustomerId(customerId);
        assertThat(deletedWallet).isEmpty(); // 삭제 후 조회 시 Optional.empty()
    }
}
