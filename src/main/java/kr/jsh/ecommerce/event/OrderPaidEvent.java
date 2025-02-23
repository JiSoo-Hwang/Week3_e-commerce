package kr.jsh.ecommerce.event;

import kr.jsh.ecommerce.domain.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaidEvent /*extends ApplicationEvent*/ {

    private Long orderId;
    private Long customerId;
    private int totalAmount;

    public OrderPaidEvent(Order order) {
        this.orderId = order.getOrderId();
        this.customerId = order.getCustomer().getCustomerId();
        this.totalAmount = order.getTotalAmount();
    }
}
