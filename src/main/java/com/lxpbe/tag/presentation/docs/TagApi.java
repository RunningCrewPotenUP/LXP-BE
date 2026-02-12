package com.lxpbe.tag.presentation.docs;

import com.lxpbe.tag.application.result.TagResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Tag", description = "태그 API")
public interface TagApi {

    @Operation(summary = "전체 태그 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = TagApiResponseExamples.TAG_LIST_200)))
    })
    ResponseEntity<com.lxpbe.common.response.ApiResponse<List<TagResult>>> findAll();

    @Operation(summary = "태그 ID로 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = TagApiResponseExamples.TAG_200))),
            @ApiResponse(responseCode = "404", description = "태그 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = TagApiResponseExamples.TAG_NOT_FOUND_404)))
    })
    ResponseEntity<com.lxpbe.common.response.ApiResponse<TagResult>> findById(
            @PathVariable
            @Positive(message = "id 는 1 이상이어야합니다.")
            Long id
    );

    @Operation(summary = "태그 ID 목록으로 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = TagApiResponseExamples.TAG_LIST_200)))
    })
    ResponseEntity<com.lxpbe.common.response.ApiResponse<List<TagResult>>> findByIds(
            @RequestParam(value = "ids", required = false)
            @NotNull(message = "id 목록은 null 일 수 없습니다.")
            @Size(min = 1, message = "id 목록은 비어있을 수 없습니다.")
            List<@NotNull Long> ids
    );

    @Operation(summary = "태그 이름으로 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = TagApiResponseExamples.TAG_200)))
    })
    ResponseEntity<com.lxpbe.common.response.ApiResponse<TagResult>> findByName(
            @RequestParam
            @NotBlank(message = "name 은 blank 일 수 없습니다.")
            String name
    );

    @Operation(summary = "태그 검색")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = TagApiResponseExamples.TAG_LIST_200)))
    })
    ResponseEntity<com.lxpbe.common.response.ApiResponse<List<TagResult>>> search(
            @RequestParam
            @NotBlank(message = "검색 키워드는 null 이거나 비어있을 수 없습니다.")
            String q
    );
}
