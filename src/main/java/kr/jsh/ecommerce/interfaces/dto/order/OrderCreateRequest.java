package kr.jsh.ecommerce.interfaces.dto.order;

import java.util.List;

public record OrderCreateRequest(
        String customerId,
        List<OrderFruitRequest> orderFruits
) {
}
