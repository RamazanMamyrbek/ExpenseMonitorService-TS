package com.ramazanmamyrbek.expensemonitorservice.repository;

import com.ramazanmamyrbek.expensemonitorservice.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findExchangeRateByCurrencyPair(String currencyPair);
}
