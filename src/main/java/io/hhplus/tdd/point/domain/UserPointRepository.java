package io.hhplus.tdd.point.domain;

public interface UserPointRepository {
    UserPoint findById(Long id);
    UserPoint insertOrUpdate(long id, long amount);
}
