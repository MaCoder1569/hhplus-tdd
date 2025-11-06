package io.hhplus.tdd.point.application;

import io.hhplus.tdd.point.domain.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PointService {
    private static final Logger log = LoggerFactory.getLogger(PointService.class);

    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public UserPoint charge(long id, long amount) throws Exception {
        validateId(id, amount);

        long point = amount;
        UserPoint userPoint = userPointRepository.findById(id);

        if (!Objects.isNull(userPoint)) {
            point += userPoint.point();
        }

        pointHistoryRepository.insert(id, amount, TransactionType.CHARGE);
        return userPointRepository.insertOrUpdate(id, point);
    }

    public UserPoint use(long id, long amount) throws Exception {
        validateId(id, amount);

        long point = amount;
        UserPoint userPoint = userPointRepository.findById(id);

        point = userPoint.point() - point;

        if (point < 0L) {
            log.error("사용 포인트 보다 남은 포인트가 적습니다. amount={}, point={}", amount, userPoint.point());
            throw new Exception("사용 포인트 보다 남은 포인트가 적습니다.");
        }

        pointHistoryRepository.insert(id, amount, TransactionType.USE);
        return userPointRepository.insertOrUpdate(id, point);
    }

    public UserPoint getPoint(long id) throws Exception {
        validateId(id);
        return userPointRepository.findById(id);
    }

    public List<PointHistory> getPointHistories(long id) throws Exception {
        validateId(id);
        return pointHistoryRepository.findAllByUserId(id);
    }

    private static void validateId(long id) throws Exception {
        if(id < 0L){
            log.error("id 값은 0보다 작을 수 없습니다. id={}", id);
            throw new Exception("id 값은 0보다 작을 수 없습니다.");
        }
    }

    private static void validateId(long id, long amount) throws Exception {
        validateId(id);
        if(amount < 0L){
            log.error("amount 값은 0보다 작을 수 없습니다. amount={}", amount);
            throw new Exception("amount 값은 0보다 작을 수 없습니다.");
        }
    }

    //TODO 사용자 포인트 추가, 사용 부터 구현
}
