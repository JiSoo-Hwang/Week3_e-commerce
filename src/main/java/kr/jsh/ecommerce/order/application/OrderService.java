package kr.jsh.ecommerce.order.application;

import kr.jsh.ecommerce.customer.domain.Customer;
import kr.jsh.ecommerce.order.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService {

    public final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository){
        this.orderRepository=orderRepository;

    }

    public OrderSheet placeOrder(Customer customer, List<OrderItem> orderItems){
        // 주문 생성
        Order order = new Order(customer, OrderStatus.PENDING_PAYMENT);

        // 주문 항목 추가
        orderItems.forEach(order::addOrderItem);

        // 주문 저장
        orderRepository.save(order);

        // 주문서 생성 및 반환
        return new OrderSheet(order);
    }
}
