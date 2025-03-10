package com.ramazanmamyrbek.expensemonitorservice.repository;

import com.ramazanmamyrbek.expensemonitorservice.entity.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {

    @Query("SELECT l FROM Limit l WHERE l.expenseCategory = :expenseCategory AND l.accountFrom = :accountFrom ORDER BY l.limitDatetime DESC LIMIT 1")
    Optional<Limit> findLatestLimitByExpenseCategoryAndAccountFrom(String expenseCategory, Long accountFrom);

    List<Limit> findAllByAccountFrom(Long accountFrom);
}
