package com.lxpbe.enrollment.application;

import com.lxpbe.enrollment.application.result.EnrollmentDetails;
import com.lxpbe.enrollment.application.result.EnrollmentSummary;
import com.lxpbe.enrollment.domain.exception.EnrollmentErrorCode;
import com.lxpbe.enrollment.domain.exception.EnrollmentException;
import com.lxpbe.enrollment.repository.projection.EnrollmentDetailsProjectionRow;
import com.lxpbe.enrollment.repository.EnrollmentRepository;
import com.lxpbe.enrollment.repository.projection.EnrollmentSummaryProjectionRow;
import com.lxpbe.tag.application.result.TagResult;
import com.lxpbe.tag.repository.projection.CourseTagProjectionRow;
import com.lxpbe.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EnrollmentQueryService {

    private final TagRepository tagRepository;
    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentDetails queryDetails(Long requesterId, Long enrollmentId) {
        EnrollmentDetailsProjectionRow foundDetails = enrollmentRepository.findDetailsById(enrollmentId)
                .orElseThrow(() -> new EnrollmentException(EnrollmentErrorCode.ENROLLMENT_NOT_FOUND));

        if (!foundDetails.getUserId().equals(requesterId)
                && !foundDetails.getInstructorId().equals(requesterId)
        ) {
            throw new EnrollmentException(EnrollmentErrorCode.FORBIDDEN_ENROLLMENT_QUERY);
        }

        List<TagResult> tags = tagRepository.findAllByCourseId(foundDetails.getCourseId()).stream()
                .map(TagResult::of)
                .toList();

        return EnrollmentDetails.of(foundDetails, tags);
    }

    public Page<EnrollmentSummary> myEnrollments(Long requesterId, Pageable pageable) {
        Page<EnrollmentSummaryProjectionRow> summaryPage =
                enrollmentRepository.findSummariesByUserId(requesterId, pageable);

        if (summaryPage.isEmpty()) {
            return summaryPage.map(row -> EnrollmentSummary.of(row, List.of()));
        }

        List<Long> courseIds = summaryPage.getContent().stream()
                .map(EnrollmentSummaryProjectionRow::getCourseId)
                .distinct()
                .toList();

        List<CourseTagProjectionRow> courseTagProjectionRows = tagRepository.findCourseTagInfoByCourseIds(courseIds);

        Map<Long, List<TagResult>> courseIdToTagInfos = new HashMap<>();
        for (CourseTagProjectionRow row : courseTagProjectionRows) {
            Long courseId = row.getCourseId();
            if (!courseIdToTagInfos.containsKey(courseId)) {
                courseIdToTagInfos.put(courseId, new ArrayList<>());
            }
            List<TagResult> tagResults = courseIdToTagInfos.get(courseId);
            tagResults.add(TagResult.builder()
                    .tagId(row.getTagId())
                    .name(row.getTagName())
                    .category(row.getTagCategory())
                    .subCategory(row.getTagSubCategory())
                    .build()
            );
        }

        return summaryPage.map(row -> EnrollmentSummary.of(
                row,
                courseIdToTagInfos.getOrDefault(row.getCourseId(), List.of())
        ));
    }
}
