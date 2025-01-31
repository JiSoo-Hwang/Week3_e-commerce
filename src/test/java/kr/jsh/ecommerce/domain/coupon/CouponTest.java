package kr.jsh.ecommerce.domain.coupon;

import kr.jsh.ecommerce.base.exception.BaseCustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Coupon 엔터티 테스트")
public class CouponTest {

    @Test
    @DisplayName("Coupon 객체가 정상적으로 생성되어야 한다")
    void createCoupon_success() {
        // Given & When
        Coupon coupon = Coupon.builder()
                .couponName("1000원 할인 쿠폰")
                .discountAmount(1000)
                .maxQuantity(5) // 최대 5장 발급 가능
                .issuedCount(0)  // 아직 발급되지 않음
                .build();

        // Then
        assertThat(coupon).isNotNull();
        assertThat(coupon.getCouponName()).isEqualTo("1000원 할인 쿠폰");
        assertThat(coupon.getDiscountAmount()).isEqualTo(1000);
        assertThat(coupon.getMaxQuantity()).isEqualTo(5);
        assertThat(coupon.getIssuedCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("쿠폰이 정상적으로 발급되어야 한다")
    void issueCoupon_success(){
        //Given
        Coupon coupon = Coupon.builder()
                .couponName("무료배송쿠폰")
                .discountAmount(2500)
                .maxQuantity(5)
                .issuedCount(0)
                .build();

        coupon.issueCoupon();
        coupon.issueCoupon();
        coupon.issueCoupon();

        assertThat(coupon.getIssuedCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("발급 가능 수량을 초과하면 예외가 발생해야 한다")
    void issueCoupon_exceedMaxQuantity(){
        //Given
        Coupon coupon = Coupon.builder()
                .couponName("설 한정 쿠폰")
                .discountAmount(2000)
                .issuedCount(7)
                .maxQuantity(7)
                .build();

        //When & Then
        assertThrows(BaseCustomException.class,coupon::issueCoupon);
    }
}
