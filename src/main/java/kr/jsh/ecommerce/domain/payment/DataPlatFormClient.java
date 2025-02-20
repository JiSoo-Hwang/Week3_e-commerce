package kr.jsh.ecommerce.domain.payment;

public interface DataPlatFormClient {
    void sendOrderData(Long orderId);
}
