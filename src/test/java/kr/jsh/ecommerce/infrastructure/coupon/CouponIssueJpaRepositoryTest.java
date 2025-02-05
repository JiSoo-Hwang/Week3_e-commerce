package kr.jsh.ecommerce.infrastructure.coupon;

import kr.jsh.ecommerce.domain.coupon.Coupon;
import kr.jsh.ecommerce.domain.coupon.CouponIssue;
import kr.jsh.ecommerce.domain.customer.Customer;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@DisplayName("CouponIssueJpaRepository 테스트")
public class CouponIssueJpaRepositoryTest {

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.32")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private CouponIssueJpaRepository couponIssueJpaRepository;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private CustomerJpaRepository customerJpaRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
    }

    private Coupon savedCoupon;
    private Customer savedCustomer;
    private CouponIssue savedCouponIssue;

    @BeforeEach
    void setUp() {
        // Given: 테스트용 Customer 저장
        Customer customer = Customer.create("황지수", "서울시", "01012345678");
        savedCustomer = customerJpaRepository.save(customer);

        // Given: 테스트용 Coupon 저장
        Coupon coupon = Coupon.builder()
                .couponName("VIP 고객 할인 쿠폰")
                .discountAmount(10_000)
                .maxQuantity(50)
                .issuedCount(0)
                .build();
        savedCoupon = couponJpaRepository.save(coupon);

        // Given: CouponIssue 저장
        CouponIssue couponIssue = CouponIssue.create(savedCoupon, savedCustomer);
        savedCouponIssue = couponIssueJpaRepository.save(couponIssue);
    }

    @AfterEach
    void tearDown() {
        // 데이터 정리
        couponIssueJpaRepository.deleteAll();
        couponJpaRepository.deleteAll();
        customerJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("findByCustomerId()로 고객이 발급받은 쿠폰 목록을 정상 조회해야 한다")
    void findByCustomerId_success() {
        // When
        List<CouponIssue> result = couponIssueJpaRepository.findByCustomerId(savedCustomer.getCustomerId());

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCoupon().getCouponName()).isEqualTo("VIP 고객 할인 쿠폰");
    }
}
