package com.lxpbe.user.application.service;

import com.lxpbe.tag.domain.Tag;
import com.lxpbe.tag.domain.enums.TagStatus;
import com.lxpbe.tag.repository.TagRepository;
import com.lxpbe.user.domain.User;
import com.lxpbe.user.domain.enums.Level;
import com.lxpbe.user.infrastructure.facade.dto.UserProfileDto;
import com.lxpbe.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
class UserQueryServiceTest {

    @InjectMocks
    private UserQueryService userQueryService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagRepository tagRepository;

    @Nested
    @DisplayName("internal facade 테스트")
    class InternalFacade {

        @Test
        @DisplayName("존재하는 사용자 ID로 조회하면 UserProfileDto를 반환한다")
        void findById_withExistingUser_returnsUserProfileDto() {
            Long userId = 1L;
            List<Long> tagIds = List.of(10L, 20L, 30L);

            User user = mockUser(userId, tagIds, Level.JUNIOR);

            Tag tag1 = Tag.builder().name("Java").category("언어").subCategory("백엔드").status(TagStatus.ACTIVE).build();
            Tag tag2 = Tag.builder().name("Spring").category("프레임워크").subCategory("백엔드").status(TagStatus.ACTIVE)
                    .build();
            Tag tag3 = Tag.builder().name("Database").category("인프라").subCategory("데이터").status(TagStatus.ACTIVE)
                    .build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(tagRepository.findAllByIdIn(tagIds)).thenReturn(List.of(tag1, tag2, tag3));

            Optional<UserProfileDto> result = userQueryService.findById(userId);

            assertThat(result).isPresent();
            assertThat(result.get().userId()).isEqualTo(userId);
            assertThat(result.get().interestTags()).containsExactlyInAnyOrder("Java", "Spring", "Database");
            assertThat(result.get().level()).isEqualTo(Level.JUNIOR);
        }

        @Test
        @DisplayName("존재하지 않는 사용자 ID로 조회하면 빈 Optional을 반환한다")
        void findById_withNonExistingUser_returnsEmpty() {
            Long userId = 999L;
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            Optional<UserProfileDto> result = userQueryService.findById(userId);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("태그가 DB에서 조회되지 않으면 빈 interestTags를 반환한다")
        void findById_withNoMatchingTags_returnsEmptyInterestTags() {
            Long userId = 1L;
            List<Long> tagIds = List.of(10L, 20L, 30L);

            User user = mockUser(userId, tagIds, Level.MIDDLE);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(tagRepository.findAllByIdIn(tagIds)).thenReturn(List.of());

            Optional<UserProfileDto> result = userQueryService.findById(userId);

            assertThat(result).isPresent();
            assertThat(result.get().interestTags()).isEmpty();
        }

        private User mockUser(Long id, List<Long> tagIds, Level level) {
            User user = org.mockito.Mockito.mock(User.class);
            when(user.getId()).thenReturn(id);
            when(user.getTagIds()).thenReturn(tagIds);
            when(user.getLevel()).thenReturn(level);
            return user;
        }
    }
}
