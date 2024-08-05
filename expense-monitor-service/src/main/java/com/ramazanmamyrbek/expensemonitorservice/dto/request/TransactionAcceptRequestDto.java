package com.ramazanmamyrbek.expensemonitorservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Schema(description = "Dto for creating transaction")
public class TransactionAcceptRequestDto {

    @Schema(example = "1234567890")
    private Long accountFrom;

    @Schema(example = "9876543210")
    private Long accountTo;

    @Schema(example = "USD")
    private String currencyShortname;

    @Schema(example = "200")
    private BigDecimal sum;

    @Schema(example = "product")
    private String expenseCategory;

    @Schema(example = "2024-08-01T10:00:00+06:00")
    private ZonedDateTime datetime;
}
