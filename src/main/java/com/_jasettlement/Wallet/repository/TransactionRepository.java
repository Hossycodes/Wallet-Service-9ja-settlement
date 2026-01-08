package com._jasettlement.Wallet.repository;

import com._jasettlement.Wallet.entity.Transaction;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Optional<Transaction> findByReference(String reference);

    @Query("""
    SELECT COALESCE(SUM(
        CASE 
            WHEN t.type IN ('CREDIT', 'TRANSFER_IN') THEN t.amountMinorUnits
            WHEN t.type IN ('DEBIT', 'TRANSFER_OUT') THEN -t.amountMinorUnits
            ELSE 0
        END
    ), 0)
    FROM Transaction t
    WHERE t.walletId = :walletId
    """)
    Long calculateBalance(@Param("walletId") String walletId);


}
