package kr.jsh.ecommerce.domain.order;

import jakarta.persistence.*;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.fruit.Fruit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_fruit")
public class OrderFruit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderFruitId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "fruit_id", nullable = false)
    private Fruit fruit;

    @Column(nullable = false)
    private int fruitPrice;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int subTotal;

    public void setOrder(Order order) {
        this.order = order;
    }
}
