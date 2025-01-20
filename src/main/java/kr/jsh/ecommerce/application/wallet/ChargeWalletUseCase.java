package kr.jsh.ecommerce.application.wallet;

import kr.jsh.ecommerce.domain.wallet.Wallet;
import kr.jsh.ecommerce.domain.wallet.WalletService;
import kr.jsh.ecommerce.interfaces.api.wallet.dto.ChargeWalletRequest;
import kr.jsh.ecommerce.interfaces.api.wallet.dto.ChargeWalletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChargeWalletUseCase {

    private final WalletService walletService;

    public ChargeWalletResponse chargeWallet(ChargeWalletRequest chargeWalletRequest) {
        Wallet wallet = walletService.getWalletByCustomerId(Long.parseLong(chargeWalletRequest.customerId()));
        return walletService.chargeWallet(wallet,chargeWalletRequest.chargeAmount());
    }
}
