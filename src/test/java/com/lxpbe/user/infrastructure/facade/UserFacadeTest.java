package com.lxpbe.user.infrastructure.facade;

import com.lxpbe.user.application.service.UserQueryService;
import com.lxpbe.user.domain.enums.Level;
import com.lxpbe.user.infrastructure.facade.dto.UserProfileDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserFacade 테스트")
class UserFacadeTest {

    @InjectMocks
    private UserFacade userFacade;

    @Mock
    private UserQueryService userQueryService;

    @Test
    @DisplayName("존재하는 사용자 프로필을 조회하면 UserProfileDto를 반환한다")
    void getProfile_withExistingUser_returnsUserProfileDto() {
        Long userId = 1L;
        UserProfileDto expected = new UserProfileDto(userId, List.of("Java", "Spring", "Database"), Level.JUNIOR);
        when(userQueryService.findById(userId)).thenReturn(Optional.of(expected));

        Optional<UserProfileDto> result = userFacade.getProfile(userId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 프로필을 조회하면 빈 Optional을 반환한다")
    void getProfile_withNonExistingUser_returnsEmpty() {
        Long userId = 999L;
        when(userQueryService.findById(userId)).thenReturn(Optional.empty());

        Optional<UserProfileDto> result = userFacade.getProfile(userId);

        assertThat(result).isEmpty();
    }
}
