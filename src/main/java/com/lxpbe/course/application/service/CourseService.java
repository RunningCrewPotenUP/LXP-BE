package com.lxpbe.course.application.service;

import com.lxpbe.common.exception.DomainException;
import com.lxpbe.course.domain.Course;
import com.lxpbe.course.domain.Lecture;
import com.lxpbe.course.domain.Section;
import com.lxpbe.course.domain.exception.CourseErrorCode;
import com.lxpbe.course.application.port.InstructorResult;
import com.lxpbe.course.application.port.TagResult;
import com.lxpbe.course.application.port.TagPort;
import com.lxpbe.course.application.port.UserPort;
import com.lxpbe.course.infrastructure.repository.CourseJpaRepository;
import com.lxpbe.course.presentation.response.CourseDetailResponse;
import com.lxpbe.course.presentation.response.CourseListResponse;
import com.lxpbe.course.presentation.request.CreateCourseRequest;
import com.lxpbe.course.presentation.request.UpdateCourseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseJpaRepository courseRepository;
    private final UserPort userPort;
    private final TagPort tagPort;

    public Page<CourseListResponse> searchCourses(String keyword, Pageable pageable) {
        Page<Course> courses = courseRepository.searchByKeyword(keyword, pageable);

        return courses.map(course -> {
            InstructorResult instructor = userPort.findInstructorById(course.getInstructorId())
                    .orElse(InstructorResult.unknown(course.getInstructorId()));
            List<TagResult> tags = tagPort.findTagsByIds(course.getTags());
            return CourseListResponse.of(course, instructor, tags);
        });
    }

    public CourseDetailResponse getCourse(Long courseId) {
        Course course = courseRepository.findByIdWithSectionsAndLectures(courseId)
                .orElseThrow(() -> new DomainException(CourseErrorCode.COURSE_NOT_FOUND));

        InstructorResult instructor = userPort.findInstructorById(course.getInstructorId())
                .orElse(InstructorResult.unknown(course.getInstructorId()));
        List<TagResult> tags = tagPort.findTagsByIds(course.getTags());

        return CourseDetailResponse.of(course, instructor, tags);
    }

    @Transactional
    public CourseDetailResponse createCourse(CreateCourseRequest request, Long instructorId) {
        Course course =  Course.create(instructorId, request.title(), request.description(), request.thumbnailUrl(), request.level(), request.tags());

        if (request.sections() != null) {
            AtomicInteger sectionOrder = new AtomicInteger(1);
            request.sections().forEach(sectionRequest -> {
                Section section = Section.builder()
                        .title(sectionRequest.title())
                        .order(sectionOrder.getAndIncrement())
                        .build();

                if (sectionRequest.lectures() != null) {
                    AtomicInteger lectureOrder = new AtomicInteger(1);
                    sectionRequest.lectures().forEach(lectureRequest -> {
                        Lecture lecture = Lecture.builder()
                                .title(lectureRequest.title())
                                .videoUrl(lectureRequest.videoUrl())
                                .order(lectureOrder.getAndIncrement())
                                .build();
                        section.addLecture(lecture);
                    });
                }
                course.addSection(section);
            });
        }

        Course savedCourse = courseRepository.save(course);

        InstructorResult instructor = userPort.findInstructorById(savedCourse.getInstructorId())
                .orElse(InstructorResult.unknown(savedCourse.getInstructorId()));
        List<TagResult> tags = tagPort.findTagsByIds(savedCourse.getTags());

        return CourseDetailResponse.of(savedCourse, instructor, tags);
    }

    @Transactional
    public CourseDetailResponse updateCourse(Long courseId, UpdateCourseRequest request) {
        Course course = courseRepository.findByIdWithSectionsAndLectures(courseId)
                .orElseThrow(() -> new DomainException(CourseErrorCode.COURSE_NOT_FOUND));

        course.updateBasicInfo(request.title(), request.description(), request.thumbnailUrl(), request.level());

        if (request.tags() != null) {
            course.updateTags(request.tags());
        }

        if (request.sections() != null) {
            course.clearSections();
            AtomicInteger sectionOrder = new AtomicInteger(1);
            request.sections().forEach(sectionReq -> {
                Section section = Section.builder()
                        .title(sectionReq.title())
                        .order(sectionOrder.getAndIncrement())
                        .build();

                if (sectionReq.lectures() != null) {
                    AtomicInteger lectureOrder = new AtomicInteger(1);
                    sectionReq.lectures().forEach(lectureReq -> {
                        Lecture lecture = Lecture.builder()
                                .title(lectureReq.title())
                                .videoUrl(lectureReq.videoUrl())
                                .order(lectureOrder.getAndIncrement())
                                .build();
                        section.addLecture(lecture);
                    });
                }
                course.addSection(section);
            });
        }

        InstructorResult instructor = userPort.findInstructorById(course.getInstructorId())
                .orElse(InstructorResult.unknown(course.getInstructorId()));
        List<TagResult> tags = tagPort.findTagsByIds(course.getTags());

        return CourseDetailResponse.of(course, instructor, tags);
    }

    @Transactional
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new DomainException(CourseErrorCode.COURSE_NOT_FOUND));
        courseRepository.delete(course);
    }
}
