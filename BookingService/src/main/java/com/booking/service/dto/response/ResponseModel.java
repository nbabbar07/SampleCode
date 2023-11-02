package com.booking.service.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel<T> {
    private T data;

    private String message;

}
