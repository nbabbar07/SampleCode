package com.booking.service.service.impl;

import com.booking.service.dto.response.GetAllTransactionsResponse;
import com.booking.service.dto.response.SumResponse;
import com.booking.service.dto.request.TransactionRequest;
import com.booking.service.dto.response.TransactionResponseDTO;
import com.booking.service.entity.TransactionEntity;
import com.booking.service.exception.CustomException;
import com.booking.service.repository.TransactionRepository;
import com.booking.service.service.BookingService;
import com.booking.service.dto.response.ResponseModel;
import com.booking.service.util.Constants;
import com.booking.service.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private TransactionRepository transactionRepository;


    @Override
    public ResponseModel createTransaction(TransactionRequest transactionRequest, Integer transactionId) {
        Boolean transactionExists = transactionRepository.checkTransactionExists(transactionId);
        if(!transactionExists){
            TransactionEntity transactionEntity = TransactionEntity.builder()
                    .transactionId(transactionId)
                    .amount(transactionRequest.getAmount())
                    .type(transactionRequest.getType())
                    .currency(transactionRequest.getCurrency())
                    .build();

            if(Objects.nonNull(transactionRequest.getParent())){
                Optional<TransactionEntity> parentTransaction = transactionRepository.findById(transactionRequest.getParent());

                if(parentTransaction.isEmpty()){
                    throw new CustomException(Constants.INVALID_PARENT_ID,HttpStatus.BAD_REQUEST);
                }

                TransactionEntity transaction = parentTransaction.get();

                if(transaction.getCurrency().equals(transactionEntity.getCurrency())){
                    transactionEntity.setParentTransaction(transaction);
                }
                else{
                    throw new CustomException(Constants.PARENT_CHILD_CURRENCY_MISMATCH,HttpStatus.BAD_REQUEST);
                }
            }

            transactionRepository.save(transactionEntity);

            return new ResponseModel(null,Constants.TRANSACTION_CREATED);
        }
        else{
            throw new CustomException(Constants.transactionExists(transactionId),HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseModel<List<Integer>> getTransactionIdList(String type) {
        String transactionType = type.toLowerCase();

        List<Integer> listOfTransactionId = transactionRepository.findTransactionIdByType(transactionType);

        if(listOfTransactionId.isEmpty()){
           throw new CustomException(Constants.noTransactionTypes(type),HttpStatus.NOT_FOUND);
        }
        return new ResponseModel(listOfTransactionId,Constants.transactionTypes(transactionType));
    }

    @Override
    public ResponseModel<List<String>> getAllCurrencies() {
        List<String> listOfCurrencies = transactionRepository.findAllCurrencies();

        if(listOfCurrencies.isEmpty()){
            throw new CustomException(Constants.CURRENCY_NOT_FOUND,HttpStatus.NOT_FOUND);
        }
        return new ResponseModel(listOfCurrencies,Constants.ALL_CURRENCIES_USED_IN_TRANSACTION);
    }

    @Override
    public ResponseModel<SumResponse> findSumOfTransactions(Integer transactionId) {

        Optional<TransactionEntity> savedTransaction = transactionRepository.findById(transactionId);
        if(savedTransaction.isEmpty()){
            throw new CustomException(Constants.transactionDoesNotExists(transactionId),HttpStatus.NOT_FOUND);
        }

        TransactionEntity transactionEntity = savedTransaction.get();

        Set<TransactionEntity> childData =  transactionEntity.getSubTransactions();

        double sum  = transactionEntity.getAmount();

        for (TransactionEntity data : childData) {
            sum += data.getAmount();
        }

        SumResponse returnValue = SumResponse.builder()
                .sum(sum)
                .currency(transactionEntity.getCurrency())
                .build();

        return new ResponseModel(returnValue,Constants.SUM_OF_PARENT_AND_CHILD_TRANSACTIONS);
    }

    @Override
    public ResponseModel<TransactionResponseDTO> getTransactionById(Integer transactionId) {

        Optional<TransactionEntity> savedTransaction = transactionRepository.findById(transactionId);

        if(savedTransaction.isEmpty()){
            throw new CustomException(Constants.transactionDoesNotExists(transactionId),HttpStatus.NOT_FOUND);
        }

        TransactionEntity transactionEntity = savedTransaction.get();

        return new ResponseModel<>(MapperUtils.toTransactionDTO(transactionEntity),Constants.TRANSACTION_FETCHED_SUCCESSFULLY);
    }

    @Override
    public ResponseModel<GetAllTransactionsResponse> getAllTransactions(Integer page, Integer size) {
        List<TransactionResponseDTO> allTransactions = new ArrayList<>();

        Pageable pageable = PageRequest.of(page, size);

        Page<TransactionEntity> content = transactionRepository.findAll(pageable);

        List<TransactionEntity> transactions = content.getContent();

        if(transactions.isEmpty()) throw new CustomException(Constants.NO_TRANSACTION_FOUND,HttpStatus.NO_CONTENT);

        transactions.forEach(e -> {
            allTransactions.add(MapperUtils.toTransactionDTO(e));
        });

        GetAllTransactionsResponse response = GetAllTransactionsResponse.builder()
                .transactions(allTransactions)
                .count(content.getTotalElements())
                .totalPages(content.getTotalPages()).build();

        return new ResponseModel<>(response,Constants.ALL_TRANSACTIONS_FETCHED);
    }
    @Override
    public ResponseModel<?> deleteTransaction(Integer transactionId) {
        Boolean aBoolean = transactionRepository.checkTransactionExists(transactionId);
        if(!aBoolean) throw new CustomException(Constants.transactionDoesNotExists(transactionId),HttpStatus.NOT_FOUND);

        transactionRepository.deleteById(transactionId);
        return new ResponseModel<>(null,Constants.DELETED_SUCCESFULLY);
    }
}
