package kr.jsh.ecommerce.infrastructure.wallet;

import kr.jsh.ecommerce.domain.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletJpaRepository extends JpaRepository<Wallet,Long> {
}
