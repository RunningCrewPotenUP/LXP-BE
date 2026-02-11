package com.lxpbe.user.application.service;

import com.lxpbe.tag.domain.Tag;
import com.lxpbe.tag.repository.TagRepository;
import com.lxpbe.user.application.result.UserInfoResult;
import com.lxpbe.user.domain.User;
import com.lxpbe.user.domain.exception.UserErrorCode;
import com.lxpbe.user.domain.exception.UserException;
import com.lxpbe.user.infrastructure.facade.dto.UserProfileDto;
import com.lxpbe.user.infrastructure.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public Optional<UserProfileDto> findById(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    List<String> tagNames = getTagNames(user);
                    return new UserProfileDto(
                            user.getId(),
                            tagNames,
                            user.getLevel());
                });
    }

    private List<String> getTagNames(User user) {
        return tagRepository.findAllByIdIn(user.getTagIds())
                .stream()
                .map(Tag::name)
                .toList();
    }

    public UserInfoResult getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        List<Tag> tags = tagRepository.findAllByIdIn(user.getTagIds());
        return UserInfoResult.from(user, tags);
    }
}
