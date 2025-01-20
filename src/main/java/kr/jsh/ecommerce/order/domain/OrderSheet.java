package kr.jsh.ecommerce.order.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class OrderSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDERSHEET_ID")
    private Long orderSheetId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    protected OrderSheet() {}

    public OrderSheet(Order order) {
        this.order = order;
        this.createdAt = LocalDateTime.now();
    }
}
