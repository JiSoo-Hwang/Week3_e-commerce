package kr.jsh.ecommerce.product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "PRODUCT_NAME", nullable = false)
    private String productName;

    @Column(name = "PRODUCT_PRICE", nullable = false)
    private int productPrice;

    @Column(name = "BRAND", nullable = false)
    private String brand;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Stock> stock = new ArrayList<>();

    protected Product(){}

    public Product(String productName,int productPrice,String brand){
        this.productName=productName;
        this.productPrice=productPrice;
        this.brand=brand;
    }

    public void addStock(Stock stock){
        this.stock.add(stock);
        stock.setProduct(this);
    }
}
