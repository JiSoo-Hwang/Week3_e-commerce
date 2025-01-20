package kr.jsh.ecommerce.domain.customer;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer getCustomerById(Long customerId){
        return customerRepository.findById(customerId)
                .orElseThrow(()-> new BaseCustomException(BaseErrorCode.NOT_FOUND,new String[]{String.valueOf(customerId)}));
    }
}
