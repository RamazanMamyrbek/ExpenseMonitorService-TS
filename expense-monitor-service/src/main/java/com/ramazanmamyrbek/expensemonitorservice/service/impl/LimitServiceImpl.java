package com.ramazanmamyrbek.expensemonitorservice.service.impl;

import com.ramazanmamyrbek.expensemonitorservice.dto.request.LimitCreateRequestDto;
import com.ramazanmamyrbek.expensemonitorservice.dto.response.LimitResponseDto;
import com.ramazanmamyrbek.expensemonitorservice.entity.Limit;
import com.ramazanmamyrbek.expensemonitorservice.exception.BadRequestException;
import com.ramazanmamyrbek.expensemonitorservice.repository.LimitRepository;
import com.ramazanmamyrbek.expensemonitorservice.service.ExchangeRateService;
import com.ramazanmamyrbek.expensemonitorservice.service.LimitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class LimitServiceImpl implements LimitService {
    final LimitRepository limitRepository;
    final ModelMapper modelMapper;
    final ExchangeRateService exchangeRateService;

    @Override
    public List<LimitResponseDto> getAllLimitsByAccount(Long accountFrom) {
        return limitRepository.findAllByAccountFrom(accountFrom).stream()
                .map(limit -> modelMapper.map(limit, LimitResponseDto.class))
                .toList();
    }

    @Override
    @Transactional
    public LimitResponseDto createLimit(LimitCreateRequestDto limitCreateRequestDto) {
        Limit limit = modelMapper.map(limitCreateRequestDto, Limit.class);
        limit.setLimitSumUsd(exchangeRateService.toUsd(limit.getLimitSum(), limitCreateRequestDto.getLimitCurrencyShortname()));
        limitRepository.save(limit);
        return modelMapper.map(limit, LimitResponseDto.class);
    }

    @Override
    public Optional<Limit> getLatestLimitByExpenseCategoryAndAccount(String expenseCategory, Long accountFrom) {
        return limitRepository.findLatestLimitByExpenseCategoryAndAccountFrom(expenseCategory, accountFrom);
    }

    @Override
    public void save(Limit limit) {
        limitRepository.save(limit);
    }

}
