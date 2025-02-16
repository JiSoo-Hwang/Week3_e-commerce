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

    @Column(nullable = false)
    private int balance;

    @Version
    private int version; // 낙관적 락 관리용 필드

    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public static Wallet create(Customer customer, int initialBalance) {
        Wallet wallet = new Wallet();
        wallet.customer = customer;
        wallet.balance = initialBalance;
        return wallet;
    }

    public void spendCash(int amount) {
        if (amount > this.balance||amount%100!=0) {
            throw new BaseCustomException(BaseErrorCode.INSUFFICIENT_BALANCE, new String[]{String.valueOf(amount)});
        }
        this.balance -= amount;
    }

    public void chargeCash(int amount) {
        if (amount < 0 || amount % 100 != 0) {
            throw new BaseCustomException(BaseErrorCode.INVALID_PARAMETER, new String[]{String.valueOf(amount)});
        }
        this.balance += amount;
    }
}
