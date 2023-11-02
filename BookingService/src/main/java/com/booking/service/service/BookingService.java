package com.booking.service.service;

import com.booking.service.dto.request.TransactionRequest;
import com.booking.service.dto.response.GetAllTransactionsResponse;
import com.booking.service.dto.response.ResponseModel;
import com.booking.service.dto.response.SumResponse;
import com.booking.service.dto.response.TransactionResponseDTO;

import java.util.List;


public interface BookingService {
    ResponseModel<?> createTransaction(TransactionRequest transactionRequest, Integer transactionId);
    ResponseModel<List<Integer>> getTransactionIdList(String type);
    ResponseModel<List<String>> getAllCurrencies();
    ResponseModel<SumResponse> findSumOfTransactions(Integer transactionId);
    ResponseModel<TransactionResponseDTO> getTransactionById(Integer transactionId);
    ResponseModel<GetAllTransactionsResponse> getAllTransactions(Integer pageNumber, Integer pageSize);

    ResponseModel<?> deleteTransaction(Integer transactionId);
}
