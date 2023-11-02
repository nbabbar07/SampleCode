package com.booking.service.repository;

import com.booking.service.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity,Integer>{

    @Query(value = "SELECT transaction_id FROM transactions where type= ?1", nativeQuery = true)
    List<Integer> findTransactionIdByType(String type);

    @Query(value = "SELECT DISTINCT currency FROM transactions", nativeQuery = true)
    List<String> findAllCurrencies();

    @Query(value = "SELECT EXISTS(SELECT 1 FROM transactions WHERE transaction_id = ?1);", nativeQuery = true)
    Boolean checkTransactionExists(int transactionId);
}
