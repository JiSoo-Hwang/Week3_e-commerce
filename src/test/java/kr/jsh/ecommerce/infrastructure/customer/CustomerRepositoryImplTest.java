package kr.jsh.ecommerce.infrastructure.customer;

import kr.jsh.ecommerce.domain.customer.Customer;
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
public class CustomerRepositoryImplTest {

    @Mock //JPA Repository를 Mock으로 설정
    private CustomerJpaRepository customerJpaRepository;

    @InjectMocks // CustomerRepositoryImpl에 Mock 객체 주입
    private CustomerRepositoryImpl customerRepository;

    @Test
    @DisplayName("findById()는 존재하는 고객을 반환해야 한다")
    void findById_success(){
        //Given
        Customer mockCustomer = Customer.create(7L,"우효진");
        when(customerJpaRepository.findById(7L)).thenReturn(Optional.of(mockCustomer));

        //When
        Optional<Customer>result = customerRepository.findById(7L);

        //Then
        assertThat(result).isPresent();
        assertThat(result.get().getCustomerName()).isEqualTo("우효진");

        //Verify
        verify(customerJpaRepository,times(1)).findById(7L);
    }

    @Test
    @DisplayName("save()는 고객을 저장하고 반환해야 한다")
    void save_success(){
        //Given
        Customer mockCustomer = Customer.create(8L,"박정환");
        when(customerJpaRepository.save(any(Customer.class))).thenReturn(mockCustomer);

        //When
        Customer savedCustomer = customerRepository.save(mockCustomer);

        //Then
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getCustomerName()).isEqualTo("박정환");

        //Verify
        verify(customerJpaRepository,times(1)).save(any(Customer.class));

    }
}
