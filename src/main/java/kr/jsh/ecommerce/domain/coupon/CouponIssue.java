package kr.jsh.ecommerce.domain.coupon;

import jakarta.persistence.*;
import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.customer.Customer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponIssue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponIssueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id",nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Column(nullable = true)
    private LocalDateTime usedAt;

    public boolean isExpired() {
    return LocalDateTime.now().isAfter(issuedAt.plusDays(30));
    }

    public void markAsUsed() {
        if(isExpired()){
            throw new BaseCustomException(BaseErrorCode.COUPON_EXPIRED);//TODO:만료날짜안내
        }
        if(usedAt != null){
           throw new BaseCustomException(BaseErrorCode.ALREADY_USED_COUPON);//TODO:사용날짜안내
        }
        this.usedAt = LocalDateTime.now();
    }
}
