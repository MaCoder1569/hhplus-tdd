package io.hhplus.tdd.point.domain;

import java.util.List;

public interface PointHistoryRepository {
    List<PointHistory> findAllByUserId(long userId);
    PointHistory insert(long userId, long amount, TransactionType type, long updateMillis);
}
