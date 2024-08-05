package com.ramazanmamyrbek.expensemonitorservice.service.impl;

import com.ramazanmamyrbek.expensemonitorservice.dto.request.LimitCreateRequestDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.request.TransactionAcceptRequestDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.response.ExchangeRatesResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.response.LimitResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.response.TransactionResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.entity.Limit;
import com.ramazanmamyrbek.expensemonitorservice.entity.Transaction;
import com.ramazanmamyrbek.expensemonitorservice.exception.BadRequestException;
import com.ramazanmamyrbek.expensemonitorservice.service.ExchangeRateService;
import com.ramazanmamyrbek.expensemonitorservice.service.ExpenseMonitoringService;
import com.ramazanmamyrbek.expensemonitorservice.service.LimitService;
import com.ramazanmamyrbek.expensemonitorservice.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ExpenseMonitoringServiceImpl implements ExpenseMonitoringService {
    final TransactionService transactionService;
    final LimitService limitService;
    final ExchangeRateService exchangeRateService;
    final ModelMapper modelMapper;


    @Override
    @Transactional
    public TransactionResponseDto acceptTransaction(TransactionAcceptRequestDto transactionAcceptRequestDto) {
        if(!transactionAcceptRequestDto.getExpenseCategory().equals("product") &&
        !transactionAcceptRequestDto.getExpenseCategory().equals("service"))
            throw new BadRequestException("Expense category should be 'service' or 'product'");
        Transaction transaction = modelMapper.map(transactionAcceptRequestDto, Transaction.class);
        transaction.setSumUsd(exchangeRateService.toUsd(transaction.getSum(),transactionAcceptRequestDto.getCurrencyShortname()));
        transaction.setLimitExceeded(isLimitExceeded(transaction));
        TransactionResponseDto transactionResponseDto = transactionService.save(transaction);
        return transactionResponseDto;
    }

    private boolean isLimitExceeded(Transaction transaction) {
        if(limitService.getLatestLimitByExpenseCategory(transaction.getExpenseCategory()).isEmpty()) {
            Limit limit = new Limit();
            limit.setLimitSumUsd(new BigDecimal(1000));
            limit.setLimitCurrencyShortname("USD");
            limit.setLimitSum(new BigDecimal(1000));
            limit.setExpenseCategory(transaction.getExpenseCategory());
            transaction.setLimit(limit);
            transaction.setRemainingLimitUsd(limit.getLimitSumUsd().subtract(transaction.getSumUsd()));
            limitService.save(limit);
        } else{
            Limit limit = limitService.getLatestLimitByExpenseCategory(transaction.getExpenseCategory()).get();
            Transaction latestTransaction = transactionService.getLatestTransactionByExpenseCategory(transaction.getExpenseCategory());
            transaction.setLimit(limit);
            if(latestTransaction == null) {
                transaction.setRemainingLimitUsd(limit.getLimitSumUsd().subtract(transaction.getSumUsd()));
            } else {
                if(!latestTransaction.getLimit().equals(limit)) {
                    Limit previousLimit = latestTransaction.getLimit();
                    BigDecimal newRemainingLimit = limit.getLimitSumUsd().subtract(previousLimit.getLimitSumUsd().subtract(latestTransaction.getRemainingLimitUsd())).subtract(transaction.getSumUsd());
                    transaction.setRemainingLimitUsd(newRemainingLimit);

                } else {
                    transaction.setRemainingLimitUsd(latestTransaction.getRemainingLimitUsd().subtract(transaction.getSumUsd()));
                }
            }
        }
        return transaction.getRemainingLimitUsd().doubleValue() < 0;
    }

    @Override
    public List<TransactionResponseDto> getExceededTransactions() {
        return transactionService.getExceededTransactions();
    }

    @Override
    public List<LimitResponseDto> getAllLimits() {
        return limitService.getAllLimits();
    }

    @Override
    @Transactional
    public LimitResponseDto createLimit(LimitCreateRequestDto limitCreateRequestDto) {
        if(!limitCreateRequestDto.getExpenseCategory().equals("product") &&
        !limitCreateRequestDto.getExpenseCategory().equals("service"))
            throw new BadRequestException("Expense category should be 'service' or 'product'");
        return limitService.createLimit(limitCreateRequestDto);
    }

    @Override
    @Transactional
    public ExchangeRatesResponseDto updateExchangeRates() throws InterruptedException {
        return exchangeRateService.updateExchangeRates();
    }
}
