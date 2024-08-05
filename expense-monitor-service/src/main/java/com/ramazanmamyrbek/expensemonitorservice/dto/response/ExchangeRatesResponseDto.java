package com.ramazanmamyrbek.expensemonitorservice.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangeRatesResponseDto {
    String disclaimer;
    String license;
    int timestamp;
    String base;
    Map<String, Double> rates;
}

