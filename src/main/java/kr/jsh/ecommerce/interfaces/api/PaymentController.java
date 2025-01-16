package kr.jsh.ecommerce.interfaces.api;

import kr.jsh.ecommerce.application.payment.PayForOrderUseCase;
import kr.jsh.ecommerce.base.dto.response.BaseResponseContent;
import kr.jsh.ecommerce.interfaces.dto.payment.PaymentRequest;
import kr.jsh.ecommerce.interfaces.dto.payment.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PayForOrderUseCase payForOrderUseCase;

    @PostMapping("/{orderId}")
    public ResponseEntity<BaseResponseContent> createPayment(@RequestBody PaymentRequest paymentRequest){
        PaymentResponse response = payForOrderUseCase.payForOrder(paymentRequest);
        BaseResponseContent responseContent = new BaseResponseContent(response);
        responseContent.setMessage("결제 성공! 빠르게 배송해드릴게요 :)");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseContent);
    }
}
