package kr.jsh.ecommerce.product.application;

import kr.jsh.ecommerce.product.domain.ProductRepository;
import kr.jsh.ecommerce.product.presentation.dto.ProductListInfoResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<ProductListInfoResponse> getInStockProducts(){
        return productRepository.getInStockProducts().stream()
                .map(product -> new ProductListInfoResponse(
                        product.getProductId(),
                        product.getProductName(),
                        product.getBrand(),
                        product.getProductPrice()
                ))
                .collect(Collectors.toList());
    }
}
