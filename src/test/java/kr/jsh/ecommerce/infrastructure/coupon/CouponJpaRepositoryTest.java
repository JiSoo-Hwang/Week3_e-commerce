package kr.jsh.ecommerce.infrastructure.coupon;

import kr.jsh.ecommerce.domain.coupon.Coupon;
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
@DisplayName("CouponJpaRepository 테스트")
public class CouponJpaRepositoryTest {

    @Container
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.32")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url",mysql::getJdbcUrl);
        registry.add("spring.datasource.username",mysql::getUsername);
        registry.add("spring.datasource.password",mysql::getPassword);
        registry.add("spring.datasource.driver-class-name",mysql::getDriverClassName);
    }

    private Coupon savedCoupon;

    @BeforeEach
    void setUp() {
        // Given: 테스트용 Coupon 저장
        Coupon coupon = Coupon.builder()
                .couponName("무료 배송 쿠폰")
                .discountAmount(5000)
                .maxQuantity(10)
                .issuedCount(0)
                .build();
        savedCoupon = couponJpaRepository.save(coupon);
    }

    @AfterEach
    void tearDown() {
        // 데이터 정리
        couponJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("findById()로 저장된 쿠폰을 정상 조회해야 한다")
    void findById_success() {
        // When
        Optional<Coupon> result = couponJpaRepository.findById(savedCoupon.getCouponId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getCouponName()).isEqualTo("무료 배송 쿠폰");
    }

    @Test
    @DisplayName("findByIdForUpdate()가 비관적 락을 사용하여 쿠폰을 조회해야 한다")//락 관련 테스트는 동시성 테스트에서 따로 진행
    void findByIdForUpdate_success() {
        // When
        Optional<Coupon> result = couponJpaRepository.findByIdForUpdate(savedCoupon.getCouponId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getCouponId()).isEqualTo(savedCoupon.getCouponId());
    }
}
