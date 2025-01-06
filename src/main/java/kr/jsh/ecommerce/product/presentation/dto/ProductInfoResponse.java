package kr.jsh.ecommerce.product.presentation.dto;

import kr.jsh.ecommerce.product.domain.ProductStatus;

public record ProductInfoResponse(
        long productId,
        String productName,
        String productCategory,
        double productPrice,
        ProductStatus productStatus
) {
}