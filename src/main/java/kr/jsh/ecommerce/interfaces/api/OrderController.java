package kr.jsh.ecommerce.interfaces.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order API",description = "주문 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {


}
