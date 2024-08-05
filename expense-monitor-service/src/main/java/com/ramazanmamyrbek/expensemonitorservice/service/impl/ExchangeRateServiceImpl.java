package com.ramazanmamyrbek.expensemonitorservice.service.impl;

import com.ramazanmamyrbek.expensemonitorservice.dto.response.ExchangeRatesResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.entity.ExchangeRate;
import com.ramazanmamyrbek.expensemonitorservice.exception.BadRequestException;
import com.ramazanmamyrbek.expensemonitorservice.feign.ExchangeRateFeignClient;
import com.ramazanmamyrbek.expensemonitorservice.repository.ExchangeRateRepository;
import com.ramazanmamyrbek.expensemonitorservice.service.ExchangeRateService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {
    final ExchangeRateRepository exchangeRateRepository;
    final ExchangeRateFeignClient exchangeRateFeignClient;
    @Value("${openexchangerates.appId}")
    String appId;

    @Override
    public BigDecimal toUsd(BigDecimal sum, String currencyShortname) {
        String currencyPair = "USD/"+currencyShortname;
        ExchangeRate exchangeRate = exchangeRateRepository.findExchangeRateByCurrencyPair(currencyPair)
                .orElseThrow(() -> new BadRequestException("Currency pair " + currencyPair + " is not found. Try to update exchanges by invoking /api/exchange-rates"));
        double sumUsd = sum.doubleValue() * exchangeRate.getRate();
        return new BigDecimal(sumUsd);
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public ExchangeRatesResponseDto updateExchangeRates() throws InterruptedException {
        truncateTable();
        ExchangeRatesResponseDto exchangeRatesResponseDto = exchangeRateFeignClient.updateExchangeRates(appId);
        if(exchangeRatesResponseDto != null) {
            for(Map.Entry<String, Double> rate: exchangeRatesResponseDto.getRates().entrySet()) {
                ExchangeRate exchangeRateToUsd = new ExchangeRate();
                exchangeRateToUsd.setCurrencyPair(rate.getKey()+"/USD");
                exchangeRateToUsd.setRate(rate.getValue());


                ExchangeRate exchangeRateFromUsd = new ExchangeRate();
                exchangeRateFromUsd.setCurrencyPair("USD/" + rate.getKey());
                exchangeRateFromUsd.setRate(1.0 / rate.getValue());

                exchangeRateRepository.save(exchangeRateToUsd);
                if(!exchangeRateFromUsd.getCurrencyPair().equals("USD/USD"))
                    exchangeRateRepository.save(exchangeRateFromUsd);
            }
        }
        return exchangeRatesResponseDto;
    }

    @Transactional
    protected void truncateTable() {
        exchangeRateRepository.deleteAll();
    }
}
