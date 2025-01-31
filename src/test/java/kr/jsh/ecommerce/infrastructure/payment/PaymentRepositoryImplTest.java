package kr.jsh.ecommerce.infrastructure.payment;

import kr.jsh.ecommerce.domain.payment.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PaymentRepositoryImpl 단위 테스트")
class PaymentRepositoryImplTest {

    @Mock
    private PaymentJpaRepository paymentJpaRepository; // ✅ Mock 객체

    @InjectMocks
    private PaymentRepositoryImpl paymentRepository; // ✅ 테스트 대상

    @Test
    @DisplayName("save()가 JpaRepository의 save()를 호출하고 Payment 객체를 반환해야 한다")
    void savePayment_shouldCallJpaRepositorySave() {
        // Given: 저장할 Payment 객체
        Payment mockPayment = mock(Payment.class);
        when(paymentJpaRepository.save(mockPayment)).thenReturn(mockPayment); // Mock 동작 설정

        // When: Payment 저장 실행
        Payment savedPayment = paymentRepository.save(mockPayment);

        // Then: 반환된 Payment 검증
        assertThat(savedPayment).isNotNull();
        assertThat(savedPayment).isEqualTo(mockPayment);

        // Verify: save() 호출 검증
        verify(paymentJpaRepository, times(1)).save(mockPayment);
    }
}