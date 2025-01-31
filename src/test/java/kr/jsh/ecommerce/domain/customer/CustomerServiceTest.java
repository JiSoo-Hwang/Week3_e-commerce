package kr.jsh.ecommerce.domain.customer;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName("findCustomerById()는 존재하는 고객을 반환해야 한다")
    void findCustomerById_success(){
        //Given : Mock 객체 설정 (ID=1L인 고객이 존재한다고 가정)
        Customer mockCustomer = Customer.create(2L,"이다은");
        when(customerRepository.findById(2L)).thenReturn(Optional.of(mockCustomer));

        //When: 기대한 값과 일치하는지 검증
        Customer customer = customerService.findCustomerById(2L);

        //Then: 기대한 값과 일치하는지 검증
        assertThat(customer).isNotNull();
        assertThat(customer.getCustomerName()).isEqualTo("이다은");

        //Verify : customerRepository.findById()가 정확히 한 번 호출되었는지 검증
        verify(customerRepository,times(1)).findById(2L);
    }

    @Test
    @DisplayName("findCustomerById()는 존재하지 않는 고객 ID를 조회할 때 예외를 던져야 한다")
    void findCustomerById_notFound(){
        //Given : Mock 객체 설정 (해당 ID가 없을 경우 Optional.empty() 반환)
        when(customerRepository.findById(3L)).thenReturn(Optional.empty());

        //When & Then : 예외 발생 검증
        BaseCustomException exception = assertThrows(BaseCustomException.class,
                ()->customerService.findCustomerById(3L));

        //예외 메시지가 올바르게 설정되었늕 확인
        assertThat(exception.getBaseErrorCode()).isEqualTo(BaseErrorCode.NOT_FOUND);
        assertThat(exception.getMsgArgs()[0]).isEqualTo("고객을 찾을 수 없습니다.");

        //Verify : customerRepository.findById()가 정확히 한 번 호출되었는지 검증
        verify(customerRepository,times(1)).findById(3L);
    }
}
