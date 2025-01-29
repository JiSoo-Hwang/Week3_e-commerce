package kr.jsh.ecommerce.domain.customer;

import jakarta.persistence.*;
import kr.jsh.ecommerce.domain.wallet.Wallet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 모든 필드 초기화 생성자 (외부에서 직접 사용 불가)
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

    public static Customer create(String customerName,String customerAddress,String customerPhone) {
        Customer customer = new Customer();
        customer.customerName = customerName;
        customer.customerAddress = customerAddress;
        customer.customerPhone = customerPhone;
        return customer;
    }

    public static Customer create(Long customerId, String customerName) {
        Customer customer = new Customer();
        customer.customerId = customerId;
        customer.customerName = customerName;
        return customer;
    }

    public static Customer create(String customerName) {
        Customer customer = new Customer();
        customer.customerName = customerName;
        return customer;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

}
