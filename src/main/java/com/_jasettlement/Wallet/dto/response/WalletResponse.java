package com._jasettlement.Wallet.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WalletResponse {
    private String id;
    private String walletId;
    private String ownerName;
    private Long balanceMinorUnits;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
