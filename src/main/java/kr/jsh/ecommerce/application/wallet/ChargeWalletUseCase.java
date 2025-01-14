package kr.jsh.ecommerce.application.wallet;

import kr.jsh.ecommerce.domain.wallet.Wallet;
import kr.jsh.ecommerce.domain.wallet.WalletService;
import kr.jsh.ecommerce.interfaces.dto.wallet.ChargeWalletRequest;
import kr.jsh.ecommerce.interfaces.dto.wallet.ChargeWalletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChargeWalletUseCase {

    private final WalletService walletService;

    public ChargeWalletResponse chargeWallet(ChargeWalletRequest chargeWalletRequest) {
        Wallet wallet = walletService.getWalletByCustomerId(chargeWalletRequest.customerId());
        return walletService.chargeWallet(wallet,chargeWalletRequest.chargeAmount());
    }
}
