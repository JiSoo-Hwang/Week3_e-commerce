package kr.jsh.ecommerce.domain.coupon;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponIssue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponIssueId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Date issuedAt;

    @Column(nullable = true)
    private Date usedAt;

    public void setStatus(String status){
        this.status=status;
    }

    public void setUsedAt(Date usedAt){
        this.usedAt=usedAt;
    }
}
