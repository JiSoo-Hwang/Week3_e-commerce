package kr.jsh.ecommerce.interfaces.api.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // 기본 생성자 필수!
@AllArgsConstructor
public class ProductSalesDTO {
    private Long productId;
    private String productName;
    private int totalQuantity;

    //  JPQL에서 사용할 생성자 명시
    public ProductSalesDTO(Long productId, String productName, Long totalQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.totalQuantity = totalQuantity.intValue(); // JPQL SUM() 결과를 int로 변환
    }
}
