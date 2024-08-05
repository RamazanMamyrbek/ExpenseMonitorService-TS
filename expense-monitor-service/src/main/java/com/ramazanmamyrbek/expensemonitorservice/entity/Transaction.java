package com.ramazanmamyrbek.expensemonitorservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_from", nullable = false)
    private Long accountFrom;

    @Column(name = "account_to", nullable = false)
    private Long accountTo;

    @Column(name = "currency_shortname", nullable = false)
    private String currencyShortname;

    @Column(name = "sum", nullable = false)
    private BigDecimal sum;

    @Column(name = "expense_category", nullable = false)
    private String expenseCategory;

    @Column(name = "datetime", nullable = false)
    private ZonedDateTime datetime;

    @Column(name = "sum_usd", nullable = false)
    private BigDecimal sumUsd;

    @Column(name = "limit_exceeded", nullable = false)
    private boolean limitExceeded;

    @Column(name = "remaining_limit_usd", nullable = false)
    private BigDecimal remainingLimitUsd;

    @ManyToOne
    @JoinColumn(name = "limit_id", referencedColumnName = "id")
    private Limit limit;
}
