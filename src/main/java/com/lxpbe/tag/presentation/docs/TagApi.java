package com.lxpbe.tag.presentation.docs;

import com.lxpbe.tag.application.result.TagResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Tag", description = "태그 API")
public interface TagApi {

    @Operation(summary = "전체 태그 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = TagApiResponseExamples.TAG_LIST_200)))
    })
    ResponseEntity<List<TagResult>> findAll();

    @Operation(summary = "태그 ID로 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = TagApiResponseExamples.TAG_200))),
            @ApiResponse(responseCode = "404", description = "태그 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = TagApiResponseExamples.TAG_NOT_FOUND_404)))
    })
    ResponseEntity<TagResult> findById(Long id);

    @Operation(summary = "태그 ID 목록으로 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = TagApiResponseExamples.TAG_LIST_200)))
    })
    ResponseEntity<List<TagResult>> findByIds(List<Long> ids);

    @Operation(summary = "태그 이름으로 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = TagApiResponseExamples.TAG_200)))
    })
    ResponseEntity<TagResult> findByName(String name);

    @Operation(summary = "태그 검색")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = TagApiResponseExamples.TAG_LIST_200)))
    })
    ResponseEntity<List<TagResult>> search(String q);
}
