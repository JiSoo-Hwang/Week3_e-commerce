package kr.jsh.ecommerce.customer.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "CUSTOMER_NAME", nullable = false)
    private String customerName;

    @Column(name = "ADDRESS", nullable = false)
    private String customerAddress;

    protected Customer() {}

    public Customer(String customerName, String customerAddress) {
        this.customerName = customerName;
        this.customerAddress=customerAddress;
    }
}
