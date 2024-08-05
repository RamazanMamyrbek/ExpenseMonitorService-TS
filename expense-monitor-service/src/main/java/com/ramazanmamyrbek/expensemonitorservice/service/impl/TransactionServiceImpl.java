package com.ramazanmamyrbek.expensemonitorservice.service.impl;

import com.ramazanmamyrbek.expensemonitorservice.dto.response.TransactionResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.entity.Transaction;
import com.ramazanmamyrbek.expensemonitorservice.exception.BadRequestException;
import com.ramazanmamyrbek.expensemonitorservice.repository.TransactionRepository;
import com.ramazanmamyrbek.expensemonitorservice.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    final TransactionRepository transactionRepository;
    final ModelMapper modelMapper;

    @Override
    @Transactional
    public TransactionResponseDto save(Transaction transaction) {
        Transaction savedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, TransactionResponseDto.class);
    }

    @Override
    public List<TransactionResponseDto> getExceededTransactions() {
        return transactionRepository.findAllByLimitExceeded(true).stream()
                .map(transaction -> modelMapper.map(transaction, TransactionResponseDto.class)).toList();
    }

    @Override
    public List<Transaction> getAllServicesByDateTimeAfter(ZonedDateTime limitDatetime) {
        return transactionRepository.findAllByDatetimeAfter(limitDatetime);
    }

    @Override
    public Transaction getLatestTransactionByExpenseCategory(String expenseCategory) {
        return transactionRepository.findLatestTransactionByExpenseCategory(expenseCategory)
                .orElse(null);
    }
}
