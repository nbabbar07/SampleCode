package com.booking.service.util;

import org.springframework.http.HttpStatus;

public class Constants {
    public static final String PARENT_CHILD_CURRENCY_MISMATCH = "Currency of parent doesn't match with child";
    public static final String INVALID_PARENT_ID = "Invalid Parent Id";
    public static final String TRANSACTION_CREATED = "Transaction Successfully Created!";
    public static final String CURRENCY_NOT_FOUND = "No Currencies Found!";
    public static final String ALL_CURRENCIES_USED_IN_TRANSACTION = "Currencies used in the transaction.";
    public static final String SUM_OF_PARENT_AND_CHILD_TRANSACTIONS = "Sum of parent and child transactions";
    public static final String TRANSACTION_FETCHED_SUCCESSFULLY = "Transaction fetched successfully!";
    public static final String ALL_TRANSACTIONS_FETCHED = "All transactions fetched successfully!";
    public static final String DELETED_SUCCESFULLY = "Transaction deleted successfully!";
    public static final String NO_TRANSACTION_FOUND = "No Transaction Found!";
    public static final String TRANSACTION_NOT_FOUND = "Transaction not found!";
    public static String transactionExists(int transactionId){
        return "Transaction with Id :"+transactionId+" already exists";
    }
    public static String transactionDoesNotExists(int transactionId){
        return "No Transaction found with id: "+transactionId;
    }
    public static String transactionTypes(String type){
        return "All the transaction ids having type: "+type;
    }
    public static String noTransactionTypes(String type){
        return "No Transaction Id found having type: "+type;
    }

}
