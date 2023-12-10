package com.mirna.transferapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mirna.transferapi.domain.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
