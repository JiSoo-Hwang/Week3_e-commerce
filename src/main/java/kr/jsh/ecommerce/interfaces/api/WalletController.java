package kr.jsh.ecommerce.interfaces.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.jsh.ecommerce.application.wallet.ChargeWalletUseCase;
import kr.jsh.ecommerce.application.wallet.GetWalletUseCase;
import kr.jsh.ecommerce.base.dto.response.BaseResponseContent;
import kr.jsh.ecommerce.interfaces.dto.wallet.ChargeWalletRequest;
import kr.jsh.ecommerce.interfaces.dto.wallet.ChargeWalletResponse;
import kr.jsh.ecommerce.interfaces.dto.wallet.WalletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Wallet API",description = "지갑 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    private final GetWalletUseCase getWalletUseCase;
    private final ChargeWalletUseCase chargeWalletUseCase;


    @Operation(summary = "지갑충전",description = "지갑에 입력한 금액을 충전합니다.")
    @Parameter(name = "chargeWalletRequest",description = "고객 정보와 충전 금액",required = true)
    @PostMapping
    public ResponseEntity<BaseResponseContent> chargeWallet(@RequestBody ChargeWalletRequest chargeWalletRequest){
        ChargeWalletResponse response = chargeWalletUseCase.chargeWallet(chargeWalletRequest);
        BaseResponseContent responseContent = new BaseResponseContent(response);
        responseContent.setMessage("금액 충전 완료! 즐거운 쇼핑되세요 :)");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseContent);
    }

    @Operation(summary = "지갑 잔액 조회",description = "고객 식별자를 기준으로 잔액을 조회합니다.")
    @Parameter(name = "customerId",description = "고객 정보와",required = true)
    @GetMapping("/{customerId}")
    public ResponseEntity<BaseResponseContent> getWalletById(@PathVariable Long customerId){//TODO:이게 최선인지 생각해볼 것...
        WalletResponse walletResponse = getWalletUseCase.getWalletByCustomerId(customerId);
        return ResponseEntity.ok(new BaseResponseContent(walletResponse));
    }
}
