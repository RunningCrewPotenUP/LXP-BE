package com.lxpbe.user.infrastructure.repository;

import com.lxpbe.user.domain.User;
import java.util.Optional;

import com.lxpbe.user.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByIdAndRole(Long id, Role role);
}
