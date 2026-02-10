package com.recommend.application.port.provided.persistence;

import com.recommend.domain.model.MemberRecommendation;
import com.recommend.domain.model.ids.MemberId;

import java.util.Optional;

public interface MemberRecommendationRepository {
    Optional<MemberRecommendation> findByMemberId(MemberId memberId);
    MemberRecommendation save(MemberRecommendation recommendation);
}
