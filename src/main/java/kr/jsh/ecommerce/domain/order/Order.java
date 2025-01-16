package kr.jsh.ecommerce.domain.order;

import jakarta.persistence.*;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.payment.Payment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 기본 생성자
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Builder용 생성자
@Table(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Setter
    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private int totalAmount;

    @Column(nullable = false)
    private String orderStatus;

    @OneToMany(mappedBy = "order", cascade=CascadeType.PERSIST)
    private List<OrderFruit> orderFruits = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.PERSIST)
    private Payment payment;

    public void addOrderFruit(OrderFruit orderFruit) {
        this.orderFruits.add(orderFruit);
        orderFruit.setOrder(this);
    }

    public void calculateTotalAmount() {
        this.totalAmount = orderFruits.stream()
                .mapToInt(OrderFruit::getSubTotal)
                .sum();
    }

    public void setPayment(Payment payment) {
        this.payment = payment;

        // 양방향 관계 설정
        if (payment != null) {
            payment.setOrder(this);
        }
    }
}
