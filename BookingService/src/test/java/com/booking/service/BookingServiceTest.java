package com.booking.service;

import com.booking.service.dto.request.TransactionRequest;
import com.booking.service.dto.response.ResponseModel;
import com.booking.service.dto.response.SumResponse;
import com.booking.service.dto.response.TransactionResponseDTO;
import com.booking.service.entity.TransactionEntity;
import com.booking.service.exception.CustomException;
import com.booking.service.repository.TransactionRepository;
import com.booking.service.service.BookingService;
import com.booking.service.service.impl.BookingServiceImpl;
import com.booking.service.util.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService = new BookingServiceImpl();

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateParentTransaction(){
        int transactionId = 1;
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(22.00)
                .type("expenses")
                .currency("EUR")
                .parent(null)
                .build();


        when(transactionRepository.checkTransactionExists(anyInt())).thenReturn(false);

        ResponseModel<?> transaction = bookingService.createTransaction(transactionRequest, transactionId);

        Assertions.assertEquals(transaction.getMessage(),Constants.TRANSACTION_CREATED);
        Assertions.assertNull(transaction.getData());
    }

    @Test
    public void testCreateChildTransaction(){
        int transactionId = 1;

        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(22.00)
                .type("expenses")
                .currency("EUR")
                .parent(1)
                .build();

        TransactionEntity parent = TransactionEntity.builder()
                .transactionId(1)
                .amount(22.00)
                .subTransactions(null)
                .currency("EUR")
                .parentTransaction(null)
                .type("expenses")
                .build();

        when(transactionRepository.checkTransactionExists(anyInt())).thenReturn(false);
        when(transactionRepository.findById(anyInt())).thenReturn(Optional.of(parent));
        ResponseModel<?> transaction = bookingService.createTransaction(transactionRequest, transactionId);

        Assertions.assertEquals(transaction.getMessage(),Constants.TRANSACTION_CREATED);
        Assertions.assertNull(transaction.getData());
    }

    @Test
    public void testParentChildCurrencyMismatch(){
        int transactionId = 1;

        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(22.00)
                .type("expenses")
                .currency("INR")
                .parent(1)
                .build();


        TransactionEntity parent = TransactionEntity.builder()
                .transactionId(1)
                .amount(22.00)
                .subTransactions(null)
                .currency("EUR")
                .parentTransaction(null)
                .type("expenses")
                .build();

        when(transactionRepository.checkTransactionExists(anyInt())).thenReturn(false);
        when(transactionRepository.findById(anyInt())).thenReturn(Optional.of(parent));

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> bookingService.createTransaction(transactionRequest, transactionId));

        Assertions.assertEquals(customException.getMessage(),Constants.PARENT_CHILD_CURRENCY_MISMATCH);
        Assertions.assertEquals(customException.getStatus(),HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testInvalidParentId(){
        int transactionId = 1;

        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(22.00)
                .type("expenses")
                .currency("INR")
                .parent(1)
                .build();


        when(transactionRepository.checkTransactionExists(anyInt())).thenReturn(false);
        when(transactionRepository.findById(anyInt())).thenReturn(Optional.ofNullable(null));

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> bookingService.createTransaction(transactionRequest,transactionId));

        Assertions.assertEquals(customException.getMessage(),Constants.INVALID_PARENT_ID);
        Assertions.assertEquals(customException.getStatus(),HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testTransactionExists(){
        int transactionId = 1;

        TransactionRequest transactionRequest = TransactionRequest.builder()
                .amount(22.00)
                .type("expenses")
                .currency("INR")
                .parent(1)
                .build();


        when(transactionRepository.checkTransactionExists(anyInt())).thenReturn(true);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> bookingService.createTransaction(transactionRequest, transactionId));

        Assertions.assertEquals(customException.getMessage(),Constants.transactionExists(transactionId));
        Assertions.assertEquals(customException.getStatus(),HttpStatus.BAD_REQUEST);
    }


    @Test
    public void testGetTransactionTypes(){
        String transactionType = "expenses";
        when(transactionRepository.findTransactionIdByType(transactionType)).thenReturn(List.of(1,2,3,4,5));

        ResponseModel<List<Integer>> transactionIdList = bookingService.getTransactionIdList(transactionType);

        Assertions.assertEquals(transactionIdList.getMessage(),Constants.transactionTypes(transactionType));
        Assertions.assertEquals(transactionIdList.getData(),List.of(1,2,3,4,5));
    }

    @Test
    public void testNoTransactionIdOfSpecificType(){
        String transactionType = "expenses";
        when(transactionRepository.findTransactionIdByType(transactionType)).thenReturn(anyList());

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> bookingService.getTransactionIdList(transactionType));

        Assertions.assertEquals(customException.getMessage(),Constants.noTransactionTypes(transactionType));
        Assertions.assertEquals(customException.getStatus(),HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetAllCurrencies(){
        List<String> currencies = List.of("INR", "EUR", "USD");

        when(transactionRepository.findAllCurrencies()).thenReturn(currencies);

        ResponseModel<List<String>> allCurrencies = bookingService.getAllCurrencies();

        Assertions.assertEquals(allCurrencies.getMessage(),Constants.ALL_CURRENCIES_USED_IN_TRANSACTION);
        Assertions.assertEquals(allCurrencies.getData(),currencies);
    }

    @Test
    public void testNoCurrencyFound(){
        when(transactionRepository.findAllCurrencies()).thenReturn(List.of());

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> bookingService.getAllCurrencies());

        Assertions.assertEquals(customException.getMessage(),Constants.CURRENCY_NOT_FOUND);
        Assertions.assertEquals(customException.getStatus(),HttpStatus.NOT_FOUND);
    }

    @Test
    public void testSumOfTransactions(){
        int transactionId = 1;

        TransactionEntity child = TransactionEntity.builder()
                .transactionId(1)
                .amount(100.00)
                .subTransactions(null)
                .currency("EUR")
                .parentTransaction(null)
                .type("expenses")
                .build();


        TransactionEntity parent = TransactionEntity.builder()
                .transactionId(1)
                .amount(22.00)
                .subTransactions(Set.of(child))
                .currency("EUR")
                .parentTransaction(null)
                .type("expenses")
                .build();

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(parent));

        ResponseModel<SumResponse> sumOfTransactions = bookingService.findSumOfTransactions(transactionId);

        Assertions.assertEquals(sumOfTransactions.getMessage(),Constants.SUM_OF_PARENT_AND_CHILD_TRANSACTIONS);
        Assertions.assertEquals(sumOfTransactions.getData().getSum(),parent.getAmount()+child.getAmount());
        Assertions.assertEquals(sumOfTransactions.getData().getCurrency(),parent.getCurrency());
    }

    @Test
    public void testNoTransactionFound(){
        int transactionId = 1;

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.ofNullable(null));

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> bookingService.findSumOfTransactions(transactionId));

        Assertions.assertEquals(customException.getMessage(),Constants.transactionDoesNotExists(transactionId));
        Assertions.assertEquals(customException.getStatus(),HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetTransactionByID(){
        int transactionId = 1;
        TransactionEntity transactionEntity = TransactionEntity.builder()
                .transactionId(1)
                .amount(22.00)
                .subTransactions(null)
                .currency("EUR")
                .parentTransaction(null)
                .type("expenses")
                .build();

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.ofNullable(transactionEntity));

        ResponseModel<TransactionResponseDTO> transactionById = bookingService.getTransactionById(transactionId);

        Assertions.assertEquals(transactionById.getMessage(),Constants.TRANSACTION_FETCHED_SUCCESSFULLY);
        Assertions.assertNotNull(transactionById.getData());
    }

    @Test
    public void testGetTransactionDoesNotExists(){
        int transactionId = 1;

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.ofNullable(null));

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> bookingService.getTransactionById(transactionId));

        Assertions.assertEquals(customException.getMessage(),Constants.transactionDoesNotExists(transactionId));
        Assertions.assertEquals(customException.getStatus(),HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteTransaction(){
        int transactionId = 1;
        when(transactionRepository.checkTransactionExists(transactionId)).thenReturn(true);

        ResponseModel<?> responseModel = bookingService.deleteTransaction(transactionId);

        Assertions.assertEquals(responseModel.getMessage(),Constants.DELETED_SUCCESFULLY);
        Assertions.assertNull(responseModel.getData());
    }

    @Test
    public void testDeleteTransactionNotFound(){
        int transactionId = 1;
        when(transactionRepository.checkTransactionExists(transactionId)).thenReturn(false);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> bookingService.deleteTransaction(transactionId));

        Assertions.assertEquals(customException.getMessage(),Constants.transactionDoesNotExists(transactionId));
        Assertions.assertEquals(customException.getStatus(),HttpStatus.NOT_FOUND);

    }
}
