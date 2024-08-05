package com.ramazanmamyrbek.expensemonitorservice.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LimitResponseDto {

    Long id;

    BigDecimal limitSum;

    String limitCurrencyShortname;

    String expenseCategory;

    ZonedDateTime limitDatetime;

    BigDecimal limitSumUsd;

    Long accountFrom;
}
