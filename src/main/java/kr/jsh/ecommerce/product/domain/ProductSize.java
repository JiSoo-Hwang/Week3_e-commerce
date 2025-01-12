package kr.jsh.ecommerce.product.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Embeddable
@Getter
public class ProductSize {

    @Column(name = "PRODUCT_SIZE", nullable = false)
    private int productSize;

    protected ProductSize(){}

    public ProductSize(int productSize){
        this.productSize=productSize;
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(obj==null||getClass()!=obj.getClass()) return false;
        ProductSize productSize1 = (ProductSize) obj;
        return productSize == productSize1.productSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productSize);
    }
}
