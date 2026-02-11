package com.lxpbe.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lxpbe.auth.domain.exception.AuthErrorCode;
import com.lxpbe.common.exception.ErrorBody;
import com.lxpbe.common.exception.ErrorCode;
import com.lxpbe.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ErrorCode errorCode = (ErrorCode) request.getAttribute(JwtAuthenticationFilter.AUTH_ERROR_ATTRIBUTE);

        if (errorCode == null) {
            errorCode = AuthErrorCode.UNAUTHORIZED;
        }

        response.setStatus(errorCode.httpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorBody errorBody = new ErrorBody(errorCode.code(), errorCode.message());
        ApiResponse<Void> body = new ApiResponse<>(null, errorBody);
        objectMapper.writeValue(response.getWriter(), body);
    }
}
