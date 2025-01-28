package kr.jsh.ecommerce.infrastructure.payment;

import kr.jsh.ecommerce.domain.payment.Payment;
import kr.jsh.ecommerce.domain.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentJpaRepository paymentJpaRepository;
    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }
}
