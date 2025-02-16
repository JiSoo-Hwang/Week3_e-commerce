package kr.jsh.ecommerce.event;

import kr.jsh.ecommerce.domain.order.Order;
import org.springframework.context.ApplicationEvent;

public class OrderPaidEvent extends ApplicationEvent {

    private final Order order;

    public OrderPaidEvent(Object source, Order order){
        super(source);
        this.order = order;
    }

    public Order getOrder(){
        return order;
    }
}
