package com.lxpbe.auth.application.service;

import com.lxpbe.auth.application.dto.LoginDto;
import com.lxpbe.auth.application.dto.RegisterDto;
import com.lxpbe.auth.domain.exception.AuthErrorCode;
import com.lxpbe.auth.presentation.response.TokenResponse;
import com.lxpbe.common.exception.DomainException;
import com.lxpbe.common.security.JwtTokenProvider;
import com.lxpbe.user.domain.User;
import com.lxpbe.user.domain.exception.UserErrorCode;
import com.lxpbe.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void register(RegisterDto dto) {
        validateDuplicateEmail(dto.email());

        User user = User.create(
                dto.email(),
                passwordEncoder.encode(dto.password()),
                dto.name(),
                dto.role(),
                dto.level(),
                dto.tagIds()
        );

        userRepository.save(user);
    }

    @Transactional
    public TokenResponse login(LoginDto dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new DomainException(AuthErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new DomainException(AuthErrorCode.LOGIN_FAILED);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getRoles());
        return new TokenResponse(accessToken);
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DomainException(UserErrorCode.DUPLICATE_EMAIL);
        }
    }
}
