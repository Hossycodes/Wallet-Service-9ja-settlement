
package com._jasettlement.Wallet.controller;

import com._jasettlement.Wallet.dto.ApiResponse;
import com._jasettlement.Wallet.dto.request.CreateWalletRequest;
import com._jasettlement.Wallet.dto.request.DepositRequest;
import com._jasettlement.Wallet.dto.request.TransferRequest;
import com._jasettlement.Wallet.dto.request.WithdrawalRequest;
import com._jasettlement.Wallet.dto.response.TransactionResponse;
import com._jasettlement.Wallet.dto.response.WalletResponse;
import com._jasettlement.Wallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
@Tag(name = "Wallet Management", description = "APIs for managing wallets and transactions")
public class WalletController {

    private final WalletService walletService;

    @Operation(
            summary = "Create a new wallet",
            description = "Creates a new wallet for a user with initial balance of 0"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Wallet created successfully",
                    content = @Content(schema = @Schema(implementation = WalletResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<WalletResponse>> createWallet(
            @Valid @RequestBody CreateWalletRequest request) {
        WalletResponse response = walletService.createWallet(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @Operation(
            summary = "Get wallet by WalletID",
            description = "Retrieves wallet details including current balance"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Wallet found",
                    content = @Content(schema = @Schema(implementation = WalletResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Wallet not found"
            )
    })
    @GetMapping("/{walletId}")
    public ResponseEntity<ApiResponse<WalletResponse>> getWallet(
            @Parameter(description = "Wallet ID", required = true)
            @PathVariable String walletId) {
        WalletResponse response = walletService.getWallet(walletId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    @Operation(
            summary = "Deposit funds",
            description = "Deposits money into a wallet. Creates a CREDIT transaction."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Deposit successful",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Wallet not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid deposit amount"
            )
    })
    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionResponse>> deposit(
            @Valid @RequestBody DepositRequest request) {
        TransactionResponse response = walletService.deposit(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Withdraw funds",
            description = "Withdraws money from a wallet. Creates a DEBIT transaction."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Withdrawal successful",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Wallet not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Insufficient funds or invalid amount"
            )
    })
    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionResponse>> withdraw(
            @Valid @RequestBody WithdrawalRequest request) {
        TransactionResponse response = walletService.withdraw(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(
            summary = "Transfer funds between wallets",
            description = "Transfers money from one wallet to another. Creates TRANSFER_OUT and TRANSFER_IN transactions."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Transfer successful",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Source or destination wallet not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Insufficient funds or invalid amount"
            )
    })
    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(
            @Valid @RequestBody TransferRequest request) {
        TransactionResponse response = walletService.transfer(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
