package com.booking.service.dto.response;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllTransactionsResponse {

    List<TransactionResponseDTO> transactions;
    private Long count;
    private Integer totalPages;
}
