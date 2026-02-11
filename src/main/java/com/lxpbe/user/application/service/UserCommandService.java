package com.lxpbe.user.application.service;

import com.lxpbe.tag.domain.Tag;
import com.lxpbe.tag.repository.TagRepository;
import com.lxpbe.user.application.result.UserInfoResult;
import com.lxpbe.user.domain.User;
import com.lxpbe.user.domain.enums.Level;
import com.lxpbe.user.domain.exception.UserErrorCode;
import com.lxpbe.user.domain.exception.UserException;
import com.lxpbe.user.infrastructure.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public UserInfoResult updateMyInfo(Long userId, String name, List<Long> tagIds, Level level) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        user.updateInfo(name, level, tagIds);
        List<Tag> tags = tagRepository.findAllByIdIn(user.getTagIds());
        return UserInfoResult.from(user, tags);
    }
}
