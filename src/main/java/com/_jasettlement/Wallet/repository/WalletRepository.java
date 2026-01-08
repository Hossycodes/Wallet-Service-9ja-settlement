package com._jasettlement.Wallet.repository;

import com._jasettlement.Wallet.entity.Wallet;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {

    Optional<Wallet> findByEmail(String email);

    Optional<Wallet> findByWalletId(@NotBlank(message = "Wallet ID is required") String walletId);
}
