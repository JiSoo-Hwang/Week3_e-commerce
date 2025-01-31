package kr.jsh.ecommerce.application.wallet;

import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.wallet.Wallet;
import kr.jsh.ecommerce.domain.wallet.WalletService;
import kr.jsh.ecommerce.interfaces.api.wallet.dto.WalletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetWalletUseCase 단위테스트")
public class GetWalletUseCaseTest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private GetWalletUseCase getWalletUseCase;

    private WalletResponse walletResponse;

    @Test
    @DisplayName("고객 ID로 지갑을 조회하면 올바른 잔액을 반환해야 한다")
    void getWalletByCustomerId_success(){
        //Given:테스트용 고객 ID & Mock Wallet 객체 생성
        Long customerId = 3L;
        Customer mockCustomer = Customer.create(customerId,"임기원");
        Wallet mockWallet = Wallet.create(mockCustomer,100000);

        //Mock 설정 : walletService.getWalletByCustomerId() 호출 시 mockWallet 반환하도록 설정
        when(walletService.getWalletByCustomerId(customerId)).thenReturn(mockWallet);

        //When : UseCase 실행
        WalletResponse response = getWalletUseCase.getWalletByCustomerId(customerId);

        //Then : 반환된 WalletResponse가 올바른 값인지 검증
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.balance()).isEqualTo(100000);

        //Verify : walletService.getWalletByCustomerId()가 정확히 한 번 호출되었는지 확인
        verify(walletService,times(1)).getWalletByCustomerId(customerId);
    }
}
