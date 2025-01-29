package kr.jsh.ecommerce.infrastructure.order;

import kr.jsh.ecommerce.domain.order.Order;
import org.assertj.core.api.Assertions;
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
public class OrderRepositoryImplTest {

    @Mock
    private OrderJpaRepository orderJpaRepository;

    @InjectMocks
    private OrderRepositoryImpl orderRepository;

    @Test
    @DisplayName("findById()는 존재하는 주문을 반환해야 한다")
    void findById_success(){
        //Given
        Order mockOrder = Order.builder()
                .orderId(300L)
                .totalAmount(15000)
                .build();

        when(orderJpaRepository.findById(300L)).thenReturn(Optional.of(mockOrder));

        //When
        Optional<Order> result = orderRepository.findById(300L);

        //Then
        assertThat(result).isPresent();
        assertThat(result.get().getTotalAmount()).isEqualTo(15000);

        //Verify
        verify(orderJpaRepository,times(1)).findById(300L);
    }

    @Test
    @DisplayName("save()는 주문을 저장하고 반환해야 한다")
    void save_success(){
        //Given
        Order mockOrder = Order.builder()
                .orderId(500L)
                .totalAmount(50000)
                .build();

        when(orderJpaRepository.save(any(Order.class))).thenReturn(mockOrder);

        //When
        Order savedOrder = orderRepository.save(mockOrder);

        //Then
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getTotalAmount()).isEqualTo(50000);

        //Verify
        verify(orderJpaRepository,times(1)).save(any(Order.class));
    }
}
