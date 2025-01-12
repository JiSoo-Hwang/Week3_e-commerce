package kr.jsh.ecommerce.domain.fruit;

import jakarta.persistence.*;
import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import kr.jsh.ecommerce.base.entity.BaseEntity;
import kr.jsh.ecommerce.base.exception.BaseCustomException;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 기본 생성자
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Builder용 생성자
@Table(name = "fruit")
public class Fruit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fruitName;

    @Column(nullable = false)
    private int fruitStock;

    @Column(nullable = false)
    private int fruitPrice;

    @Column(nullable = false)
    private String status;

    //재고 감소
    private void deductStock(int quantity){
        if(quantity>this.fruitStock){
            throw new BaseCustomException(BaseErrorCode.OUT_OF_STOCK,new String[]{this.fruitName});
        }
        this.fruitStock -= quantity;
    }

    //재고 복구
    public void restoreStock(int quantity){
        //음수 입력 방지
        if(quantity<0){
            throw new BaseCustomException(BaseErrorCode.INVALID_PARAMETER,new String[]{String.valueOf(quantity)});
        }
        this.fruitStock+=quantity;
    }
}
