package kr.jsh.ecommerce.domain.coupon;

import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.customer.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CouponIssue 엔터티 테스트")
public class CouponIssueTest {

    @Test
    @DisplayName("CouponIssue 객체가 정상적으로 생성되어야 한다")
    void createCouponIssue_success() {
        //Given
        Customer mockCustomer = Customer.create(1L, "이충헌");
        Coupon mockCoupon = Coupon.builder()
                .couponName("연휴 한정 쿠폰")
                .discountAmount(1000)
                .maxQuantity(5)
                .issuedCount(0)
                .build();

        //When
        CouponIssue couponIssue = CouponIssue.create(mockCoupon, mockCustomer);

        //Then
        assertThat(couponIssue).isNotNull();
        assertThat(couponIssue.getCoupon()).isEqualTo(mockCoupon);
        assertThat(couponIssue.getCustomer()).isEqualTo(mockCustomer);
        assertThat(couponIssue.getIssuedAt()).isNotNull();
        assertThat(couponIssue.getExpiredAt()).isEqualTo(couponIssue.getIssuedAt().plusDays(30));
        assertThat(couponIssue.getStatus()).isEqualTo(CouponStatus.ISSUED);
    }

    @Test
    @DisplayName("CouponIssue 생성 시 쿠폰 발급 수량이 증가해야 한다")
    void createCouponIssue_increaseIssuedCount() {
        //Given
        Customer mockCustomer = Customer.create(1L, "이충헌");
        Coupon mockCoupon = Coupon.builder()
                .couponName("연휴 기간 한정 쿠폰")
                .discountAmount(0)
                .maxQuantity(5)
                .issuedCount(1)
                .build();

        //When
        CouponIssue couponIssue = CouponIssue.create(mockCoupon, mockCustomer);

        //Then
        assertThat(couponIssue).isNotNull();
        assertThat(mockCoupon.getIssuedCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("쿠폰이 사용되면 상태가 USED로 변경되어야 한다")
    void markedAsUsed_success() {
        //Given
        Customer mockCustomer = Customer.create(1L, "이충헌");
        Coupon mockCoupon = Coupon.builder()
                .couponName("연휴 기간 한정 쿠폰")
                .discountAmount(2500)
                .issuedCount(1)
                .maxQuantity(5)
                .build();

        CouponIssue couponIssue = CouponIssue.create(mockCoupon, mockCustomer);

        //When
        couponIssue.markAsUsed();

        //Then
        assertThat(couponIssue.getStatus()).isEqualTo(CouponStatus.USED);
        assertThat(couponIssue.getUsedAt()).isNotNull();
    }

    @Test
    @DisplayName("이미 사용된 쿠폰을 다시 사용하면 예외가 발생해야 한다")
    void markedAsUsed_alreadyUsed() {
        //Given
        Customer mockCustomer = Customer.create(1L, "이충헌");
        Coupon mockCoupon = Coupon.builder()
                .couponName("연휴기간 한정 쿠폰")
                .discountAmount(3000)
                .maxQuantity(10)
                .issuedCount(1)
                .build();
        CouponIssue couponIssue = CouponIssue.create(mockCoupon, mockCustomer);
        couponIssue.markAsUsed();

        //When & Then
        BaseCustomException exception = assertThrows(BaseCustomException.class, couponIssue::markAsUsed);
        // 예외 메시지 검증
        String formattedUsedDate = couponIssue.getUsedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        assertThat(exception.getMessage()).contains(formattedUsedDate);
    }

    @Test
    @DisplayName("만료된 쿠폰을 사용하면 예외가 발생해야 한다")
    void markAsUsed_expired(){
        //Given
        Customer mockCustomer = Customer.create(1L,"이충헌");
        Coupon mockCoupon = Coupon.builder()
                .couponName("연휴 기간 한정 쿠폰")
                .discountAmount(3000)
                .maxQuantity(10)
                .issuedCount(5)
                .build();

        CouponIssue couponIssue = CouponIssue.builder()
                .coupon(mockCoupon)
                .customer(mockCustomer)
                .issuedAt(LocalDateTime.now().minusDays(31))
                .expiredAt(LocalDateTime.now().minusDays(1))
                .status(CouponStatus.ISSUED)
                .build();

        //udpateIfExpired() 발급 날짜가 현시점 만료된거라면 상태값 "만료"로 변경
        couponIssue.updateStatusIfExpired();

        //When & Then
        BaseCustomException exception = assertThrows(BaseCustomException.class,couponIssue::markAsUsed);

        //예외 메시지 검증
        String formattedExpiredDate = couponIssue.getExpiredAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        assertThat(exception.getMessage()).contains(formattedExpiredDate);

    }
}