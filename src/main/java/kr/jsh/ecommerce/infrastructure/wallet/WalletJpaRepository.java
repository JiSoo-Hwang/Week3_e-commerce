package kr.jsh.ecommerce.infrastructure.wallet;

import kr.jsh.ecommerce.domain.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WalletJpaRepository extends JpaRepository<Wallet,Long> {
    //고객의 식별자를 기준으로 지갑 찾기
    @Query("SELECT w FROM Wallet w WHERE w.customer.customerId = :customerId")
    Optional<Wallet> findByCustomerId(Long customerId);
}
