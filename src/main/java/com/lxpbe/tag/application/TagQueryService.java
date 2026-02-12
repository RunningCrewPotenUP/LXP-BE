package com.lxpbe.tag.application;

import com.lxpbe.tag.domain.Tag;
import com.lxpbe.tag.domain.exception.TagErrorCode;
import com.lxpbe.tag.domain.exception.TagException;
import com.lxpbe.tag.application.result.TagResult;
import com.lxpbe.tag.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagQueryService {


    public TagQueryService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagResult> findAll() {
        return tagRepository.findAll().stream()
                .map(TagResult::of)
                .toList();
    }

    public TagResult findById(Long id) {
        Tag foundTag = tagRepository.findById(id)
                .orElseThrow(() -> new TagException(TagErrorCode.TAG_NOT_FOUND));
        return TagResult.of(foundTag);
    }

    public List<TagResult> findByIds(List<Long> ids) {
        return tagRepository.findAllByIdIn(ids).stream()
                .map(TagResult::of)
                .toList();
    }

    public TagResult findByName(String name) {
        Tag foundTag = tagRepository.findByName(name)
                .orElseThrow(() -> new TagException(TagErrorCode.TAG_NOT_FOUND));
        return TagResult.of(foundTag);
    }

    public List<TagResult> searchIdsByNameContaining(String q) {
        return tagRepository.findAllByNameContaining(q).stream()
                .map(TagResult::of)
                .toList();
    }
}
