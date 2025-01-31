package kr.jsh.ecommerce.domain.fruit;

import jakarta.persistence.*;
import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.entity.BaseEntity;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import kr.jsh.ecommerce.domain.order.OrderFruit;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 기본 생성자
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Builder용 생성자
@Table(name = "fruit")
public class Fruit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fruitId;

    @Column(nullable = false)
    private String fruitName;

    @Column(nullable = false)
    private int fruitStock;

    @Column(nullable = false)
    private int fruitPrice;

    //TODO:하드코딩된 상태값 StockStatus로 바꾸기
    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "fruit",cascade = CascadeType.ALL,orphanRemoval = false)
    private List<OrderFruit> orderFruits;

    public static Fruit create(String fruitName, int fruitPrice, int fruitStock, String status) {
        Fruit fruit = new Fruit();
        fruit.fruitName = fruitName;
        fruit.fruitPrice = fruitPrice;
        fruit.fruitStock = fruitStock;
        fruit.status = status;
        return fruit;
    }

    //재고 감소
    public void deductStock(int quantity){
        if(quantity>this.fruitStock){
            throw new BaseCustomException(BaseErrorCode.OUT_OF_STOCK,new String[]{this.fruitName});
        }
        this.fruitStock -= quantity;
    }

    //재고 채우기
    public void restoreStock(int quantity){
        //음수 입력 방지
        if(quantity<0){
            throw new BaseCustomException(BaseErrorCode.INVALID_PARAMETER,new String[]{String.valueOf(quantity)});
        }
        this.fruitStock+=quantity;
    }
}
