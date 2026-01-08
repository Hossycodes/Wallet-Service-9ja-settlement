package com._jasettlement.Wallet.dto.request;
import com._jasettlement.Wallet.entity.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DepositRequest {
    @NotBlank(message = "Wallet ID is required")
    private String walletId;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be positive")
    private Long amountMinorUnits;

    private String description;
}
