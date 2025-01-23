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
    @JoinColumn(name = "fruit_id", nullable = false)
    private Fruit fruit;

    @Column(nullable = false)
    private int fruitPrice;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int subTotal;

    public OrderFruit(Order order,Fruit fruit,int fruitPrice, int quantity){
        this.order=order;
        this.fruit=fruit;
        this.fruitPrice=fruitPrice;
        this.quantity=quantity;
        this.subTotal=calculateSubTotal(fruitPrice,quantity);
    }
    private int calculateSubTotal(int fruitPrice,int quantity){
        return fruitPrice*quantity;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
}
