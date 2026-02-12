package com.lxpbe.tag.presentation;

import com.lxpbe.common.response.ApiResponse;
import com.lxpbe.tag.application.TagQueryService;
import com.lxpbe.tag.application.result.TagResult;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lxpbe.tag.presentation.docs.TagApi;

import java.util.List;

@Validated
@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController implements TagApi {

    private final TagQueryService tagQueryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TagResult>>> findAll() {
        List<TagResult> body = tagQueryService.findAll();
        return ResponseEntity.ok(ApiResponse.success(body));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TagResult>> findById(
            @PathVariable
            @Positive(message = "id 는 1 이상이어야합니다.")
            Long id
    ) {
        TagResult body = tagQueryService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(body));
    }

    @GetMapping("/findByIds")
    public ResponseEntity<ApiResponse<List<TagResult>>> findByIds(
            @RequestParam(value = "ids", required = false)
            @NotNull(message = "id 목록은 null 일 수 없습니다.")
            @Size(min = 1, message = "id 목록은 비어있을 수 없습니다.")
            List<@NotNull Long> ids
    ) {
        List<TagResult> body = tagQueryService.findByIds(ids);
        return ResponseEntity.ok(ApiResponse.success(body));
    }

    @GetMapping("findByName")
    public ResponseEntity<ApiResponse<TagResult>> findByName(
            @RequestParam
            @NotBlank(message = "name 은 blank 일 수 없습니다.")
            String name
    ) {
        TagResult body = tagQueryService.findByName(name);
        return ResponseEntity.ok(ApiResponse.success(body));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TagResult>>> search(
            @RequestParam
            @NotBlank(message = "검색 키워드는 null 이거나 비어있을 수 없습니다.")
            String q
    ) {
        List<TagResult> body = tagQueryService.searchIdsByNameContaining(q);
        return ResponseEntity.ok(ApiResponse.success(body));
    }
}
