package kr.jsh.ecommerce.interfaces.api.wallet.dto;

public record ChargeWalletRequest(
        String customerId,
        int chargeAmount
) {
}
