package kr.jsh.ecommerce.domain.payment;

import jakarta.persistence.*;
import kr.jsh.ecommerce.domain.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",nullable = false)
    private Order order;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime paidAt;

    public void setOrder(Order order) {
        this.order = order;
    }
}
