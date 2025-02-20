package kr.jsh.ecommerce.domain.payment;

import kr.jsh.ecommerce.domain.order.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderData {
    private final Long orderId;
    private final LocalDateTime orderDate;
    private final int totalAmount;
/*
    public static OrderData from(final Order order){
        return new OrderData(order.getOrderId(),order.getOrderDate(),order.getTotalAmount());
    }*/
}
