package com._jasettlement.Wallet.dto.response;

import com._jasettlement.Wallet.entity.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class TransactionResponse {
    private String id;
    private String reference;
    private String walletId;
    private TransactionType type;
    private Long amountMinorUnits;
    private String currency;
    private String description;
    private Long balanceAfterMinorUnits;
    private LocalDateTime createdAt;
}
