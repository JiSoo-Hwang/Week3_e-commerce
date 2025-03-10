package kr.jsh.ecommerce.application.wallet;

import kr.jsh.ecommerce.domain.wallet.Wallet;
import kr.jsh.ecommerce.domain.wallet.WalletService;
import kr.jsh.ecommerce.interfaces.api.wallet.dto.WalletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetWalletUseCase {
    private final WalletService walletService;
    public WalletResponse getWalletByCustomerId(Long customerId){
        Wallet foundWallet = walletService.getWalletByCustomerId(customerId);
        return WalletResponse.from(foundWallet);
    }
}
