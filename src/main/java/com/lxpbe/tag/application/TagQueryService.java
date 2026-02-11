package com.lxpbe.tag.application;

import com.lxpbe.tag.domain.Tag;
import com.lxpbe.tag.domain.exception.TagErrorCode;
import com.lxpbe.tag.domain.exception.TagException;
import com.lxpbe.tag.presentation.response.TagResponse;
import com.lxpbe.tag.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagQueryService {

    TagRepository tagRepository;

    public TagQueryService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagResponse> findAll() {
        return tagRepository.findAll().stream()
                .map(TagResponse::of)
                .toList();
    }

    public TagResponse findById(Long id) {
        Tag foundTag = tagRepository.findById(id)
                .orElseThrow(() -> new TagException(TagErrorCode.TAG_NOT_FOUND));
        return TagResponse.of(foundTag);
    }

    public List<TagResponse> findByIds(List<Long> ids) {
        return tagRepository.findAllByIdIn(ids).stream()
                .map(TagResponse::of)
                .toList();
    }

    public TagResponse findByName(String name) {
        Tag foundTag = tagRepository.findByName(name)
                .orElseThrow(() -> new TagException(TagErrorCode.TAG_NOT_FOUND));
        return TagResponse.of(foundTag);
    }

    public List<TagResponse> searchIdsByNameContaining(String q) {
        return tagRepository.findAllByNameContaining(q).stream()
                .map(TagResponse::of)
                .toList();
    }
}
