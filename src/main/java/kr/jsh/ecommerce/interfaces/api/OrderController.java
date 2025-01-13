package kr.jsh.ecommerce.interfaces.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.jsh.ecommerce.application.order.CreateOrderUseCase;
import kr.jsh.ecommerce.base.dto.response.BaseResponseContent;
import kr.jsh.ecommerce.interfaces.dto.order.OrderCreateRequest;
import kr.jsh.ecommerce.interfaces.dto.order.OrderCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order API",description = "주문 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;

    @Operation(summary = "주문생성",description = "주문 내역을 가지고 주문을 생성합니다.")
    @Parameter(name="orderCreateRequest",description = "주문 생성 정보", required = true)
    @PostMapping
    public ResponseEntity<BaseResponseContent> createOrder(@RequestBody OrderCreateRequest orderCreateRequest){
        OrderCreateResponse response = createOrderUseCase.createOrder(orderCreateRequest.customerId(),orderCreateRequest.orderFruits());
        BaseResponseContent responseContent = new BaseResponseContent(response);
        responseContent.setMessage("주문 성공! 빠르게 배송해드릴게요 :)");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseContent);
    }

}
