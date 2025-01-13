package kr.jsh.ecommerce.interfaces.dto.order;

import kr.jsh.ecommerce.domain.order.OrderFruit;

public record OrderFruitResponse(
        String fruitName,
        int quantity,
        int subTotal
) {
    public static OrderFruitResponse fromOrderFruit(OrderFruit orderFruit) {
        return new OrderFruitResponse(
                orderFruit.getFruit().getFruitName(),
                orderFruit.getQuantity(),
                orderFruit.getSubTotal()
        );
    }
}
