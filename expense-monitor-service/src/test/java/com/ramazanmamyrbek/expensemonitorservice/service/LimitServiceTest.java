package com.ramazanmamyrbek.expensemonitorservice.service;

import com.ramazanmamyrbek.expensemonitorservice.dto.request.LimitCreateRequestDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.response.LimitResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.entity.Limit;
import com.ramazanmamyrbek.expensemonitorservice.repository.ExchangeRateRepository;
import com.ramazanmamyrbek.expensemonitorservice.repository.LimitRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootTest(classes = {ExpenseMonitorServiceApplication.class, H2JpaConfig.class})
@SpringBootTest
@ActiveProfiles("test")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LimitServiceTest {

    @Autowired
    LimitRepository limitRepository;

    @Autowired
    ExchangeRateService exchangeRateService;
    @Autowired
    ExchangeRateRepository exchangeRateRepository;
    @Autowired
    LimitService limitService;

    @BeforeEach
    void beforeEach() throws InterruptedException {
        exchangeRateRepository.deleteAll();
        exchangeRateService.updateExchangeRates();
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void afterEach() {
        limitRepository.deleteAll();
        exchangeRateRepository.deleteAll();
    }



    @Test
    void getAllLimitsTest() {
        Limit limit1 = new Limit();
        limit1.setLimitSum(new BigDecimal("1000.00"));
        limit1.setLimitCurrencyShortname("USD");
        limit1.setExpenseCategory("service");
        limit1.setLimitSumUsd(new BigDecimal("1000.00"));
        limit1.setLimitDatetime(ZonedDateTime.parse("2024-08-01T04:00:00Z")); // UTC
        limitRepository.save(limit1);

        List<LimitResponseDto> limitResponseDtoList = new ArrayList<>();
        LimitResponseDto limitResponseDto1 = new LimitResponseDto();
        limitResponseDto1.setId(1L);
        limitResponseDto1.setLimitSum(new BigDecimal("1000.00"));
        limitResponseDto1.setLimitCurrencyShortname("USD");
        limitResponseDto1.setLimitSumUsd(new BigDecimal("1000.00"));
        limitResponseDto1.setExpenseCategory("service");
        limitResponseDto1.setLimitDatetime(ZonedDateTime.parse("2024-08-01T04:00:00Z")); // UTC
        limitResponseDtoList.add(limitResponseDto1);

        List<LimitResponseDto> limitResponseDtoListActual = limitService.getAllLimits();

        for(int i = 0; i < limitResponseDtoListActual.size(); i++) {
            LimitResponseDto expected = limitResponseDtoList.get(i);
            LimitResponseDto actual = limitResponseDtoListActual.get(i);
            assertEquals(expected.getLimitSum(), actual.getLimitSum());
            assertEquals(expected.getLimitCurrencyShortname(), actual.getLimitCurrencyShortname());
            assertEquals(expected.getExpenseCategory(), actual.getExpenseCategory());
        }
    }

    @Test
    void createLimitTestWithValidArguments() {
        LimitCreateRequestDto limitCreateRequestDto = new LimitCreateRequestDto();
        limitCreateRequestDto.setLimitCurrencyShortname("USD");
        limitCreateRequestDto.setLimitSum(new BigDecimal(2000.00));
        limitCreateRequestDto.setExpenseCategory("product");

        LimitResponseDto limitResponseDtoExpected = new LimitResponseDto();
        limitResponseDtoExpected.setId(1L);
        limitResponseDtoExpected.setLimitSum(new BigDecimal(2000.00));
        limitResponseDtoExpected.setLimitDatetime(ZonedDateTime.now());
        limitResponseDtoExpected.setLimitSumUsd(new BigDecimal(2000.00));
        limitResponseDtoExpected.setLimitCurrencyShortname("USD");
        limitResponseDtoExpected.setExpenseCategory("product");

        LimitResponseDto limitResponseDtoActual = limitService.createLimit(limitCreateRequestDto);
        assertEquals(limitResponseDtoExpected.getLimitSum(), limitResponseDtoActual.getLimitSum());
        assertEquals(limitResponseDtoExpected.getLimitSumUsd(), limitResponseDtoActual.getLimitSumUsd());
        assertEquals(limitResponseDtoExpected.getLimitCurrencyShortname(), limitResponseDtoActual.getLimitCurrencyShortname());
        assertEquals(limitResponseDtoExpected.getExpenseCategory(), limitResponseDtoActual.getExpenseCategory());
    }





}
