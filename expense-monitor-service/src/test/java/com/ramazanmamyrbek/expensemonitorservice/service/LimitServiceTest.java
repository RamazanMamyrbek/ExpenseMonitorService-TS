package com.ramazanmamyrbek.expensemonitorservice.service;

import com.ramazanmamyrbek.expensemonitorservice.dto.response.LimitResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.entity.Limit;
import com.ramazanmamyrbek.expensemonitorservice.repository.LimitRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
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
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LimitServiceTest {

    @Autowired
    LimitRepository limitRepository;

    @Autowired
    ExchangeRateService exchangeRateService;
    @Autowired
    LimitService limitService;

    @BeforeEach
    void beforeEach() throws InterruptedException {
        exchangeRateService.updateExchangeRates();

        Limit limit1 = new Limit();
        limit1.setLimitSum(new BigDecimal("1000.00"));
        limit1.setLimitCurrencyShortname("USD");
        limit1.setExpenseCategory("service");
        limit1.setLimitSumUsd(new BigDecimal("1000.00"));
        limit1.setLimitDatetime(ZonedDateTime.parse("2024-08-01T04:00:00Z")); // UTC

        Limit limit2 = new Limit();
        limit2.setLimitSum(new BigDecimal("2000.00"));
        limit2.setLimitCurrencyShortname("EUR");
        limit2.setLimitSumUsd(new BigDecimal("2200.00"));
        limit2.setExpenseCategory("service");
        limit2.setLimitDatetime(ZonedDateTime.parse("2024-08-02T04:00:00Z")); // UTC

        Limit limit3 = new Limit();
        limit3.setLimitSum(new BigDecimal("1500.00"));
        limit3.setLimitCurrencyShortname("KZT");
        limit3.setExpenseCategory("product");
        limit3.setLimitSumUsd(new BigDecimal("3.4"));
        limit3.setLimitDatetime(ZonedDateTime.parse("2024-08-03T04:00:00Z")); // UTC
        limitRepository.saveAll(List.of(limit1, limit2, limit3));
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getAllLimitsTest() {
        List<LimitResponseDto> limitResponseDtoList = new ArrayList<>();

        LimitResponseDto limitResponseDto1 = new LimitResponseDto();
        limitResponseDto1.setId(1L);
        limitResponseDto1.setLimitSum(new BigDecimal("1000.00"));
        limitResponseDto1.setLimitCurrencyShortname("USD");
        limitResponseDto1.setLimitSumUsd(new BigDecimal("1000.00"));
        limitResponseDto1.setExpenseCategory("service");
        limitResponseDto1.setLimitDatetime(ZonedDateTime.parse("2024-08-01T04:00:00Z")); // UTC

        limitResponseDtoList.add(limitResponseDto1);

        LimitResponseDto limitResponseDto2 = new LimitResponseDto();
        limitResponseDto2.setId(2L);
        limitResponseDto2.setLimitSum(new BigDecimal("2000.00"));
        limitResponseDto2.setLimitCurrencyShortname("EUR");
        limitResponseDto2.setLimitSumUsd(new BigDecimal("2200.00"));
        limitResponseDto2.setExpenseCategory("service");
        limitResponseDto2.setLimitDatetime(ZonedDateTime.parse("2024-08-02T04:00:00Z")); // UTC

        limitResponseDtoList.add(limitResponseDto2);

        LimitResponseDto limitResponseDto3 = new LimitResponseDto();
        limitResponseDto3.setId(3L);
        limitResponseDto3.setLimitSum(new BigDecimal("1500.00"));
        limitResponseDto3.setLimitCurrencyShortname("KZT");
        limitResponseDto3.setLimitSumUsd(new BigDecimal("3.40"));
        limitResponseDto3.setExpenseCategory("product");
        limitResponseDto3.setLimitDatetime(ZonedDateTime.parse("2024-08-03T04:00:00Z")); // UTC
        limitResponseDtoList.add(limitResponseDto3);

        assertEquals(limitResponseDtoList, limitService.getAllLimits());
    }

    @Test
    void createLimitTestWithValidArguments() {

    }



}
