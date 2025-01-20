package kr.jsh.ecommerce.product.presentation.dto;


public record ProductListInfoResponse(
        long productId,
        String productName,
        String brand,
        int productPrice
) {
}