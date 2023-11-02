package com.booking.service.dto.response;

import lombok.*;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponseDTO {
    private Integer transactionId;
    private Double amount;
    private String type;
    private String currency;
}
