package com.booking.service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @Column(name = "transaction_id", nullable = false)
    private Integer transactionId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "type")
    private String type;

    @Column(name = "currency")
    private String currency;

    @ManyToOne(fetch = FetchType.LAZY)
    private TransactionEntity parentTransaction;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "parentTransaction")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<TransactionEntity> subTransactions;


}
