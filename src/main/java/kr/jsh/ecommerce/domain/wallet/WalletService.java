package kr.jsh.ecommerce.domain.wallet;

import jakarta.persistence.OptimisticLockException;
import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.interfaces.api.wallet.dto.ChargeWalletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    @Transactional
    public Wallet getWalletByCustomerId(Long customerId) {
        return walletRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new BaseCustomException(BaseErrorCode.NOT_FOUND));
    }

    @Transactional
    public ChargeWalletResponse chargeWallet(Wallet wallet, int amount) {
        int maxRetryCount = 3; // 최대 재시도 횟수
        int retryCount = 0;

        while (retryCount < maxRetryCount) {
            try {
                wallet.chargeCash(amount);
                Wallet chargedWallet = walletRepository.save(wallet);
                return new ChargeWalletResponse(chargedWallet.getBalance());
            } catch (OptimisticLockException e) {
                retryCount++;
                if (retryCount == maxRetryCount) {
                    throw new BaseCustomException(BaseErrorCode.CONCURRENCY_ISSUE, new String[]{"지갑 충전"});
                }
            }
        }
        throw new BaseCustomException(BaseErrorCode.UNKNOWN_ERROR, new String[]{"지갑 충전"});
    }
}
