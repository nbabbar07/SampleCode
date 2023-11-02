package com.booking.service.util;

import com.booking.service.dto.response.TransactionResponseDTO;
import com.booking.service.entity.TransactionEntity;

public class MapperUtils {
    public static TransactionResponseDTO toTransactionDTO(TransactionEntity transactionEntity){
        return TransactionResponseDTO.builder()
                .transactionId(transactionEntity.getTransactionId())
                .type(transactionEntity.getType())
                .amount(transactionEntity.getAmount())
                .currency(transactionEntity.getCurrency())
                .build();
    }
}
