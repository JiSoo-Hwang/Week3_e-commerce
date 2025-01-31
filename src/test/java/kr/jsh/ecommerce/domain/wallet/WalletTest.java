package kr.jsh.ecommerce.domain.wallet;

import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.customer.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Wallet 엔터티 테스트")
public class WalletTest {

    @Test
    @DisplayName("Wallet 생성이 정상적으로 이루어져야 한다")
    void createWallet_success(){
        //Given
        Customer mockCustomer=Customer.create(1L, "김선경");

        //When
        Wallet wallet = Wallet.create(mockCustomer,5000000);

        //Then
        assertThat(wallet).isNotNull();
        assertThat(wallet.getCustomer()).isEqualTo(mockCustomer);
        assertThat(wallet.getBalance()).isEqualTo(5000000);
    }

    @Test
    @DisplayName("금액 충전이 정상적으로 이루어져야 한다")
    void chargeCash_success(){
        //Given
        Customer mockCustomer = Customer.create(1L,"김선경");
        Wallet wallet = Wallet.create(mockCustomer,5000000);

        //When
        wallet.chargeCash(2000000);

        //Then
        assertThat(wallet.getBalance()).isEqualTo(7000000);
    }

    @Test
    @DisplayName("출금이 정상적으로 이루어져야 한다")
    void spendCash_success(){
        //Given
        Customer mockCustomer = Customer.create(1L,"김선경");
        Wallet wallet = Wallet.create(mockCustomer,5000000);

        //When
        wallet.spendCash(2000000);

        //Then
        assertThat(wallet.getBalance()).isEqualTo(3000000);
    }

    @Test
    @DisplayName("100원 단위가 아닌 금액을 충전하면 예외가 발생해야 한다")
    void chargeCash_invalidAmount(){
        //Given
        Customer mockCustomer = Customer.create(1L,"김선경");
        Wallet wallet = Wallet.create(mockCustomer,5000000);

        //When & Then
        assertThrows(BaseCustomException.class,()->wallet.chargeCash(250));
    }

    @Test
    @DisplayName("100원 단위가 아닌 금액을 출금하면 예외가 발생해야 한다")
    void spendCash_invalidAmount(){
        //Given
        Customer mockCustomer = Customer.create(1L,"김선경");
        Wallet wallet = Wallet.create(mockCustomer,5000000);

        //When & Then
        assertThrows(BaseCustomException.class,()->wallet.spendCash(250));
    }

    @Test
    @DisplayName("음수 금액을 충전하면 예외가 발생해야 한다")
    void chargeCash_negativeAmount(){
        //Given
        Customer mockCustomer = Customer.create(1L,"김선경");
        Wallet wallet = Wallet.create(mockCustomer,5000000);

        //When & Then
        assertThrows(BaseCustomException.class,()->wallet.chargeCash(-1000000));
    }

    @Test
    @DisplayName("잔액보다 큰 금액을 출금하면 예외가 발생해야 한다")
    void spendCash_insufficientFunds(){
        //Given
        Customer mockCustomer = Customer.create(1L,"김선경");
        Wallet wallet = Wallet.create(mockCustomer,5000000);

        //When & Then
        assertThrows(BaseCustomException.class,()->wallet.spendCash(10000000));
    }
}
