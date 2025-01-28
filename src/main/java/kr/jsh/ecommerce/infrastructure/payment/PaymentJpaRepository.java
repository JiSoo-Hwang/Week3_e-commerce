package kr.jsh.ecommerce.infrastructure.payment;

import kr.jsh.ecommerce.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment,Long> {
}
