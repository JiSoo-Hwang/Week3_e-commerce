package kr.jsh.ecommerce.domain.customer;

import jakarta.persistence.*;
import kr.jsh.ecommerce.domain.wallet.Wallet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerAddress;

    @Column(nullable = false)
    private String customerPhone;

    @OneToOne(mappedBy = "customer")
    private Wallet wallet;

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

}
