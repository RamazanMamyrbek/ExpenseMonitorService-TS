package com.ramazanmamyrbek.expensemonitorservice.service;

import com.ramazanmamyrbek.expensemonitorservice.dto.request.LimitCreateRequestDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.request.TransactionAcceptRequestDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.response.ExchangeRatesResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.response.LimitResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.response.TransactionResponseDto;

import java.util.List;

public interface ExpenseMonitoringService {
    TransactionResponseDto acceptTransaction(TransactionAcceptRequestDto transactionAcceptRequestDto);

    List<TransactionResponseDto> getExceededTransactions();

    List<LimitResponseDto> getAllLimits();

    LimitResponseDto createLimit(LimitCreateRequestDto limitCreateRequestDto);

    ExchangeRatesResponseDto updateExchangeRates() throws InterruptedException;
}
