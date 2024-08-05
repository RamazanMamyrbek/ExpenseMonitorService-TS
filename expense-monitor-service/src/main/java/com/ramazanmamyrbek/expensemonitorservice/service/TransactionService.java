package com.ramazanmamyrbek.expensemonitorservice.service;

import com.ramazanmamyrbek.expensemonitorservice.dto.response.TransactionResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.entity.Transaction;

import java.time.ZonedDateTime;
import java.util.List;

public interface TransactionService {
    TransactionResponseDto save(Transaction transaction);

    List<TransactionResponseDto> getExceededTransactions();

    List<Transaction> getAllServicesByDateTimeAfter(ZonedDateTime limitDatetime);

    Transaction getLatestTransactionByExpenseCategory(String expenseCategory);
}
