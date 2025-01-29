package kr.jsh.ecommerce.interfaces.api.wallet.dto;

import kr.jsh.ecommerce.domain.wallet.Wallet;

public record WalletResponse(
        int balance
) {
    public static WalletResponse from(Wallet wallet) { // ✅ 정적 팩토리 메서드 추가
        return new WalletResponse(wallet.getBalance());
    }
}
