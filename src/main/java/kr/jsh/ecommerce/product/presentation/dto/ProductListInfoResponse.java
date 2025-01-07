package kr.jsh.ecommerce.product.presentation.dto;

import kr.jsh.ecommerce.product.domain.ProductStatus;

public record ProductListInfoResponse(
        long productId,
        String productName,
        String brand,
        int productPrice
) {
}