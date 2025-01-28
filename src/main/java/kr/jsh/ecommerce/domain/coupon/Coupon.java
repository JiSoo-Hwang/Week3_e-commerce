package kr.jsh.ecommerce.domain.coupon;

import jakarta.persistence.*;
import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.customer.Customer;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    @Column(nullable = false)
    private String couponName;

    @Column(nullable = false)
    private int discountAmount;

    @Column(nullable = false)
    private int maxQuantity; // 최대 발급 가능 수량

    @Column(nullable = false)
    private int issuedCount; // 현재 발급된 수량


    public void issueCoupon(){
        if(issuedCount>=maxQuantity){
            throw new BaseCustomException(BaseErrorCode.OUT_OF_STOCK,new String[]{this.couponName});
        }
        issuedCount++;
    }
}
