package kr.jsh.ecommerce.domain.wallet;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.interfaces.dto.wallet.ChargeWalletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WalletService {
    private final WalletRepository walletRepository;

    public Wallet getWalletByCustomerId(Long customerId) {
    return walletRepository.findByCustomerId(customerId)
            .orElseThrow(()->new BaseCustomException(BaseErrorCode.NOT_FOUND));
    }

    public ChargeWalletResponse chargeWallet(Wallet wallet, int amount) {
        wallet.chargeCash(amount);
        Wallet chargedWallet = walletRepository.save(wallet);
        return new ChargeWalletResponse(chargedWallet.getBalance());
    }

}
