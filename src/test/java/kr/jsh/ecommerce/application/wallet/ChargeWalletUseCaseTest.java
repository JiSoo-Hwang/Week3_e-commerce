package kr.jsh.ecommerce.application.wallet;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.wallet.Wallet;
import kr.jsh.ecommerce.domain.wallet.WalletService;
import kr.jsh.ecommerce.interfaces.api.wallet.dto.ChargeWalletRequest;
import kr.jsh.ecommerce.interfaces.api.wallet.dto.ChargeWalletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChargeWalletUseCase 테스트")
public class ChargeWalletUseCaseTest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private ChargeWalletUseCase chargeWalletUseCase;

    @Test
    @DisplayName("정상적으로 지갑 충전이 이루어져야 한다")
    void chargeWallet_success(){
        //Given : 테스트용 ChargeWalletRequest & Mock Wallet 생성
        ChargeWalletRequest request = new ChargeWalletRequest("1",5000000);
        Long customerId = 1L;
        Customer mockCustomer = Customer.create(customerId,"한동진");
        Wallet mockWallet = Wallet.create(mockCustomer,5000000);
        ChargeWalletResponse mockResponse = new ChargeWalletResponse(10000000);

        //Mock 설정 : walletService.getWalletByCustomerId() 호출 시 mockWallet 반환
        when(walletService.getWalletByCustomerId(customerId)).thenReturn(mockWallet);

        //Mock 설정 : walletService.chargeWallet() 호출 시 mockResponse 반환
        when(walletService.chargeWallet(mockWallet,request.chargeAmount())).thenReturn(mockResponse);

        //When : UseCase 실행
        ChargeWalletResponse response = chargeWalletUseCase.chargeWallet(request);

        //Then : 반환된 ChargeWalletResponse가 올바른 값인지 검증
        assertThat(response).isNotNull();
        assertThat(response.balance()).isEqualTo(10000000);

        //Verify : walletService.getWalletByCustomerId()가 한 번 호출되었나요?
        verify(walletService,times(1)).getWalletByCustomerId(customerId);
        //Verify : walletService.chargeWallet()가 한 번 호출되었나요?
        verify(walletService,times(1)).chargeWallet(mockWallet,request.chargeAmount());
    }
    @Test
    @DisplayName("존재하지 않는 고객 ID로 충전하면 예외가 발생해야 한다")
    void chargeWallet_notFound() {
        // Given: 존재하지 않는 고객 ID 요청
        ChargeWalletRequest request = new ChargeWalletRequest("999", 5000000);
        Long customerId = 999L;

        // Mock 설정: walletService.getWalletByCustomerId()가 예외를 발생하도록 설정
        when(walletService.getWalletByCustomerId(customerId))
                .thenThrow(new BaseCustomException(BaseErrorCode.NOT_FOUND));

        // When & Then: 예외가 발생해야 함
        assertThrows(BaseCustomException.class, () -> chargeWalletUseCase.chargeWallet(request));

        // Verify: walletService.getWalletByCustomerId()가 정확히 한 번 호출되었는지 검증
        verify(walletService, times(1)).getWalletByCustomerId(customerId);
        // Verify: walletService.chargeWallet()는 호출되지 않아야 함
        verify(walletService, never()).chargeWallet(any(Wallet.class), anyInt());
    }

    @Test
    @DisplayName("유효하지 않은 금액을 충전하면 예외가 발생해야 한다")
    void chargeWallet_invalidAmount() {
        // Given: 충전 금액이 100원 단위가 아닌 경우
        ChargeWalletRequest request = new ChargeWalletRequest("1", 250);
        Long customerId = 1L;
        Customer mockCustomer = Customer.create(customerId, "황지수");
        Wallet mockWallet = Wallet.create(mockCustomer, 10000000);

        // Mock 설정: walletService.getWalletByCustomerId()가 정상적인 Wallet을 반환
        when(walletService.getWalletByCustomerId(customerId)).thenReturn(mockWallet);

        // Mock 설정: walletService.chargeWallet()이 예외를 던지도록 설정
        when(walletService.chargeWallet(mockWallet, request.chargeAmount()))
                .thenThrow(new BaseCustomException(BaseErrorCode.INVALID_PARAMETER));

        // When & Then: 예외가 발생해야 함
        assertThrows(BaseCustomException.class, () -> chargeWalletUseCase.chargeWallet(request));

        // Verify: walletService.getWalletByCustomerId()가 정확히 한 번 호출되었는지 검증
        verify(walletService, times(1)).getWalletByCustomerId(customerId);
        // Verify: walletService.chargeWallet()이 정확히 한 번 호출되었는지 검증
        verify(walletService, times(1)).chargeWallet(mockWallet, request.chargeAmount());
    }
}
