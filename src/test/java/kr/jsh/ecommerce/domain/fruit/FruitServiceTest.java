package kr.jsh.ecommerce.domain.fruit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class FruitServiceTest {

    private FruitService fruitService;
    private FruitRepository fruitRepository;

    private Fruit fruitA;
    private Fruit fruitB;

    @BeforeEach
    void setUp(){
        fruitRepository = mock(FruitRepository.class);

    }

}
