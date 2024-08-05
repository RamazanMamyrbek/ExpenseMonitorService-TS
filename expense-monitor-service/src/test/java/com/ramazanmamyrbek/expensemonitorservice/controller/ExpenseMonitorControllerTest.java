package com.ramazanmamyrbek.expensemonitorservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramazanmamyrbek.expensemonitorservice.dto.request.LimitCreateRequestDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.request.TransactionAcceptRequestDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.response.ExchangeRatesResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.response.LimitResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.response.TransactionResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.service.ExpenseMonitoringService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExpenseMonitorController.class)
public class ExpenseMonitorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseMonitoringService expenseMonitoringService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAcceptTransaction() throws Exception {
        TransactionAcceptRequestDto transactionAcceptRequestDto = new TransactionAcceptRequestDto();
        transactionAcceptRequestDto.setAccountFrom(1234567890L);
        transactionAcceptRequestDto.setAccountTo(9876543210L);
        transactionAcceptRequestDto.setCurrencyShortname("USD");
        transactionAcceptRequestDto.setExpenseCategory("product");
        transactionAcceptRequestDto.setSum(new BigDecimal(200));
        transactionAcceptRequestDto.setDatetime(ZonedDateTime.parse("2024-08-01T10:00:00+06:00"));

        TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
        transactionResponseDto.setId(1L);
        transactionResponseDto.setAccountFrom(1234567890L);
        transactionResponseDto.setAccountTo(9876543210L);
        transactionResponseDto.setSum(new BigDecimal(200));
        transactionResponseDto.setExpenseCategory("product");
        transactionResponseDto.setCurrencyShortname("USD");
        transactionResponseDto.setLimitExceeded(false);
        transactionResponseDto.setSumUsd(new BigDecimal(200));
        when(expenseMonitoringService.acceptTransaction(any(TransactionAcceptRequestDto.class))).thenReturn(transactionResponseDto);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionAcceptRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(transactionResponseDto)));
    }

    @Test
    void testExceededTransactions() throws Exception {
        when(expenseMonitoringService.getExceededTransactions())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/transactions/exceeded-limit"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));
    }

    @Test
    void testGetAllLimits() throws Exception {
        when(expenseMonitoringService.getAllLimits())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/limits"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));
    }

    @Test
    void testCreateLimit() throws Exception {
        LimitCreateRequestDto requestDto = new LimitCreateRequestDto();
        requestDto.setLimitSum(new BigDecimal(2000));
        requestDto.setExpenseCategory("product");
        requestDto.setLimitCurrencyShortname("USD");

        LimitResponseDto responseDto = new LimitResponseDto();
        responseDto.setId(1L);
        responseDto.setLimitSum(new BigDecimal(2000));
        responseDto.setLimitSumUsd(new BigDecimal(2000));
        responseDto.setLimitCurrencyShortname("USD");
        responseDto.setLimitDatetime(ZonedDateTime.parse("2024-08-04T13:07:01.428Z"));
        when(expenseMonitoringService.createLimit(any(LimitCreateRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/limits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    void testUpdateExchangeRates() throws Exception {
        ExchangeRatesResponseDto responseDto = new ExchangeRatesResponseDto();
        when(expenseMonitoringService.updateExchangeRates())
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/exchange-rates"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }


}

