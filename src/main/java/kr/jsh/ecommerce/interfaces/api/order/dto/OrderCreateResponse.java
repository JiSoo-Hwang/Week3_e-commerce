package kr.jsh.ecommerce.interfaces.api.order.dto;

import kr.jsh.ecommerce.domain.order.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record OrderCreateResponse(
        String orderId,
        String customerName,
        LocalDateTime orderDate,
        int totalAmount,
        String orderStatus,
        List<OrderFruitResponse> orderFruits
) {
    public static OrderCreateResponse fromOrder(Order order) {
        return new OrderCreateResponse(
                String.valueOf(order.getOrderId()),
                order.getCustomer().getCustomerName(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getOrderFruits().stream()
                        .map(OrderFruitResponse::fromOrderFruit)
                        .collect(Collectors.toList())
        );
    }
}
