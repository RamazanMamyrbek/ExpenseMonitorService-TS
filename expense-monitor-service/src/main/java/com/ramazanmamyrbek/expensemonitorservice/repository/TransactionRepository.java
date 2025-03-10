package com.ramazanmamyrbek.expensemonitorservice.repository;

import com.ramazanmamyrbek.expensemonitorservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByLimitExceededAndAccountFrom(boolean limitExceeded, Long accountId);

    List<Transaction> findAllByDatetimeAfter(ZonedDateTime dateTime);

    @Query("SELECT t from Transaction t where t.expenseCategory = :expenseCategory AND t.accountFrom = :accountFrom ORDER BY t.datetime DESC, t.id DESC LIMIT 1")
    Optional<Transaction> findLatestTransactionByExpenseCategoryAndAccount(String expenseCategory, Long accountFrom);

}
