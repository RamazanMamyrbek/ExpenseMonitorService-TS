package com.ramazanmamyrbek.expensemonitorservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Schema(description = "Dto for creating a limit")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LimitCreateRequestDto {
    @Schema(example = "2000")
    @Min(value = 0)
    BigDecimal limitSum;

    @Schema(example = "USD")
    String limitCurrencyShortname;

    @Schema(example = "product")
    String expenseCategory;

    Long accountFrom;
}
