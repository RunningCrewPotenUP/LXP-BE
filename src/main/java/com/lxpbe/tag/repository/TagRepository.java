package com.lxpbe.tag.repository;

import com.lxpbe.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByIdIn(Collection<Long> ids);

    Optional<Tag> findByName(String name);

    List<Tag> findAllByNameContaining(String name);
}
