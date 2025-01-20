package kr.jsh.ecommerce.domain.wallet;

import java.util.Optional;

public interface WalletRepository {
    Optional<Wallet> findByCustomerId(Long customerId);
    Wallet save(Wallet wallet);
}
