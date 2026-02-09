package com.lxpbe.course.application.service;

import com.lxpbe.common.exception.DomainException;
import com.lxpbe.course.application.command.CourseCreateCommand;
import com.lxpbe.course.application.command.CourseUpdateCommand;
import com.lxpbe.course.application.repository.CourseRepository;
import com.lxpbe.course.domain.Course;
import com.lxpbe.course.domain.exception.CourseErrorCode;
import com.lxpbe.course.application.port.InstructorResult;
import com.lxpbe.course.application.port.TagResult;
import com.lxpbe.course.application.port.TagPort;
import com.lxpbe.course.application.port.UserPort;
import com.lxpbe.course.presentation.response.CourseDetailResponse;
import com.lxpbe.course.presentation.response.CourseListResponse;
import com.lxpbe.course.presentation.request.UpdateCourseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            InstructorResult instructor = userPort.findInstructorById(course.getInstructorId())
                    .orElse(InstructorResult.unknown(course.getInstructorId()));
            List<TagResult> tags = tagPort.findTagsByIds(course.getTags());
            return CourseListResponse.of(course, instructor, tags);
        });
    }

    public CourseDetailResponse getCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new DomainException(CourseErrorCode.COURSE_NOT_FOUND));

        InstructorResult instructor = userPort.findInstructorById(course.getInstructorId())
                .orElse(InstructorResult.unknown(course.getInstructorId()));
        List<TagResult> tags = tagPort.findTagsByIds(course.getTags());

        return CourseDetailResponse.of(course, instructor, tags);
    }

    @Transactional
    public CourseDetailResponse createCourse(CourseCreateCommand command) {
        InstructorResult instructor = userPort.findInstructorById(command.instructorId())
                .orElse(InstructorResult.unknown(command.instructorId()));
        List<TagResult> tags = tagPort.findTagsByIds(command.tags());

        Course course =  Course.create(command);
        Course savedCourse = courseRepository.save(course);

        return CourseDetailResponse.of(savedCourse, instructor, tags);
    }

    @Transactional
    public CourseDetailResponse updateCourse(Long courseId, UpdateCourseRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new DomainException(CourseErrorCode.COURSE_NOT_FOUND));

        course.update(CourseUpdateCommand.of(courseId, request));

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
