package kr.jsh.ecommerce.order.presentation.controller;

import kr.jsh.ecommerce.order.domain.OrderSheet;
import kr.jsh.ecommerce.order.facade.OrderFacade;
import kr.jsh.ecommerce.order.presentation.dto.OrderClientRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderFacade orderFacade;

    public OrderController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @PostMapping
    public OrderSheet placeOrder(@RequestBody OrderClientRequest orderClientRequest) {
        return orderFacade.placeOrder(orderClientRequest.customer(),orderClientRequest.orderRequests());
    }
}
