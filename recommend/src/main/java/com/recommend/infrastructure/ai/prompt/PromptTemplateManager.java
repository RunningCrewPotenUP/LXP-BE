package com.recommend.infrastructure.ai.prompt;

import com.recommend.domain.model.PersonaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 페르소나별 프롬프트 템플릿 관리
 */
@Component
public class PromptTemplateManager {

    private final Map<PersonaType, String> templates = new HashMap<>();

    public PromptTemplateManager() {
        initializeTemplates();
    }

    /**
     * 페르소나에 맞는 프롬프트 템플릿 반환
     */
    public String getTemplate(PersonaType persona) {
        return templates.getOrDefault(persona, templates.get(PersonaType.BACKEND));
    }

    /**
     * 6개 페르소나 프롬프트 초기화
     */
    private void initializeTemplates() {
        templates.put(PersonaType.BACKEND, BACKEND_PROMPT);
        templates.put(PersonaType.FRONTEND, FRONTEND_PROMPT);
        templates.put(PersonaType.FULLSTACK, FULLSTACK_PROMPT);
        templates.put(PersonaType.DEVOPS, DEVOPS_PROMPT);
        templates.put(PersonaType.UIUX, UIUX_PROMPT);
        templates.put(PersonaType.AIML, AIML_PROMPT);
    }

    // === 페르소나별 프롬프트 상수 ===

    private static final String BACKEND_PROMPT = """
        백엔드 개발자로서 다음 단계로 학습하면 실무 역량 향상에 도움이 될 기술 영역을 
        3가지 카테고리로 추천해주세요:
        
        1. **인프라/배포** (예: Docker, Kubernetes, CI/CD)
        2. **모니터링/관측성** (예: Prometheus, Grafana, ELK)
        3. **클라우드 플랫폼** (예: AWS, GCP, Azure)
        
        각 카테고리당 2-3개 키워드만 제시하세요.
        백엔드 워크플로우에서 직접 사용하는 기술에 집중해주세요.
        """;

    private static final String FRONTEND_PROMPT = """
        프론트엔드 개발자로서 다음 단계로 학습하면 실무 역량 향상에 도움이 될 기술 영역을 
        3가지 카테고리로 추천해주세요:
        
        1. **상태 관리/성능** (예: Redux, Recoil, 웹 성능 최적화)
        2. **테스트/품질** (예: Jest, Cypress, Storybook)
        3. **빌드/배포** (예: Webpack, Vite, Vercel)
        
        각 카테고리당 2-3개 키워드만 제시하세요.
        React/Vue 생태계와 직접 연관된 기술에 집중해주세요.
        """;

    private static final String FULLSTACK_PROMPT = """
        풀스택 개발자로서 다음 단계로 학습하면 프로젝트 완성도를 높일 수 있는 기술 영역을 
        4가지 카테고리로 추천해주세요:
        
        1. **인증/보안** (예: JWT, OAuth, HTTPS)
        2. **배포/호스팅** (예: Docker, Heroku, Nginx)
        3. **API 설계** (예: REST, GraphQL, API 문서화)
        4. **데이터베이스 고급** (예: 인덱싱, 트랜잭션, 캐싱)
        
        각 카테고리당 2-3개 키워드만 제시하세요.
        풀스택 프로젝트에서 바로 적용 가능한 기술에 집중해주세요.
        """;

    private static final String DEVOPS_PROMPT = """
        백엔드 개발자가 DevOps 역량을 키우기 위해 학습하면 좋을 기술 영역을 
        4가지 카테고리로 추천해주세요:
        
        1. **컨테이너 오케스트레이션** (예: Kubernetes, Helm, Istio)
        2. **CI/CD 파이프라인** (예: Jenkins, GitHub Actions, ArgoCD)
        3. **인프라 자동화** (예: Terraform, Ansible, CloudFormation)
        4. **모니터링/로깅** (예: Prometheus, Grafana, ELK Stack)
        
        각 카테고리당 2-3개 키워드만 제시하세요.
        Spring Boot/Node.js 애플리케이션 운영에 실질적으로 필요한 기술에 집중해주세요.
        """;

    private static final String UIUX_PROMPT = """
        UI/UX 디자이너로서 다음 단계로 학습하면 실무 역량 향상에 도움이 될 기술 영역을 
        3가지 카테고리로 추천해주세요:
        
        1. **프론트엔드 기초** (예: HTML, CSS, JavaScript 기초)
        2. **디자인 시스템/협업** (예: Design System, Component Library, Storybook)
        3. **프로토타이핑/도구** (예: Framer, Principle, After Effects)
        
        각 카테고리당 2-3개 키워드만 제시하세요.
        디자이너가 개발팀과 협업할 때 유용한 기술에 집중해주세요.
        """;

    private static final String AIML_PROMPT = """
        AI/ML 엔지니어로서 다음 단계로 학습하면 실무 역량 향상에 도움이 될 기술 영역을 
        4가지 카테고리로 추천해주세요:
        
        1. **모델 배포** (예: FastAPI, Docker, MLOps)
        2. **딥러닝 프레임워크** (예: PyTorch, Hugging Face, ONNX)
        3. **데이터 처리** (예: Spark, 데이터 증강, Feature Engineering)
        4. **클라우드 ML** (예: AWS SageMaker, GCP Vertex AI, Azure ML)
        
        각 카테고리당 2-3개 키워드만 제시하세요.
        실무 AI/ML 프로젝트에서 필수적인 기술에 집중해주세요.
        """;
}
