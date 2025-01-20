package kr.jsh.ecommerce.order.presentation.dto;

public record OrderRequest(
        Long productId,
        int productSize,
        int productQuantity
) {

}
