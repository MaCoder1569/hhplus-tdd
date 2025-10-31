package io.hhplus.tdd.point.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRepository {
    UserPoint findById(Long id);
    UserPoint insertOrUpdate(long id, long amount);
}
