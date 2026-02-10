package com.lxpbe.common.security;

import com.lxpbe.auth.domain.exception.AuthErrorCode;
import com.lxpbe.common.exception.DomainException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginUserArgumentResolverTest {

    private final LoginUserArgumentResolver resolver = new LoginUserArgumentResolver();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("@LoginUser Long 파라미터를 지원한다")
    void supportsParameter_withLoginUserAndLong_returnsTrue() {
        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.hasParameterAnnotation(LoginUser.class)).thenReturn(true);
        when(parameter.getParameterType()).thenReturn((Class) Long.class);

        assertThat(resolver.supportsParameter(parameter)).isTrue();
    }

    @Test
    @DisplayName("@LoginUser 없는 파라미터는 지원하지 않는다")
    void supportsParameter_withoutLoginUser_returnsFalse() {
        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.hasParameterAnnotation(LoginUser.class)).thenReturn(false);
        when(parameter.getParameterType()).thenReturn((Class) Long.class);

        assertThat(resolver.supportsParameter(parameter)).isFalse();
    }

    @Test
    @DisplayName("Long 타입이 아닌 파라미터는 지원하지 않는다")
    void supportsParameter_withNonLongType_returnsFalse() {
        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.hasParameterAnnotation(LoginUser.class)).thenReturn(true);
        when(parameter.getParameterType()).thenReturn((Class) String.class);

        assertThat(resolver.supportsParameter(parameter)).isFalse();
    }

    @Test
    @DisplayName("SecurityContext에서 userId를 추출한다")
    void resolveArgument_withAuthentication_returnsUserId() {
        Long userId = 1L;
        var authentication = new UsernamePasswordAuthenticationToken(userId, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MethodParameter parameter = mock(MethodParameter.class);
        Object result = resolver.resolveArgument(parameter, null, mock(), null);

        assertThat(result).isEqualTo(userId);
    }

    @Test
    @DisplayName("인증 정보가 없으면 UNAUTHORIZED 예외를 발생시킨다")
    void resolveArgument_withoutAuthentication_throwsException() {
        MethodParameter parameter = mock(MethodParameter.class);

        assertThatThrownBy(() -> resolver.resolveArgument(parameter, null, mock(), null))
                .isInstanceOf(DomainException.class)
                .satisfies(ex -> assertThat(((DomainException) ex).errorCode())
                        .isEqualTo(AuthErrorCode.UNAUTHORIZED));
    }

    @Test
    @DisplayName("principal이 Long 타입이 아니면 UNAUTHORIZED 예외를 발생시킨다")
    void resolveArgument_withNonLongPrincipal_throwsException() {
        var authentication = new UsernamePasswordAuthenticationToken("notALong", null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MethodParameter parameter = mock(MethodParameter.class);

        assertThatThrownBy(() -> resolver.resolveArgument(parameter, null, mock(), null))
                .isInstanceOf(DomainException.class)
                .satisfies(ex -> assertThat(((DomainException) ex).errorCode())
                        .isEqualTo(AuthErrorCode.UNAUTHORIZED));
    }
}
