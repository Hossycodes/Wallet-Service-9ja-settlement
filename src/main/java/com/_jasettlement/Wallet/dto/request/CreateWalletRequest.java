package com._jasettlement.Wallet.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateWalletRequest {
    @NotBlank(message = "fullName is required")
    private String fullName;

    @Email(message = "email must be valid")
    private String email;

    @NotBlank(message = "customer's bvn is required")
    @Pattern(regexp = "^\\d{10}$", message = "bvn must be exactly 10 digits")
    private String bvn;
}
