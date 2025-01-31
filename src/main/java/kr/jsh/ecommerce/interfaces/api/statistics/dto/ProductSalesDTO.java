package kr.jsh.ecommerce.interfaces.api.statistics.dto;

public record ProductSalesDTO(
        Long productId,
        String productName,
        Integer totalQuantity
) {
}
