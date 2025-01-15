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
import java.time.format.DateTimeFormatter;

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

    @Column(nullable = true)
    private LocalDateTime expiredAt;

    public static CouponIssue create(Coupon coupon, Customer customer, String status) {
        LocalDateTime now = LocalDateTime.now();
        return new CouponIssue(
                null,
                coupon,
                customer,
                status,
                now,
                null,
                now.plusDays(30) // 발급일 기준 30일 후
        );
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(issuedAt.plusDays(30));
    }
    public void markAsUsed() {
        if(isExpired()){
            String formattedExpiredDate = expiredAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            throw new BaseCustomException(BaseErrorCode.COUPON_EXPIRED,new String[]{formattedExpiredDate});//TODO:만료날짜안내
        }
        if(usedAt != null){
            String formattedUsedDate = usedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
           throw new BaseCustomException(BaseErrorCode.ALREADY_USED_COUPON,new String[]{formattedUsedDate});//TODO:사용날짜안내
        }
        this.usedAt = LocalDateTime.now();
    }
}
