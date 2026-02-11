package com.lxpbe.tag.presentation;

import com.lxpbe.tag.application.TagQueryService;
import com.lxpbe.tag.presentation.response.TagResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api-v1/tags")
public class TagController {

    private final TagQueryService tagQueryService;

    public TagController(TagQueryService tagQueryService) {
        this.tagQueryService = tagQueryService;
    }

    @GetMapping
    ResponseEntity<List<TagResponse>> findAll() {
        List<TagResponse> body = tagQueryService.findAll();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    ResponseEntity<TagResponse> findById(
            @PathVariable
            @Positive(message = "id 는 1 이상이어야합니다.")
            Long id
    ) {
        TagResponse body = tagQueryService.findById(id);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/findByIds")
    ResponseEntity<List<TagResponse>> findByIds(
            @RequestParam(value = "ids", required = false)
            @NotNull(message = "id 목록은 null 일 수 없습니다.")
            @Size(min = 1, message = "id 목록은 비어있을 수 없습니다.")
            List<@NotNull Long> ids
    ) {
        List<TagResponse> body = tagQueryService.findByIds(ids);
        return ResponseEntity.ok(body);
    }

    @GetMapping("findByName")
    ResponseEntity<TagResponse> findByName(
            @RequestParam
            @NotBlank(message = "id 는 null 이거나 비어있을 수 없습니다.")
            String name
    ) {
        TagResponse body = tagQueryService.findByName(name);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/search")
    ResponseEntity<List<TagResponse>> search(
            @RequestParam
            @NotBlank(message = "검색 키워드는 null 이거나 비어있을 수 없습니다.")
            String q
    ) {
        List<TagResponse> body = tagQueryService.searchIdsByNameContaining(q);
        return ResponseEntity.ok(body);
    }
}
