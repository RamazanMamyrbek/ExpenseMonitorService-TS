package com.ramazanmamyrbek.expensemonitorservice.service;

import com.ramazanmamyrbek.expensemonitorservice.dto.response.TransactionResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.entity.Transaction;
import com.ramazanmamyrbek.expensemonitorservice.repository.ExchangeRateRepository;
import com.ramazanmamyrbek.expensemonitorservice.repository.TransactionRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionServiceTest {

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
    void testSave() {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(1234567890L);
        transaction.setAccountTo(9876543210L);
        transaction.setCurrencyShortname("USD");
        transaction.setSum(new BigDecimal("200.00"));
        transaction.setExpenseCategory("product");
        transaction.setDatetime(ZonedDateTime.parse("2024-08-01T10:00:00+06:00"));
        transaction.setSumUsd(new BigDecimal("200.00"));
        transaction.setLimitExceeded(false);
        transaction.setRemainingLimitUsd(new BigDecimal("1000.00"));

        TransactionResponseDto expected = new TransactionResponseDto();
        expected.setId(2L);
        expected.setAccountFrom(1234567890L);
        expected.setAccountTo(9876543210L);
        expected.setCurrencyShortname("USD");
        expected.setSum(new BigDecimal("200.00"));
        expected.setExpenseCategory("product");
        expected.setDatetime(ZonedDateTime.parse("2024-08-01T04:00:00Z"));
        expected.setSumUsd(new BigDecimal("200.00"));
        expected.setLimitExceeded(false);

        TransactionResponseDto actual = transactionService.save(transaction);

        assertEquals(expected.getAccountFrom(), actual.getAccountFrom());
        assertEquals(expected.getAccountTo(), actual.getAccountTo());
        assertEquals(expected.getSum(), actual.getSum());
        assertEquals(expected.getSum(), actual.getSumUsd());
        assertEquals(expected.getExpenseCategory(), actual.getExpenseCategory());
    }

    @Test
    void testExceededTransactions() {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(1234567890L);
        transaction.setAccountTo(9876543210L);
        transaction.setCurrencyShortname("USD");
        transaction.setSum(new BigDecimal("200.00"));
        transaction.setExpenseCategory("product");
        transaction.setDatetime(ZonedDateTime.parse("2024-08-01T10:00:00+06:00"));
        transaction.setSumUsd(new BigDecimal("200.00"));
        transaction.setLimitExceeded(true);
        transaction.setRemainingLimitUsd(new BigDecimal("1000.00"));
        transactionRepository.save(transaction);
        List<TransactionResponseDto> expectedList = List.of(transactionService.save(transaction));
        List<TransactionResponseDto> actualList = transactionService.getExceededTransactions();

        for(int i = 0; i < expectedList.size(); i++) {
            TransactionResponseDto expected = expectedList.get(i);
            TransactionResponseDto actual = actualList.get(i);
            assertEquals(expected.getAccountFrom(), actual.getAccountFrom());
            assertEquals(expected.getAccountTo(), actual.getAccountTo());
            assertEquals(expected.getSum(), actual.getSum());
            assertEquals(expected.getSum(), actual.getSumUsd());
            assertEquals(expected.getExpenseCategory(), actual.getExpenseCategory());
            assertEquals(expected.isLimitExceeded(), actual.isLimitExceeded());
        }
    }

    @Test
    void testGetAllServicesByDateTimeAfter() {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(1234567890L);
        transaction.setAccountTo(9876543210L);
        transaction.setCurrencyShortname("USD");
        transaction.setSum(new BigDecimal("200.00"));
        transaction.setExpenseCategory("product");
        transaction.setDatetime(ZonedDateTime.parse("2024-08-01T10:00:00+06:00"));
        transaction.setSumUsd(new BigDecimal("200.00"));
        transaction.setLimitExceeded(true);
        transaction.setRemainingLimitUsd(new BigDecimal("1000.00"));
        transactionRepository.save(transaction);

        ZonedDateTime dateBefore = ZonedDateTime.parse("2023-08-01T10:00:00+06:00");
        List<Transaction> expectedList = List.of(transaction);
        List<Transaction> actualList = transactionService.getAllServicesByDateTimeAfter(dateBefore);

        for(int i = 0; i < expectedList.size(); i++) {
            Transaction expected = expectedList.get(i);
            Transaction actual = actualList.get(i);
            assertEquals(expected.getAccountFrom(), actual.getAccountFrom());
            assertEquals(expected.getAccountTo(), actual.getAccountTo());
            assertEquals(expected.getSum(), actual.getSum());
            assertEquals(expected.getSum(), actual.getSumUsd());
            assertEquals(expected.getExpenseCategory(), actual.getExpenseCategory());
            assertEquals(expected.isLimitExceeded(), actual.isLimitExceeded());
        }
    }

    @Test
    void getLatestTransactionByExpenseCategory() {
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(1234567890L);
        transaction.setAccountTo(9876543210L);
        transaction.setCurrencyShortname("USD");
        transaction.setSum(new BigDecimal("200.00"));
        transaction.setExpenseCategory("product");
        transaction.setDatetime(ZonedDateTime.parse("2024-08-01T10:00:00+06:00"));
        transaction.setSumUsd(new BigDecimal("200.00"));
        transaction.setLimitExceeded(true);
        transaction.setRemainingLimitUsd(new BigDecimal("1000.00"));
        transactionRepository.save(transaction);

        String expenseCategory = "product";

        Transaction expected = transaction;
        Transaction actual = transactionService.getLatestTransactionByExpenseCategory(expenseCategory);
        assertEquals(expected.getAccountFrom(), actual.getAccountFrom());
        assertEquals(expected.getAccountTo(), actual.getAccountTo());
        assertEquals(expected.getSum(), actual.getSum());
        assertEquals(expected.getSum(), actual.getSumUsd());
        assertEquals(expected.getExpenseCategory(), actual.getExpenseCategory());
        assertEquals(expected.isLimitExceeded(), actual.isLimitExceeded());
    }
}



