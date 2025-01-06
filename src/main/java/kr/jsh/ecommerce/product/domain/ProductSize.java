package kr.jsh.ecommerce.product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProductSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sizeId;

    private int size;

    private int stockQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductSize(int size, int stockQuantity){
        this.size = size;
        this.stockQuantity = stockQuantity;
    }

    public void changeStockQuantity(int newStockQuantity){
        this.stockQuantity = newStockQuantity;
        if(product != null){
            product.updateStatus(); //Product 상태 업데이트
        }
    }
}
