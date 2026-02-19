package com.recommend.application.service;

import com.recommend.domain.model.PersonaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

//(페르소나 자동 감지)
/**
 * 사용자 태그 기반 페르소나 자동 감지
 */
@Slf4j
@Service
public class PersonaDetectionService {

    // 페르소나별 시그니처 태그 매핑
    private static final Map<PersonaType, SignatureTags> SIGNATURE_TAGS = new HashMap<>();

    static {
        // BACKEND
        SIGNATURE_TAGS.put(PersonaType.BACKEND, new SignatureTags(
                Set.of("Java", "Spring", "Spring Boot", "Node.js", "Express", "Python", "Django", "Flask", "Go", "Rust"),
                Set.of("API", "REST", "GraphQL", "MySQL", "PostgreSQL", "MongoDB", "Redis"),
                3  // 필수 태그 가중치
        ));

        // FRONTEND
        SIGNATURE_TAGS.put(PersonaType.FRONTEND, new SignatureTags(
                Set.of("React", "Vue", "Angular", "Next.js", "Nuxt.js", "Svelte"),
                Set.of("TypeScript", "JavaScript", "HTML", "CSS", "Tailwind", "styled-components"),
                3
        ));

        // FULLSTACK (특별 조건: React/Vue + Node.js 동시 보유)
        SIGNATURE_TAGS.put(PersonaType.FULLSTACK, new SignatureTags(
                Set.of("React", "Vue", "Angular"),  // 프론트엔드 필수
                Set.of("Node.js", "Express", "MongoDB", "MERN", "Fullstack"),  // 백엔드 필수
                5  // 양쪽 모두 필요하므로 높은 가중치
        ));

        // DEVOPS
        SIGNATURE_TAGS.put(PersonaType.DEVOPS, new SignatureTags(
                Set.of("Docker", "Kubernetes", "Jenkins", "CI/CD", "AWS", "GCP", "Azure"),
                Set.of("Terraform", "Ansible", "Prometheus", "Grafana", "ELK", "Nginx", "Linux", "DevOps"),
                4
        ));

        // UIUX
        SIGNATURE_TAGS.put(PersonaType.UIUX, new SignatureTags(
                Set.of("Figma", "Sketch", "Adobe XD", "UI/UX", "디자인"),
                Set.of("Prototyping", "Wireframe", "User Research", "Design System", "디자인 씽킹"),
                4
        ));

        // AIML
        SIGNATURE_TAGS.put(PersonaType.AIML, new SignatureTags(
                Set.of("TensorFlow", "PyTorch", "Machine Learning", "Deep Learning", "AI"),
                Set.of("NumPy", "Pandas", "Scikit-learn", "Keras", "Hugging Face", "NLP", "Computer Vision", "MLOps"),
                4
        ));
    }

    /**
     * 사용자 태그로부터 페르소나 감지
     *
     * @param userTags 사용자 관심 태그 + 수강 중 강좌 태그 합집합
     * @return 감지된 페르소나 (실패 시 BACKEND 기본값)
     */
    public PersonaType detectPersona(Set<String> userTags) {
        if (userTags == null || userTags.isEmpty()) {
            log.warn("[Persona] Empty user tags, using default BACKEND");
            return PersonaType.BACKEND;
        }

        log.debug("[Persona] Detecting persona from tags: {}", userTags);

        // 대소문자 무시 처리
        Set<String> normalizedTags = normalizeTagSet(userTags);

        // 페르소나별 점수 계산
        Map<PersonaType, Double> scores = new EnumMap<>(PersonaType.class);

        for (Map.Entry<PersonaType, SignatureTags> entry : SIGNATURE_TAGS.entrySet()) {
            PersonaType persona = entry.getKey();
            SignatureTags signature = entry.getValue();

            double score = calculateScore(normalizedTags, signature);
            scores.put(persona, score);

            log.debug("[Persona] {} score: {}", persona, score);
        }

        // 최고점 페르소나 선택
        PersonaType detected = scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(PersonaType.BACKEND);

        // 점수가 너무 낮으면 기본값
        if (scores.get(detected) < 3.0) {
            log.warn("[Persona] Low score ({}), using default BACKEND", scores.get(detected));
            return PersonaType.BACKEND;
        }

        log.info("[Persona] Detected persona: {} (score: {})", detected, scores.get(detected));
        return detected;
    }

    /**
     * 페르소나별 점수 계산
     */
    private double calculateScore(Set<String> userTags, SignatureTags signature) {
        double score = 0.0;

        // 필수 태그 매칭 (높은 가중치)
        for (String tag : signature.primaryTags) {
            if (userTags.contains(tag.toLowerCase())) {
                score += signature.weight;
            }
        }

        // 선택 태그 매칭 (낮은 가중치)
        for (String tag : signature.secondaryTags) {
            if (userTags.contains(tag.toLowerCase())) {
                score += 2.0;
            }
        }

        return score;
    }

    /**
     * 태그 정규화 (소문자 변환)
     */
    private Set<String> normalizeTagSet(Set<String> tags) {
        Set<String> normalized = new HashSet<>();
        for (String tag : tags) {
            normalized.add(tag.toLowerCase().trim());
        }
        return normalized;
    }

    /**
     * 시그니처 태그 클래스
     */
    private record SignatureTags(
            Set<String> primaryTags,
            Set<String> secondaryTags,
            int weight
    ) {}
}
