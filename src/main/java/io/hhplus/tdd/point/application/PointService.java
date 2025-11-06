package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.domain.PointHistoryRepository;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.domain.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {
    private static final Logger log = LoggerFactory.getLogger(PointService.class);

    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public UserPoint saveUserPoint(long id, long amount) throws Exception {
        if(id < 0L){
            log.error("id 값은 0보다 작을 수 없습니다. id={}", id);
            throw new Exception("id 값은 0보다 작을 수 없습니다.");
        }

        if(amount < 0L){
            log.error("amount 값은 0보다 작을 수 없습니다. amount={}", id);
            throw new Exception("amount 값은 0보다 작을 수 없습니다.");
        }

        return userPointRepository.insertOrUpdate(id, amount);
    }

    public UserPoint getPoint(long id) throws Exception {
        if(id < 0L){
            log.error("id 값은 0보다 작을 수 없습니다. id={}", id);
            throw new Exception("id 값은 0보다 작을 수 없습니다.");
        }

        return userPointRepository.findById(id);
    }
}
