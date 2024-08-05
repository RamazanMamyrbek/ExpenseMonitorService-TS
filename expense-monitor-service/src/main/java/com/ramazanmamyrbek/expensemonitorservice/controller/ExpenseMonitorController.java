package com.ramazanmamyrbek.expensemonitorservice.controller;

import com.ramazanmamyrbek.expensemonitorservice.dto.request.LimitCreateRequestDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.request.TransactionAcceptRequestDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.response.ExchangeRatesResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.response.LimitResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.response.TransactionResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.entity.Limit;
import com.ramazanmamyrbek.expensemonitorservice.service.ExpenseMonitoringService;
import com.ramazanmamyrbek.expensemonitorservice.service.LimitService;
import com.ramazanmamyrbek.expensemonitorservice.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Validated
@Tag(name = "Expense monitoring")
public class ExpenseMonitorController {
    final ExpenseMonitoringService expenseMonitoringService;


    @PostMapping(value = "/transactions", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Accept a transaction")
    public ResponseEntity<TransactionResponseDto> acceptTransaction(@RequestBody TransactionAcceptRequestDto transactionAcceptRequestDto) {
        TransactionResponseDto transactionResponseDto = expenseMonitoringService.acceptTransaction(transactionAcceptRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDto);
    }

    @GetMapping("/transactions/exceeded-limit")
    @Operation(summary = "Get transactions with exceeded limit")
    public ResponseEntity<List<TransactionResponseDto>> exceededTransactions() {
        return ResponseEntity.ok().body(expenseMonitoringService.getExceededTransactions());
    }

    @GetMapping("/limits")
    @Operation(summary = "Get all limits")
    public ResponseEntity<List<LimitResponseDto>> getAllLimits() {
        return ResponseEntity.ok().body(expenseMonitoringService.getAllLimits());
    }

    @PostMapping("/limits")
    @Operation(summary = "Create a limit")
    public ResponseEntity<LimitResponseDto> createLimit(@RequestBody LimitCreateRequestDto limitCreateRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseMonitoringService.createLimit(limitCreateRequestDto));
    }


    @PutMapping("/exchange-rates")
    @Operation(summary = "Update exchange rates manually")
    public ResponseEntity<ExchangeRatesResponseDto> updateExchangeRates() throws InterruptedException {
        return ResponseEntity.ok().body(expenseMonitoringService.updateExchangeRates());
    }

}
