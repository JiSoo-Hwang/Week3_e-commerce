package kr.jsh.ecommerce.infrastructure.wallet;

import kr.jsh.ecommerce.domain.customer.Customer;
import kr.jsh.ecommerce.domain.wallet.Wallet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletRepositoryImplTest {

    @Mock
    private WalletJpaRepository walletJpaRepository;

    @InjectMocks
    private WalletRepositoryImpl walletRepository;

    @Test
    @DisplayName("findByCustomerId()는 존재하는 지갑을 반환해야 한다")
    void findByCustomerId_success(){
        //Given
        Customer mockCustomer = Customer.create(1L,"최재영");
        Wallet mockWallet = Wallet.create(mockCustomer,10000000);

        when(walletJpaRepository.findByCustomerId(1L)).thenReturn(Optional.of(mockWallet));

        //When
        Optional<Wallet> result = walletRepository.findByCustomerId(1L);

        //Then
        assertThat(result).isPresent();
        assertThat(result.get().getBalance()).isEqualTo(10000000);

        //Verify
        verify(walletJpaRepository,times(1)).findByCustomerId(1L);
    }

    @Test
    @DisplayName("save()는 지갑을 저장하고 반환해야 한다")
    void save_success(){
        //Given
        Customer mockCustomer = Customer.create(1L,"최재영");
        Wallet mockWallet = Wallet.create(mockCustomer,10000000);
        when(walletJpaRepository.save(any(Wallet.class))).thenReturn(mockWallet);

        //When
        Wallet savedWallet = walletRepository.save(mockWallet);

        //Then
        assertThat(savedWallet).isNotNull();
        assertThat(savedWallet.getBalance()).isEqualTo(10000000);

        //Verify
        verify(walletJpaRepository,times(1)).save(any(Wallet.class));
    }
}
