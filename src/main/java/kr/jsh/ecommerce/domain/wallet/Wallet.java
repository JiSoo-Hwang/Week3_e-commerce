package kr.jsh.ecommerce.domain.wallet;

import jakarta.persistence.*;
import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.customer.Customer;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;

    @OneToOne(mappedBy = "customer",cascade = CascadeType.ALL,orphanRemoval = true)
    private Customer customer;

    @Column(nullable = false)
    private int balance;

    public void spendCash(int amount){
        if(amount>this.balance){
            throw new BaseCustomException(BaseErrorCode.INVALID_PARAMETER,new String[]{String.valueOf(amount)});
        }
        this.balance-=amount;
    }

    public void chargeCash(int amount){
        if(amount<0||amount%100!=0){
            throw new BaseCustomException(BaseErrorCode.INVALID_PARAMETER,new String[]{String.valueOf(amount)});
        }
        this.balance+=amount;
    }
}
