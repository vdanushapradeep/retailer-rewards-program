package com.example.rewards.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
    name = "transactions",
    indexes = {
        @Index(name = "idx_transactions_customer_date", columnList = "customer_id, transaction_date")
    }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * JPA entity representing a single transaction made by a customer.
 * Contains monetary amount and the transaction date.
 */
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull
    private CustomerEntity customer;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal amount;

    @Column(name = "transaction_date", nullable = false)
    @NotNull
    private LocalDate transactionDate;
}
