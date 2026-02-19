package com.recommend.infrastructure.web;

import com.lxpbe.common.security.LoginUser;
import com.recommend.application.service.RecommendCommandService;
import com.recommend.application.service.RecommendQueryService;
import com.recommend.infrastructure.web.dto.response.RecommendationListResponse;
import com.recommend.infrastructure.web.dto.response.RecommendedCourseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 추천 서비스 API
 * 인증된 사용자의 추천 강좌를 조회하고 갱신하는 엔드포인트
 */
@Slf4j
@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendCommandService commandService;
    private final RecommendQueryService queryService;

    /**
     * 내 추천 강좌 목록 조회
     * GET /recommend/me
     *
     * @param userId Spring Security에서 주입된 인증된 사용자 ID
     * @return 추천 강좌 목록
     */
    @GetMapping("/me")
    public ResponseEntity<RecommendationListResponse> getMyRecommendations(
            @LoginUser Long userId
    ) {
        log.info("[추천 조회] userId={}", userId);

        // 추천 조회 (Repository에서 조회만)
        List<RecommendedCourseResponse> responses = queryService.getTopRecommendations(userId);

        return ResponseEntity.ok(RecommendationListResponse.from(responses));
    }

    /**
     * 추천 목록 갱신 (명시적 호출)
     * POST /recommend/refresh
     *
     * @param userId Spring Security에서 주입된 인증된 사용자 ID
     * @return 204 No Content
     */
    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshRecommendation(
            @LoginUser Long userId  //
    ) {
        log.info("[추천 갱신] userId={}", userId);

        // 추천 재계산 (Facade 호출하여 계산)
        commandService.refreshRecommendations(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
