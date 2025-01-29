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

    public static Customer create(Long customerId, String customerName) {
        Customer customer = new Customer();
        customer.customerId = customerId;
        customer.customerName = customerName;
        return customer;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

}
