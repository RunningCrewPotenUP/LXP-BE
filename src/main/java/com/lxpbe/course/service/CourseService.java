package com.lxpbe.course.service;

import com.lxpbe.common.exception.DomainException;
import com.lxpbe.course.domain.Course;
import com.lxpbe.course.domain.Lecture;
import com.lxpbe.course.domain.Section;
import com.lxpbe.course.domain.enums.Level;
import com.lxpbe.course.dto.*;
import com.lxpbe.course.exception.CourseErrorCode;
import com.lxpbe.course.port.InstructorInfo;
import com.lxpbe.course.port.TagInfo;
import com.lxpbe.course.port.TagPort;
import com.lxpbe.course.port.UserPort;
import com.lxpbe.course.repository.CourseRepository;
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

    private final CourseRepository courseRepository;
    private final UserPort userPort;
    private final TagPort tagPort;

    public Page<CourseListResponse> searchCourses(String keyword, Pageable pageable) {
        Page<Course> courses = courseRepository.searchByKeyword(keyword, pageable);

        return courses.map(course -> {
            InstructorInfo instructor = userPort.findInstructorById(course.getInstructorId())
                    .orElse(InstructorInfo.unknown(course.getInstructorId()));
            List<TagInfo> tags = tagPort.findTagsByIds(course.getTags());
            return CourseListResponse.of(course, instructor, tags);
        });
    }

    public CourseDetailResponse getCourse(Long courseId) {
        Course course = courseRepository.findByIdWithSectionsAndLectures(courseId)
                .orElseThrow(() -> new DomainException(CourseErrorCode.COURSE_NOT_FOUND));

        InstructorInfo instructor = userPort.findInstructorById(course.getInstructorId())
                .orElse(InstructorInfo.unknown(course.getInstructorId()));
        List<TagInfo> tags = tagPort.findTagsByIds(course.getTags());

        return CourseDetailResponse.of(course, instructor, tags);
    }

    @Transactional
    public CourseDetailResponse createCourse(CreateCourseRequest request, Long instructorId) {
        Level level = Level.fromString(request.level())
                .orElseThrow(() -> new DomainException(CourseErrorCode.INVALID_LEVEL));

        Course course = Course.builder()
                .instructorId(instructorId)
                .title(request.title())
                .description(request.description())
                .thumbnailUrl(request.thumbnailUrl())
                .difficulty(level)
                .tags(request.tags())
                .build();

        if (request.sections() != null) {
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

        Course savedCourse = courseRepository.save(course);

        InstructorInfo instructor = userPort.findInstructorById(savedCourse.getInstructorId())
                .orElse(InstructorInfo.unknown(savedCourse.getInstructorId()));
        List<TagInfo> tags = tagPort.findTagsByIds(savedCourse.getTags());

        return CourseDetailResponse.of(savedCourse, instructor, tags);
    }

    @Transactional
    public CourseDetailResponse updateCourse(Long courseId, UpdateCourseRequest request) {
        Course course = courseRepository.findByIdWithSectionsAndLectures(courseId)
                .orElseThrow(() -> new DomainException(CourseErrorCode.COURSE_NOT_FOUND));

        Level level = null;
        if (request.level() != null) {
            level = Level.fromString(request.level())
                    .orElseThrow(() -> new DomainException(CourseErrorCode.INVALID_LEVEL));
        }

        course.updateBasicInfo(request.title(), request.description(), request.thumbnailUrl(), level);

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

        InstructorInfo instructor = userPort.findInstructorById(course.getInstructorId())
                .orElse(InstructorInfo.unknown(course.getInstructorId()));
        List<TagInfo> tags = tagPort.findTagsByIds(course.getTags());

        return CourseDetailResponse.of(course, instructor, tags);
    }

    @Transactional
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new DomainException(CourseErrorCode.COURSE_NOT_FOUND));
        courseRepository.delete(course);
    }
}
