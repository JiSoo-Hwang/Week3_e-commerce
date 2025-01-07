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
    @JoinColumn(name = "PRODUCT_ID",nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SIZE_ID",nullable = false)
    private ProductSize productSize;

    @Column(nullable = false)
    private int productQuantity;

    protected Stock(){}

    public Stock(Product product, ProductSize productSize,int productQuantity){
        this.product=product;
        this.productSize=productSize;
        this.productQuantity=productQuantity;
    }

    public void setProduct(Product product){
        this.product=product;
    }

    public void addQuantity(int quantityToAdd){
        this.productQuantity+=quantityToAdd;
    }

    public void removeQuanity(int quantityToRemove){
        if(this.productQuantity<quantityToRemove){
            throw new IllegalArgumentException("재고가 부족합니다.");//커스텀
        }
        this.productQuantity-=quantityToRemove;
    }

}
