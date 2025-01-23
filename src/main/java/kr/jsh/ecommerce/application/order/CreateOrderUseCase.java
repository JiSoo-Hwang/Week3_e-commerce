package kr.jsh.ecommerce.application.order;

import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.customer.CustomerService;
import kr.jsh.ecommerce.domain.fruit.FruitService;
import kr.jsh.ecommerce.domain.order.Order;
import kr.jsh.ecommerce.domain.order.OrderService;
import kr.jsh.ecommerce.interfaces.api.order.dto.OrderCreateRequest;
import kr.jsh.ecommerce.interfaces.api.order.dto.OrderCreateResponse;
import kr.jsh.ecommerce.interfaces.api.order.dto.OrderFruitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CreateOrderUseCase {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final FruitService fruitService;

    public OrderCreateResponse createOrder(OrderCreateRequest orderCreateRequest) {

        //customerId로 Customer 조회
        Customer customer = customerService.findCustomerById(Long.parseLong(orderCreateRequest.customerId()));
        Order order = orderService.createOrder(customer);
        return orderService.saveOrder(order, orderCreateRequest.orderFruits());
    }
}