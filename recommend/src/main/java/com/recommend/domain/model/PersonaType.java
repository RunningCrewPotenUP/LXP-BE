package com.recommend.domain.model;

/**
 * 학습자 페르소나 유형
 * AI 프롬프트 선택 및 추천 전략에 사용
 */
public enum PersonaType {

    /**
     * 백엔드 개발자 (기본)
     * 시그니처 태그: Java, Spring Boot, Node.js, Python Backend
     */
    BACKEND("백엔드 개발자"),

    /**
     * 프론트엔드 개발자
     * 시그니처 태그: React, Vue, Angular, Next.js
     */
    FRONTEND("프론트엔드 개발자"),

    /**
     * 풀스택 개발자 (MERN Stack)
     * 조건: React + Node.js 동시 보유
     */
    FULLSTACK("풀스택 개발자"),

    /**
     * DevOps 지향 백엔드 개발자
     * 시그니처 태그: Docker, Kubernetes, Jenkins, AWS
     */
    DEVOPS("DevOps 지향 백엔드 개발자"),

    /**
     * UI/UX 디자이너
     * 시그니처 태그: Figma, Sketch, Adobe XD, UI/UX
     */
    UIUX("UI/UX 디자이너"),

    /**
     * AI/ML 엔지니어
     * 시그니처 태그: TensorFlow, PyTorch, Machine Learning
     */
    AIML("AI/ML 엔지니어");

    private final String description;

    PersonaType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
