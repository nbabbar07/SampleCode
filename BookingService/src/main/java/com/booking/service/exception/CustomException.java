package com.booking.service.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{
    private HttpStatus httpStatus;

    public CustomException(String message,HttpStatus httpStatus){
        super(message);
        this.httpStatus=httpStatus;
    }

    public HttpStatus getStatus(){
        return httpStatus;
    }

}
