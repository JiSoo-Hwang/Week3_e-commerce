package kr.jsh.ecommerce.product.application;

import kr.jsh.ecommerce.product.domain.ProductRepository;
import kr.jsh.ecommerce.product.domain.ProductStatus;
import kr.jsh.ecommerce.product.presentation.dto.ProductInfoResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<ProductInfoResponse> getInStockProducts(){
        return productRepository.getInStockProducts(ProductStatus.IN_STOCK).stream()
                .map(product -> new ProductInfoResponse(
                        product.getProductId(),
                        product.getProductName(),
                        product.getProductCategory(),
                        product.getProductPrice(),
                        product.getProductStatus()
                ))
                .collect(Collectors.toList());
    }
}
