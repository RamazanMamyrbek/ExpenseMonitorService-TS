package com.ramazanmamyrbek.expensemonitorservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "limits")
@Data
public class Limit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "limit_sum")
    private BigDecimal limitSum;

    @Column(name = "limit_currency_shortname")
    private String limitCurrencyShortname;

    @Column(name = "expense_category", nullable = false)
    private String expenseCategory;

    @Column(name = "limit_datetime", nullable = false)
    private ZonedDateTime limitDatetime = ZonedDateTime.now();

    @Column(name = "limit_sum_usd", nullable = false)
    private BigDecimal limitSumUsd;
}
