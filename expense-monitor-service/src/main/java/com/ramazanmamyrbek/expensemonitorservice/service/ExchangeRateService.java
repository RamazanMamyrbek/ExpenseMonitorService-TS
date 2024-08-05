package com.ramazanmamyrbek.expensemonitorservice.service;

import com.ramazanmamyrbek.expensemonitorservice.dto.response.ExchangeRatesResponseDto;

import java.math.BigDecimal;

public interface ExchangeRateService {
    BigDecimal toUsd(BigDecimal sum, String currencyShortname);

    ExchangeRatesResponseDto updateExchangeRates() throws InterruptedException;
}
