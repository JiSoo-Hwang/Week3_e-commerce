package kr.jsh.ecommerce.interfaces.dto.wallet;

public record ChargeWalletRequest(
        String customerId,
        int chargeAmount
) {
}
