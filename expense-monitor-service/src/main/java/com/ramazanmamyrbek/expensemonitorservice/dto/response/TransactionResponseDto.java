package com.ramazanmamyrbek.expensemonitorservice.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class TransactionResponseDto {

    private Long id;

    private Long accountFrom;

    private Long accountTo;

    private String currencyShortname;

    private BigDecimal sum;

    private String expenseCategory;

    private ZonedDateTime datetime;

    private BigDecimal sumUsd;

    private boolean limitExceeded;
}
