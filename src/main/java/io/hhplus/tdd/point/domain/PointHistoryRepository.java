package io.hhplus.tdd.point.domain;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointHistoryRepository {
    List<PointHistory> findAllByUserId(long userId);
    PointHistory insert(long userId, long amount, TransactionType type, long updateMillis);
}
