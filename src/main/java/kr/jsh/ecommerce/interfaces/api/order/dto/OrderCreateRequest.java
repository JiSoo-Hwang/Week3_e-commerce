package kr.jsh.ecommerce.interfaces.api.order.dto;

import java.util.List;

public record OrderCreateRequest(
        String customerId,
        List<OrderFruitRequest> orderFruits
) {
}
