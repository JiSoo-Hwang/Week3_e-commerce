package kr.jsh.ecommerce.product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@Table(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "PRODUCT_NAME", nullable = false)
    private String productName;

    @Column(name = "PRODUCT_CATEGORY", nullable = false)
    private String productCategory;

    @Column(name = "PRODUCT_PRICE", nullable = false)
    private double productPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRODUCT_STATUS", nullable = false)
    private ProductStatus productStatus;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductSize> sizes = new ArrayList<>();

    public Product(String productName, String productCategory,double productPrice){
        this.productName=productName;
        this.productCategory=productCategory;
        this.productPrice=productPrice;
        this.productStatus=ProductStatus.OUT_OF_STOCK;//초기 상품은 품절 상태
    }

    //연관관계를 한쪽에서만 관리하고, 예측 가능한 코드 작성을 위함_1
    public void addsize(ProductSize productSize){
        sizes.add(productSize);
        productSize.setProduct(this);
        updateStatus(); //상품 상태 업데이트
    }
    //연관관계를 한쪽에서만 관리하고, 예측 가능한 코드 작성을 위함_2
    public void removeSize(ProductSize productSize){
        sizes.remove(productSize);
        productSize.setProduct(null);
        updateStatus(); //상품 상태 업데이트
    }
    public void updateStatus(){
        boolean hasStock = sizes.stream().anyMatch(productSize -> productSize.getStockQuantity()>0);
        this.productStatus=hasStock?ProductStatus.IN_STOCK:ProductStatus.OUT_OF_STOCK;
    }
}
