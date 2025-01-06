package kr.jsh.ecommerce.product.domain;

import java.util.List;

public interface ProductRepository {

    List<Product> getInStockProducts(ProductStatus productStatus);
}
