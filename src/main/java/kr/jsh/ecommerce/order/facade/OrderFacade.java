package kr.jsh.ecommerce.order.facade;

import kr.jsh.ecommerce.customer.domain.Customer;
import kr.jsh.ecommerce.order.application.OrderService;
import kr.jsh.ecommerce.order.domain.OrderItem;
import kr.jsh.ecommerce.order.domain.OrderSheet;
import kr.jsh.ecommerce.order.presentation.dto.OrderRequest;
import kr.jsh.ecommerce.product.application.StockService;
import kr.jsh.ecommerce.product.domain.Stock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderFacade {

    private final OrderService orderService;
    private final StockService stockService;

    public OrderFacade(OrderService orderService, StockService stockService){
        this.orderService = orderService;
        this.stockService = stockService;
    }

    public OrderSheet placeOrder(Customer customer, List<OrderRequest> orderRequests) {
        // 재고 검증 및 OrderItem 생성 (Order와는 연관되지 않음)
        List<OrderItem> orderItems = orderRequests.stream()
                .map(request -> {
                    Stock stock = stockService.findAndValidateStock(
                            request.productId(),
                            request.productSize(),
                            request.productQuantity()
                    );
                    stockService.deductStock(stock,request.productQuantity());
                    return new OrderItem(stock, request.productQuantity());
                })
                .toList();

        // 주문 생성 및 반환
        return orderService.placeOrder(customer, orderItems);
    }
}
