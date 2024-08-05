package com.ramazanmamyrbek.expensemonitorservice.service;

import com.ramazanmamyrbek.expensemonitorservice.dto.response.ExchangeRatesResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.entity.ExchangeRate;
import com.ramazanmamyrbek.expensemonitorservice.exception.BadRequestException;
import com.ramazanmamyrbek.expensemonitorservice.feign.ExchangeRateFeignClient;
import com.ramazanmamyrbek.expensemonitorservice.repository.ExchangeRateRepository;
import com.ramazanmamyrbek.expensemonitorservice.service.impl.ExchangeRateServiceImpl;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;


import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangeRateServiceTest {

    @MockBean
    ExchangeRateRepository exchangeRateRepository;
    @MockBean
    ExchangeRateFeignClient exchangeRateFeignClient;
    @Value("${openexchangerates.appId}")
    String appId;
    @Autowired
    ExchangeRateServiceImpl exchangeRateService;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToUsdReturnsValidResult() {
        BigDecimal sum = new BigDecimal("10000");
        String currencyShortname = "KZT";
        String currencyPair = "USD/" + currencyShortname;

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setCurrencyPair(currencyPair);
        exchangeRate.setRate(0.0023);

        when(exchangeRateRepository.findExchangeRateByCurrencyPair(currencyPair))
                .thenReturn(Optional.of(exchangeRate));
        BigDecimal result = exchangeRateService.toUsd(sum, currencyShortname);

        assertNotNull(result);
        assertEquals(new BigDecimal(23), result);
    }

    @Test
    void testToUsdThrowsBadRequestException() {
        BigDecimal sum = new BigDecimal("10000");
        String currencyShortname = "Wrong currency";
        String currencyPair = currencyShortname + "/USD";

        when(exchangeRateRepository.findExchangeRateByCurrencyPair(currencyPair))
                .thenThrow(new BadRequestException("Currency pair " + currencyPair + " is not found"));

        assertThrows(BadRequestException.class, () -> exchangeRateService.toUsd(sum, currencyShortname));
    }

    @Test
    void testUpdateExchangeRatesReturnsValidResponse() throws InterruptedException {
        ExchangeRatesResponseDto exchangeRatesResponseDto = new ExchangeRatesResponseDto();
        exchangeRatesResponseDto.setDisclaimer("disclaimer");
        exchangeRatesResponseDto.setLicense("license");
        exchangeRatesResponseDto.setTimestamp(1234567);
        exchangeRatesResponseDto.setRates(Map.of("KZT", 470.00, "RUB", 84.3));
        when(exchangeRateFeignClient.updateExchangeRates(appId))
                .thenReturn(exchangeRatesResponseDto);

        assertEquals(exchangeRatesResponseDto, exchangeRateService.updateExchangeRates());
    }


}
