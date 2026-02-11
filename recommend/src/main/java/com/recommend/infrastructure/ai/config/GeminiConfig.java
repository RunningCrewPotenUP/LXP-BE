package com.recommend.infrastructure.ai.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Google Gemini LLM 설정
 */
@Configuration
public class GeminiConfig {

    @Value("${langchain4j.google-ai-gemini.api-key}")
    private String apiKey;

    @Value("${langchain4j.google-ai-gemini.model-name}")
    private String modelName;

    @Value("${langchain4j.google-ai-gemini.temperature}")
    private Double temperature;

    @Value("${langchain4j.google-ai-gemini.max-output-tokens}")
    private Integer maxOutputTokens;

    @Value("${langchain4j.google-ai-gemini.timeout}")
    private Duration timeout;

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(temperature)
                .maxOutputTokens(maxOutputTokens)
                .timeout(timeout)
                .logRequestsAndResponses(true)  // 개발 단계에서 활성화
                .build();
    }
}
