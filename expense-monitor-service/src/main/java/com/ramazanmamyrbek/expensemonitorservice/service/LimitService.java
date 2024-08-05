package com.ramazanmamyrbek.expensemonitorservice.service;

import com.ramazanmamyrbek.expensemonitorservice.dto.request.LimitCreateRequestDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.response.LimitResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.entity.Limit;

import java.util.List;
import java.util.Optional;

public interface LimitService {
    List<LimitResponseDto> getAllLimits();

    LimitResponseDto createLimit(LimitCreateRequestDto limitCreateRequestDto);

    Optional<Limit> getLatestLimitByExpenseCategory(String expenseCategory);

    void save(Limit limit);
}
