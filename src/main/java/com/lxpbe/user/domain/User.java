package com.lxpbe.user.domain;

import com.lxpbe.common.domain.BaseEntity;
import com.lxpbe.common.exception.DomainException;
import com.lxpbe.user.domain.exception.UserErrorCode;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import com.lxpbe.user.domain.enums.Level;
import com.lxpbe.user.domain.enums.Role;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final int MIN_TAG_COUNT = 3;
    private static final int MAX_TAG_COUNT = 5;

    @Id @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(nullable = false, unique = true)
    private String email;

    @Getter
    @Column(nullable = false)
    private String password;

    @Getter
    @Column(nullable = false)
    private String name;

    @Getter
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles = EnumSet.noneOf(Role.class);

    @Getter
    @Enumerated(EnumType.STRING)
    private Level level;

    @Getter
    @Column(name = "tag_id")
    @ElementCollection
    @CollectionTable(name = "user_tags", joinColumns = @JoinColumn(name = "user_id"))
    private List<Long> tagIds = new ArrayList<>();

    @Builder
    private User(String email, String password, String name,
                 Set<Role> roles, Level level, List<Long> tagIds) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.roles = roles != null ? roles : EnumSet.noneOf(Role.class);
        this.level = level;
        this.tagIds = tagIds != null ? tagIds : new ArrayList<>();
    }

    public static User create(String email, String encodedPassword, String name,
                               Role role, Level level, List<Long> tagIds) {
        validateEmailRegex(email);
        validateCountTagIds(tagIds);
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .name(name)
                .roles(EnumSet.of(role))
                .level(level)
                .tagIds(tagIds)
                .build();
    }

    private static void validateEmailRegex(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new DomainException(UserErrorCode.INVALID_EMAIL_FORMAT);
        }
    }

    private static void validateCountTagIds(List<Long> tagIds) {
        if (tagIds.size() < MIN_TAG_COUNT || tagIds.size() > MAX_TAG_COUNT) {
            throw new DomainException(UserErrorCode.INVALID_TAG_COUNT);
        }
    }
}
