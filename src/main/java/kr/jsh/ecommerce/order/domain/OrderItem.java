package kr.jsh.ecommerce.order.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import kr.jsh.ecommerce.product.domain.OperationType;
import kr.jsh.ecommerce.product.domain.Stock;
import lombok.Getter;
import org.aspectj.weaver.ast.Or;

@Entity
@Getter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDERITEM_ID")
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STOCK_ID", nullable = false)
    private Stock stock;

    @Column(nullable = false)
    private int quantity;

    protected OrderItem() {
    }

    public OrderItem(Stock stock, int quantity) {
        this.stock = stock;
        this.quantity = quantity;
    }

    public static OrderItem createOrderItem(Stock stock, int quantity) {
        stock.updateQuantity(quantity, OperationType.REMOVE);
        return new OrderItem(stock, quantity);
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
