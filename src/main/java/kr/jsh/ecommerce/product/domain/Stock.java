package kr.jsh.ecommerce.product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.engine.jdbc.Size;

@Entity
@Getter
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @Embedded
    private ProductSize productSize;

    @Column(nullable = false)
    private int productQuantity;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    protected Stock() {
    }

    public Stock(Product product, ProductSize productSize, int productQuantity,ProductStatus productStatus) {
        this.product = product;
        this.productSize = productSize;
        this.productQuantity = productQuantity;
        this.productStatus = (productQuantity > 0) ? ProductStatus.IN_STOCK : ProductStatus.OUT_OF_STOCK;
    }

    public Product getProduct(){
        return product;
    }

    public void setProduct(Product product){
        this.product=product;
    }

    public void updateQuantity(int productQuantity, OperationType operationType) {
        if (operationType == OperationType.ADD) {
            this.productQuantity += productQuantity;
        } else if (operationType == OperationType.REMOVE) {
            if (this.productQuantity < productQuantity) {
                throw new IllegalArgumentException("재고가 부족합니다");//커스텀예정
            }
            this.productQuantity -= productQuantity;
        }
        this.productStatus = (this.productQuantity > 0) ? ProductStatus.IN_STOCK : ProductStatus.OUT_OF_STOCK;
    }
}
