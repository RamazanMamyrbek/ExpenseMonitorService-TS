package com.ramazanmamyrbek.expensemonitorservice.service;

import com.ramazanmamyrbek.expensemonitorservice.dto.request.LimitCreateRequestDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.request.TransactionAcceptRequestDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.response.LimitResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.response.TransactionResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.entity.Transaction;
import com.ramazanmamyrbek.expensemonitorservice.exception.BadRequestException;
import com.ramazanmamyrbek.expensemonitorservice.repository.ExchangeRateRepository;
import com.ramazanmamyrbek.expensemonitorservice.repository.TransactionRepository;
import com.ramazanmamyrbek.expensemonitorservice.service.impl.ExpenseMonitoringServiceImpl;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpenseMonitoringServiceTest {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionService transactionService;
    @Autowired
    ExchangeRateService exchangeRateService;
    @Autowired
    ExchangeRateRepository exchangeRateRepository;
    @Autowired
    ExpenseMonitoringServiceImpl expenseMonitoringService;


    @BeforeEach
    void beforeEach() throws InterruptedException {
        exchangeRateRepository.deleteAll();
        exchangeRateService.updateExchangeRates();
    }

    @AfterEach
    void afterEach() {
        exchangeRateRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    void testAcceptTransactionWorksCorrectly() {
        TransactionAcceptRequestDto requestDto = new TransactionAcceptRequestDto();
        requestDto.setAccountFrom(1234567890L);
        requestDto.setAccountTo(9876543210L);
        requestDto.setCurrencyShortname("USD");
        requestDto.setSum(new BigDecimal(200.00));
        requestDto.setExpenseCategory("product");
        requestDto.setDatetime(ZonedDateTime.parse("2024-08-01T10:00:00+06:00"));

        TransactionResponseDto expected = new TransactionResponseDto();
        expected.setId(2L);
        expected.setAccountFrom(1234567890L);
        expected.setAccountTo(9876543210L);
        expected.setCurrencyShortname("USD");
        expected.setSum(new BigDecimal(200.00));
        expected.setExpenseCategory("product");
        expected.setDatetime(ZonedDateTime.parse("2024-08-01T04:00:00Z"));
        expected.setSumUsd(new BigDecimal(200.00));
        expected.setLimitExceeded(false);

        TransactionResponseDto actual = expenseMonitoringService.acceptTransaction(requestDto);

        assertEquals(expected.getAccountFrom(), actual.getAccountFrom());
        assertEquals(expected.getAccountTo(), actual.getAccountTo());
        assertEquals(expected.getSum(), actual.getSum());
        assertEquals(expected.getSum(), actual.getSumUsd());
        assertEquals(expected.getExpenseCategory(), actual.getExpenseCategory());
    }

    @Test
    void testAcceptTransactionWithInvalidExpenseCategory() {
        TransactionAcceptRequestDto requestDto = new TransactionAcceptRequestDto();
        requestDto.setAccountFrom(1234567890L);
        requestDto.setAccountTo(9876543210L);
        requestDto.setCurrencyShortname("USD");
        requestDto.setSum(new BigDecimal(200.00));
        requestDto.setExpenseCategory("WRONG CATEGORY");
        requestDto.setDatetime(ZonedDateTime.parse("2024-08-01T10:00:00+06:00"));

        assertThrows(BadRequestException.class, () -> expenseMonitoringService.acceptTransaction(requestDto));
    }

    @Test
    void testCreateLimit() {
        LimitCreateRequestDto limitCreateRequestDto = new LimitCreateRequestDto();
        limitCreateRequestDto.setLimitCurrencyShortname("USD");
        limitCreateRequestDto.setLimitSum(new BigDecimal(2000.00));
        limitCreateRequestDto.setExpenseCategory("product");
        limitCreateRequestDto.setAccountFrom(1L);

        LimitResponseDto limitResponseDtoExpected = new LimitResponseDto();
        limitResponseDtoExpected.setId(1L);
        limitResponseDtoExpected.setAccountFrom(1L);
        limitResponseDtoExpected.setLimitSum(new BigDecimal(2000.00));
        limitResponseDtoExpected.setLimitDatetime(ZonedDateTime.now());
        limitResponseDtoExpected.setLimitSumUsd(new BigDecimal(2000.00));
        limitResponseDtoExpected.setLimitCurrencyShortname("USD");
        limitResponseDtoExpected.setExpenseCategory("product");

        LimitResponseDto limitResponseDtoActual = expenseMonitoringService.createLimit(limitCreateRequestDto);
        assertEquals(limitResponseDtoExpected.getLimitSum(), limitResponseDtoActual.getLimitSum());
        assertEquals(limitResponseDtoExpected.getLimitCurrencyShortname(), limitResponseDtoActual.getLimitCurrencyShortname());
        assertEquals(limitResponseDtoExpected.getLimitSumUsd(), limitResponseDtoActual.getLimitSumUsd());
        assertEquals(limitResponseDtoExpected.getExpenseCategory(), limitResponseDtoActual.getExpenseCategory());
        assertEquals(limitResponseDtoExpected.getAccountFrom(), limitResponseDtoActual.getAccountFrom());
    }

    @Test
    void testCreateLimitWithInvalidExpenseCategory() {
        LimitCreateRequestDto limitCreateRequestDto = new LimitCreateRequestDto();
        limitCreateRequestDto.setLimitCurrencyShortname("USD");
        limitCreateRequestDto.setAccountFrom(1L);
        limitCreateRequestDto.setLimitSum(new BigDecimal(2000.00));
        limitCreateRequestDto.setExpenseCategory("Invalid category");

        assertThrows(BadRequestException.class, () -> expenseMonitoringService.createLimit(limitCreateRequestDto));
    }
}
