package com.booking.service.exception.handler;

import com.booking.service.dto.response.ResponseModel;
import com.booking.service.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseModel<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String,String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(x-> errorMap.put(x.getField(),x.getDefaultMessage()));
        return new ResponseEntity<>(new ResponseModel<>(errorMap,"Error"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseModel<?>> handleCustomException(CustomException ex){
        return new ResponseEntity<>(new ResponseModel<>(null,ex.getMessage()),ex.getStatus());
    }
}
