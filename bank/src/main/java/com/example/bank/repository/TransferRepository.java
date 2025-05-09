package com.example.bank.repository;

import com.example.bank.model.Account;
import com.example.bank.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findBySourceAccount(Account account);
    List<Transfer> findByDestinationAccount(Account account);
    List<Transfer> findBySourceAccountAndBankingDateTimeBetween(Account account, LocalDateTime start, LocalDateTime end);
    List<Transfer> findByDestinationAccountAndBankingDateTimeBetween(Account account, LocalDateTime start, LocalDateTime end);
}
