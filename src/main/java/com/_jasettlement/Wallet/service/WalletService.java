package com._jasettlement.Wallet.service;

import com._jasettlement.Wallet.dto.request.CreateWalletRequest;
import com._jasettlement.Wallet.dto.request.DepositRequest;
import com._jasettlement.Wallet.dto.request.TransferRequest;
import com._jasettlement.Wallet.dto.request.WithdrawalRequest;
import com._jasettlement.Wallet.dto.response.TransactionResponse;
import com._jasettlement.Wallet.dto.response.WalletResponse;

public interface WalletService {
    public WalletResponse createWallet(CreateWalletRequest request);
    public TransactionResponse deposit(DepositRequest request);
    public TransactionResponse withdraw(WithdrawalRequest request);
    public TransactionResponse transfer(TransferRequest request);
    public WalletResponse getWallet(String id);
}
