package com.recommend.infrastructure.batch.reader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * 추천 대상 학습자 ID 조회 Reader
 * 전체 학습자 목록을 조회하여 순차 반환
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LearnerIdReader implements ItemReader<Long> {  // ✅ String → Long 변경

    private final JdbcTemplate jdbcTemplate;
    private Iterator<Long> learnerIdIterator;  // ✅ String → Long 변경
    private boolean initialized = false;

    /**
     * 학습자 ID를 하나씩 반환
     * 첫 호출 시 전체 학습자 조회 후 Iterator로 순회
     *
     * @return 다음 학습자 ID (Long), 없으면 null (배치 종료 신호)
     */
    @Override
    public Long read() {  // ✅ String → Long 변경
        if (!initialized) {
            List<Long> learnerIds = fetchAllLearnerIds();  // ✅ String → Long 변경
            log.info("[Batch Reader] Loaded {} learners", learnerIds.size());
            learnerIdIterator = learnerIds.iterator();
            initialized = true;
        }

        if (learnerIdIterator.hasNext()) {
            return learnerIdIterator.next();
        } else {
            initialized = false;
            learnerIdIterator = null;
            return null;
        }
    }

    /**
     * DB에서 전체 학습자 ID 조회
     * member_recommendations 테이블에서 DISTINCT member_id 추출
     *
     * TODO: Member BC Facade 호출로 변경 가능
     */
    private List<Long> fetchAllLearnerIds() {  // ✅ String → Long 변경
        String sql = "SELECT DISTINCT member_id FROM member_recommendations ORDER BY member_id";
        return jdbcTemplate.queryForList(sql, Long.class);  // ✅ String.class → Long.class 변경
    }
}
