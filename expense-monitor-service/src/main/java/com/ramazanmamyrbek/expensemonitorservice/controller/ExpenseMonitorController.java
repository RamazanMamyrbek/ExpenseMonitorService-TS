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
import jakarta.validation.Valid;
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
    @Operation(summary = "Accept a transaction", description = "This endpoint is for accepting a transaction from other service and saving it to db.'Остаток месячного лимита' (remaining_limit_usd) is counted and saved in 'transactions' table ")
    public ResponseEntity<TransactionResponseDto> acceptTransaction(@RequestBody @Valid TransactionAcceptRequestDto transactionAcceptRequestDto) {
        TransactionResponseDto transactionResponseDto = expenseMonitoringService.acceptTransaction(transactionAcceptRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponseDto);
    }

    @GetMapping("/transactions/exceeded-limit")
    @Operation(summary = "Get transactions with exceeded limit", description = "This endpoint is for getting all exceeded endpoints for a user")
    public ResponseEntity<List<TransactionResponseDto>> exceededTransactionsByAccount(@RequestParam Long accountId) {
        return ResponseEntity.ok().body(expenseMonitoringService.getExceededTransactions(accountId));
    }

    @GetMapping("/limits")
    @Operation(summary = "Get all limits", description = "This endpoint is for getting all limits of account")
    public ResponseEntity<List<LimitResponseDto>> getAllLimitsByAccount(@RequestParam Long accountId) {
        return ResponseEntity.ok().body(expenseMonitoringService.getAllLimitsByAccount(accountId));
    }

    @PostMapping("/limits")
    @Operation(summary = "Create a limit", description = "The transaction limit, as described in the task, applies only to expenditure transactions. This means that the limit is applied when the user sends money (i.e., expenses), and not when they receive it.")
    public ResponseEntity<LimitResponseDto> createLimit(@RequestBody @Valid LimitCreateRequestDto limitCreateRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseMonitoringService.createLimit(limitCreateRequestDto));
    }


    @PutMapping("/exchange-rates")
    @Operation(summary = "Update exchange rates manually", description = "This endpoint is for updating exchange rates in database.By default it will be updated automatically every day at 00:00")
    public ResponseEntity<ExchangeRatesResponseDto> updateExchangeRates() throws InterruptedException {
        return ResponseEntity.ok().body(expenseMonitoringService.updateExchangeRates());
    }

}
