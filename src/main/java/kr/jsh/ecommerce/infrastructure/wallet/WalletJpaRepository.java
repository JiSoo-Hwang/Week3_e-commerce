package kr.jsh.ecommerce.infrastructure.wallet;

import kr.jsh.ecommerce.domain.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WalletJpaRepository extends JpaRepository<Wallet, Long> {

    @Query("SELECT w FROM Wallet w WHERE w.customer.customerId = :customerId")
    Optional<Wallet> findByCustomerId(@Param("customerId") Long customerId);
}
