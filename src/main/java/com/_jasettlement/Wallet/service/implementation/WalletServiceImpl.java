package com._jasettlement.Wallet.service.implementation;

import com._jasettlement.Wallet.dto.request.CreateWalletRequest;
import com._jasettlement.Wallet.dto.request.DepositRequest;
import com._jasettlement.Wallet.dto.request.TransferRequest;
import com._jasettlement.Wallet.dto.request.WithdrawalRequest;
import com._jasettlement.Wallet.dto.response.TransactionResponse;
import com._jasettlement.Wallet.dto.response.WalletResponse;
import com._jasettlement.Wallet.entity.Transaction;
import com._jasettlement.Wallet.entity.TransactionType;
import com._jasettlement.Wallet.entity.Wallet;
import com._jasettlement.Wallet.exception.DuplicateEmailException;
import com._jasettlement.Wallet.exception.InsufficientFundsException;
import com._jasettlement.Wallet.exception.WalletNotFoundException;
import com._jasettlement.Wallet.repository.TransactionRepository;
import com._jasettlement.Wallet.repository.WalletRepository;
import com._jasettlement.Wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {


    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;


    @Override
    public WalletResponse createWallet(CreateWalletRequest request) {
        // Check if email already exists
        if (walletRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Wallet already exists for email: " + request.getEmail());
        }

        Wallet wallet = Wallet.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .bvn(request.getBvn())
                .build();

        wallet = walletRepository.save(wallet);
        log.info("Created wallet: {} with ID: {}", wallet.getWalletId(), wallet.getId());

        return mapToWalletResponse(wallet);
    }

    @Override
    @Transactional
    public TransactionResponse deposit(DepositRequest request) {

        String reference = UUID.randomUUID().toString();

        var existingTransaction = transactionRepository.findByReference(reference);
        if (existingTransaction.isPresent()) {
            log.warn("Duplicate transaction detected: {}", reference);
            return mapToTransactionResponse(existingTransaction.get());
        }

        // Verify wallet exists
        Wallet wallet = walletRepository.findByWalletId(request.getWalletId())
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found: " + request.getWalletId()));

        Transaction transaction = Transaction.builder()
                .reference(reference)
                .walletId(request.getWalletId())
                .type(TransactionType.CREDIT)
                .amountMinorUnits(request.getAmountMinorUnits())
                .description(request.getDescription())
                .build();

        transaction = transactionRepository.save(transaction);
        log.info("Processed CREDIT transaction: {} for wallet: {}", transaction.getId(), wallet.getWalletId());

        return mapToTransactionResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(WithdrawalRequest request) {

        String reference = UUID.randomUUID().toString();

        // Verify wallet exists
        Wallet wallet = walletRepository.findByWalletId(request.getWalletId())
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found: " + request.getWalletId()));

        // Check sufficient funds
        Long currentBalance = transactionRepository.calculateBalance(request.getWalletId());
        if (currentBalance < request.getAmountMinorUnits()) {
            throw new InsufficientFundsException("Insufficient funds. Available: " + currentBalance + ", Required: " + request.getAmountMinorUnits());
        }

        Transaction transaction = Transaction.builder()
                .reference(reference)
                .walletId(request.getWalletId())
                .type(TransactionType.DEBIT)
                .amountMinorUnits(request.getAmountMinorUnits())
                .description(request.getDescription())
                .build();

        transaction = transactionRepository.save(transaction);
        log.info("Processed DEBIT transaction: {} for wallet: {}", transaction.getId(), wallet.getWalletId());

        return mapToTransactionResponse(transaction);
    }



    @Override
    @Transactional
    public TransactionResponse transfer(TransferRequest request) {

        String reference = UUID.randomUUID().toString();

        // Verify both wallets exist
        Wallet fromWallet = walletRepository.findByWalletId(request.getFromWalletId())
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found: " + request.getFromWalletId()));

        Wallet toWallet = walletRepository.findByWalletId(request.getToWalletId())
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found: " + request.getToWalletId()));

        // Check sufficient funds
        Long fromWalletBalance = transactionRepository.calculateBalance(request.getFromWalletId());
        if (fromWalletBalance < request.getAmountMinorUnits()) {
            throw new InsufficientFundsException("Insufficient funds. Available: " + fromWalletBalance + ", Required: " + request.getAmountMinorUnits());
        }

        Transaction debitTxn = Transaction.builder()
                .reference(reference + "_debit")
                .walletId(request.getFromWalletId())
                .type(TransactionType.TRANSFER_OUT)
                .amountMinorUnits(request.getAmountMinorUnits())
                .description(request.getDescription())
                .build();
        debitTxn = transactionRepository.save(debitTxn);

        Transaction creditTxn = Transaction.builder()
                .reference(reference + "_credit")
                .walletId(request.getToWalletId())
                .type(TransactionType.TRANSFER_IN)
                .amountMinorUnits(request.getAmountMinorUnits())
                .description(request.getDescription())
                .build();
        transactionRepository.save(creditTxn);

        log.info("Transfer completed: {} -> {} (reference: {})",
                fromWallet.getWalletId(), toWallet.getWalletId(), reference);

        return mapToTransactionResponse(debitTxn);
    }

    @Override
    @Transactional(readOnly = true)
    public WalletResponse getWallet(String walletId) {
        Wallet wallet = walletRepository.findByWalletId(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found: " + walletId));

        return mapToWalletResponse(wallet);
    }



    private WalletResponse mapToWalletResponse(Wallet wallet) {
        Long balance = transactionRepository.calculateBalance(wallet.getId());
        return WalletResponse.builder()
                .id(wallet.getId())
                .createdAt(LocalDateTime.now())
                .walletId(wallet.getWalletId())
                .ownerName(wallet.getFullName())
                .balanceMinorUnits(balance)
                .currency("NGN")
                .updatedAt(wallet.getUpdatedAt())
                .build();
    }


    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .reference(transaction.getReference())
                .walletId(transaction.getWalletId())
                .type(transaction.getType())
                .amountMinorUnits(transaction.getAmountMinorUnits())
                .currency("NGN")
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

}
