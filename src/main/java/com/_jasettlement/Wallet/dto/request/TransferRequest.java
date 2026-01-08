package com._jasettlement.Wallet.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransferRequest {

    @NotBlank(message = "From wallet ID is required")
    private String fromWalletId;

    @NotBlank(message = "To wallet ID is required")
    private String toWalletId;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be positive")
    private Long amountMinorUnits;

    private String description;
}
