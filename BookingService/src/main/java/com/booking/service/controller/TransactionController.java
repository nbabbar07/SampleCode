package com.booking.service.controller;

import com.booking.service.dto.request.TransactionRequest;
import com.booking.service.dto.response.GetAllTransactionsResponse;
import com.booking.service.dto.response.SumResponse;
import com.booking.service.dto.response.TransactionResponseDTO;
import com.booking.service.repository.TransactionRepository;
import com.booking.service.service.BookingService;
import com.booking.service.dto.response.ResponseModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookingservice")
public class TransactionController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TransactionRepository transactionRepository;

    @PutMapping("/transaction/{transaction_id}")
    public ResponseEntity<ResponseModel<?>> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest, @PathVariable("transaction_id") Integer transactionId){
        ResponseModel<?> returnValue = bookingService.createTransaction(transactionRequest, transactionId);

        return new ResponseEntity<>(returnValue,HttpStatus.CREATED);
    }

    @GetMapping("/transaction/{transaction_id}")
    public ResponseEntity<ResponseModel<TransactionResponseDTO>> getTransactionById(@PathVariable("transaction_id") Integer transactionId){
        ResponseModel<TransactionResponseDTO> returnValue = bookingService.getTransactionById(transactionId);

        return new ResponseEntity<>(returnValue,HttpStatus.OK);
    }

    @GetMapping("/transaction")
    public ResponseEntity<ResponseModel<GetAllTransactionsResponse>> getAllTransactions(@RequestParam(name = "page",defaultValue = "0") Integer page,
                                                                                          @RequestParam(name = "size",defaultValue = "5") Integer size){
        ResponseModel<GetAllTransactionsResponse> returnValue = bookingService.getAllTransactions(page, size);

        return new ResponseEntity<>(returnValue,HttpStatus.OK);
    }
    @DeleteMapping("/transaction/{transaction_id}")
    public ResponseEntity<ResponseModel<?>> deleteTransaction(@PathVariable("transaction_id") Integer transactionId){
        ResponseModel<?> returnValue = bookingService.deleteTransaction(transactionId);

        return new ResponseEntity<>(returnValue,HttpStatus.OK);
    }

    @GetMapping("/types/{type}")
    public ResponseEntity<ResponseModel<List<Integer>>> getTransactionIdsWithGivenType(@PathVariable("type") String type){
        ResponseModel<List<Integer>> returnValue = bookingService.getTransactionIdList(type);

        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @GetMapping("/currencies")
    public ResponseEntity<ResponseModel<List<String>>> getAllUsedCurrencies(){
        ResponseModel<List<String>> returnValue = bookingService.getAllCurrencies();

        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @GetMapping("/sum/{transaction_id}")
    public ResponseEntity<ResponseModel<SumResponse>> findSumOfTransactions(@PathVariable("transaction_id") Integer transactionId){
        ResponseModel<SumResponse> returnValue = bookingService.findSumOfTransactions(transactionId);

        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }


}
