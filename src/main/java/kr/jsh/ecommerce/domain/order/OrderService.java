package kr.jsh.ecommerce.domain.order;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.fruit.Fruit;
import kr.jsh.ecommerce.domain.fruit.FruitRepository;
import kr.jsh.ecommerce.interfaces.dto.order.OrderCreateResponse;
import kr.jsh.ecommerce.interfaces.dto.order.OrderFruitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final FruitRepository fruitRepository;
    public Order createOrder(Customer customer) {
        return Order.builder()
                .customer(customer)
                .orderDate(LocalDateTime.now())
                .orderStatus("PENDING")
                .totalAmount(0)
                .build();
    }

    public OrderCreateResponse saveOrder(Order order, List<OrderFruitRequest> orderFruitRequests){
        for(OrderFruitRequest orderFruitRequest : orderFruitRequests){
            Fruit fruit = fruitRepository.findById(Long.parseLong(orderFruitRequest.fruitId()))
                    .orElseThrow(()->new BaseCustomException(BaseErrorCode.NOT_FOUND,new String[]{orderFruitRequest.fruitName()}));
            fruit.deductStock(orderFruitRequest.quantity());
            fruitRepository.save(fruit);
            OrderFruit orderFruit = new OrderFruit(
                    order,
                    order.getCustomer(),
                    fruit,
                    orderFruitRequest.fruitPrice(),
                    orderFruitRequest.quantity()
            );
            order.addOrderFruit(orderFruit);
        }
        order.calculateTotalAmount();
        orderRepository.save(order);
        return OrderCreateResponse.fromOrder(order);
    }
}
