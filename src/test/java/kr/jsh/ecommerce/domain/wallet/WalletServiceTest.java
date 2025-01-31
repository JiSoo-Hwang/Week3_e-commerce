package kr.jsh.ecommerce.domain.wallet;

import jakarta.persistence.OptimisticLockException;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.interfaces.api.wallet.dto.ChargeWalletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WalletService 단위 테스트")
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @Test
    @DisplayName("고객 ID로 지갑을 조회하면 올바른 Wallet 객체를 반환해야 한다")
    void getWalletByCustomerId_success(){
        //Given : 테스트용 고객 ID & Mock Wallet 객체 생성
        Long customerId = 5L;
        Customer mockCustomer = Customer.create(customerId,"김경덕");
        Wallet mockWallet = Wallet.create(mockCustomer,500000);

        //Mock 설정 : walletRepository.findByCustomerId() 호출 시 mockWallet 반환하도록 설정
        when(walletRepository.findByCustomerId(customerId)).thenReturn(Optional.of(mockWallet));

        //When : WalletService 실행
        Wallet wallet = walletService.getWalletByCustomerId(customerId);

        //Then : 반환된 Wallet 객체가 올바른 값인가?
        assertThat(wallet).isNotNull();
        assertThat(wallet.getBalance()).isEqualTo(500000);

        //Verify : walletRepository.findByCustomerId()가 정확히 한 번 호출되었는지 검증
        verify(walletRepository,times(1)).findByCustomerId(customerId);
    }

    @Test
    @DisplayName("존재하지 않는 고객 ID로 지갑을 조회하면 예외가 발생해야 한다")
    void getWalletByCustomerId_notFound(){
        //Given : 존재하지 않는 고객ID
        Long customerId = 999L;

        //Mock 설정 : walletRepository.findByCustomerId()가 빈 값 반환
        when(walletRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        // When & Then: 예외가 발생해야 함
        assertThatThrownBy(() -> walletService.getWalletByCustomerId(customerId))
                .isInstanceOf(BaseCustomException.class)
                .hasMessageContaining("not found");

        // Verify: walletRepository.findByCustomerId()가 정확히 한 번 호출되었는지 검증
        verify(walletRepository, times(1)).findByCustomerId(customerId);
    }

    @Test
    @DisplayName("지갑 충전 시 올바른 금액이 반영되어야 한다")
    void chargeWallet_success() {
        // Given: 테스트용 Wallet 객체
        Customer mockCustomer = Customer.create(1L, "김경덕");
        Wallet mockWallet = Wallet.create(mockCustomer, 500000); // 초기 잔액 5,000원
        int chargeAmount = 300000; // 충전 금액

        // Mock 설정: walletRepository.save() 호출 시 정상적으로 저장된 Wallet 반환
        when(walletRepository.save(any(Wallet.class))).thenReturn(mockWallet);

        // When: WalletService 실행
        ChargeWalletResponse response = walletService.chargeWallet(mockWallet, chargeAmount);

        // Then: 잔액이 800,000원(500,000 + 300,000)이 되어야 함
        assertThat(response).isNotNull();
        assertThat(response.balance()).isEqualTo(800000);

        // Verify: walletRepository.save()가 정확히 한 번 호출되었는지 검증
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    @DisplayName("지갑 충전 시 OptimisticLockException 발생하면 최대 3번 재시도해야 한다")
    void chargeWallet_optimisticLockRetry() {
        // Given: 테스트용 Wallet 객체
        Long customerId = 1L;
        Customer mockCustomer = Customer.create(customerId, "김경덕");
        Wallet mockWallet = Wallet.create(mockCustomer, 500000);
        int chargeAmount = 300000;

        //findByCustomerId()가 항상 같은 Wallet을 반환하도록 설정
        when(walletRepository.findByCustomerId(customerId)).thenReturn(Optional.of(mockWallet));

        // Mock 설정: 첫 번째, 두 번째 호출에서 OptimisticLockException 발생 → 세 번째 시도에서 정상 저장
        when(walletRepository.save(any(Wallet.class)))
                .thenThrow(new OptimisticLockException()) // 1st
                .thenThrow(new OptimisticLockException()) // 2nd
                .thenReturn(mockWallet); // 3rd (성공)

        // When: WalletService 실행
        ChargeWalletResponse response = walletService.chargeWallet(mockWallet, chargeAmount);

        // Then: 최종적으로 충전 성공
        assertThat(response).isNotNull();
        assertThat(response.balance()).isEqualTo(800000);

        // Verify: walletRepository.save()가 정확히 3번 호출되었는지 검증 (재시도 2번 + 최종 성공 1번)
        verify(walletRepository, times(3)).save(any(Wallet.class));

        // Verify: walletRepository.findByCustomerId()도 재시도마다 호출되었는지 확인
        verify(walletRepository, times(2)).findByCustomerId(customerId);//2번 호출 (첫 번째 & 두 번째 실패 후)
    }
}
