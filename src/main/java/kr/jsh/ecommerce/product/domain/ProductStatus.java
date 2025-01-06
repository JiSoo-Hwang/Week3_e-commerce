package kr.jsh.ecommerce.product.domain;

public enum ProductStatus {
    IN_STOCK("재고 있음"),
    OUT_OF_STOCK("일시 품절"),
    DISCONTINUED("개시 종료");

    private final String description;

    ProductStatus(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
