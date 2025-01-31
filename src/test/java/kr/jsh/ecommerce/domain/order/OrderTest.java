package kr.jsh.ecommerce.domain.order;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.fruit.Fruit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    private Order order;
    private OrderFruit orderFruit1;
    private OrderFruit orderFruit2;

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .customer(Customer.create("이호민"))
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING)
                .build();

        Fruit apple = Fruit.create("사과", 1000,10,"재고있음");
        Fruit banana = Fruit.create("바나나", 1500,5,"재고있음");

        orderFruit1 = new OrderFruit(order, apple, 1000, 2);
        orderFruit2 = new OrderFruit(order, banana, 1500, 1);
    }

    @Test
    @DisplayName("Order에 OrderFruit 추가 테스트")
    void addOrderFruitTest() {
        order.addOrderFruit(orderFruit1);
        order.addOrderFruit(orderFruit2);

        assertThat(order.getOrderFruits()).hasSize(2);
        assertThat(order.getOrderFruits()).contains(orderFruit1, orderFruit2);
    }

    @Test
    @DisplayName("Order의 총 금액 계산 테스트")
    void calculateTotalAmountTest() {
        order.addOrderFruit(orderFruit1);
        order.addOrderFruit(orderFruit2);
        order.calculateTotalAmount();

        assertThat(order.getTotalAmount()).isEqualTo(3500); // 2000 + 1500
    }

    @Test
    @DisplayName("주문 상태 변경 테스트")
    void changeOrderStatusTest() {
        order.changeOrderStatus(OrderStatus.PAID);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    @DisplayName("취소된 주문은 상태 변경 불가능")
    void cancelledOrderCannotChangeStatusTest() {
        order.changeOrderStatus(OrderStatus.CANCELLED);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            order.changeOrderStatus(OrderStatus.PAID);
        });

        assertThat(exception.getMessage()).isEqualTo("취소된 주문은 상태를 변경할 수 없습니다.");
    }
}
