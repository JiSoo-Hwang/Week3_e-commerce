package kr.jsh.ecommerce.domain.order;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.fruit.Fruit;
import kr.jsh.ecommerce.domain.fruit.FruitRepository;
import kr.jsh.ecommerce.interfaces.api.order.dto.OrderCreateResponse;
import kr.jsh.ecommerce.interfaces.api.order.dto.OrderFruitRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
        //주문 가능한 과일만 저장
        List<OrderFruit> validOrderFruits = new ArrayList<>();

        for (OrderFruitRequest orderFruitRequest : orderFruitRequests) {
            try {
                // 1. 비관적 락을 사용하여 상품 조회
                Fruit fruit = fruitRepository.findByIdForUpdate(Long.parseLong(orderFruitRequest.fruitId()))
                        .orElseThrow(() -> new BaseCustomException(BaseErrorCode.NOT_FOUND, new String[]{orderFruitRequest.fruitName()}));

                // 2. 재고 차감
                deductStockAndSaveSingleFruit(fruit,orderFruitRequest.quantity());
                // 3. 주문 항목 생성 및 추가
                OrderFruit orderFruit = new OrderFruit(
                        order,
                        fruit,
                        orderFruitRequest.fruitPrice(),
                        orderFruitRequest.quantity()
                );
                validOrderFruits.add(orderFruit);
            } catch (BaseCustomException ex) {
                log.warn("재고 부족으로 주문에서 제외된 과일 : {}", orderFruitRequest.fruitName());
            }
        }

        // 모든 과일이 품절되었으면 주문을 저장하지 않고 예외 발생
        if (order.getOrderFruits().isEmpty()) {
            throw new BaseCustomException(BaseErrorCode.OUT_OF_STOCK, new String[]{"모든 상품이 품절되었습니다."});
        }


        //4.주문 가능한 과일만 주문 내역에 담기
        for (OrderFruit orderFruit : validOrderFruits) {
            order.addOrderFruit(orderFruit);
        }

        // 5. 주문 총액 계산 및 저장
        order.calculateTotalAmount();
        orderRepository.save(order);

        return OrderCreateResponse.fromOrder(order);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deductStockAndSaveSingleFruit(Fruit fruit, int quantity) {
        if (!fruit.hasEnoughStock(quantity)) {
            throw new BaseCustomException(BaseErrorCode.OUT_OF_STOCK, new String[]{fruit.getFruitName()});
        }
        fruit.deductStock(quantity);
        fruitRepository.save(fruit);
    }

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new BaseCustomException(BaseErrorCode.NOT_FOUND));
    }
}
