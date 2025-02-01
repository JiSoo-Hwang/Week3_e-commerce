package kr.jsh.ecommerce.interfaces.api.statistics.dto;

public record ProductSalesResponse(
        Long fruitId,
        String fruitName,
        Integer totalQuantity
) {
    public static ProductSalesResponse from(ProductSalesDTO dto){
        return new ProductSalesResponse(dto.getProductId(),dto.getProductName(),dto.getTotalQuantity());
    }
}
