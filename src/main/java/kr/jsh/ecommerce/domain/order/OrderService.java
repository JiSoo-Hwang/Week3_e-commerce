package kr.jsh.ecommerce.domain.order;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.fruit.Fruit;
import kr.jsh.ecommerce.domain.fruit.FruitRepository;
import kr.jsh.ecommerce.interfaces.api.order.dto.OrderCreateResponse;
import kr.jsh.ecommerce.interfaces.api.order.dto.OrderFruitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orderStatus(OrderStatus.PENDING)
                .totalAmount(0)
                .build();
    }

    @Transactional
    public OrderCreateResponse saveOrder(Order order, List<OrderFruitRequest> orderFruitRequests) {
        for (OrderFruitRequest orderFruitRequest : orderFruitRequests) {
            // 1. 비관적 락을 사용하여 상품 조회
            Fruit fruit = fruitRepository.findByIdForUpdate(Long.parseLong(orderFruitRequest.fruitId()))
                    .orElseThrow(() -> new BaseCustomException(BaseErrorCode.NOT_FOUND, new String[]{orderFruitRequest.fruitName()}));

            // 2. 재고 차감
            fruit.deductStock(orderFruitRequest.quantity());
            fruitRepository.save(fruit);

            // 3. 주문 항목 생성 및 추가
            OrderFruit orderFruit = new OrderFruit(
                    order,
                    fruit,
                    orderFruitRequest.fruitPrice(),
                    orderFruitRequest.quantity()
            );
            order.addOrderFruit(orderFruit);
        }

        // 4. 주문 총액 계산 및 저장
        order.calculateTotalAmount();
        orderRepository.save(order);

        return OrderCreateResponse.fromOrder(order);
    }

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new BaseCustomException(BaseErrorCode.NOT_FOUND));
    }
}
