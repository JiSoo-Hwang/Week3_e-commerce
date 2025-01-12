package kr.jsh.ecommerce.order.domain;

import jakarta.persistence.*;
import kr.jsh.ecommerce.customer.domain.Customer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID", nullable = false)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "ORDER_STATUS", nullable = false)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    protected Order() {}

    public Order(Customer customer, OrderStatus orderStatus) {
        this.customer = customer;
        this.orderStatus = OrderStatus.PENDING_PAYMENT;
    }

    public void addOrderItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
    }
}
