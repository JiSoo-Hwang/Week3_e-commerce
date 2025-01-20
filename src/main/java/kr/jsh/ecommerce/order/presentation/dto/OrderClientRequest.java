package kr.jsh.ecommerce.order.presentation.dto;

import kr.jsh.ecommerce.customer.domain.Customer;

import java.util.List;

public record OrderClientRequest(
        Customer customer, List<OrderRequest> orderRequests
) {
}
