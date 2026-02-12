package com.lxpbe.course.application.service;

import com.lxpbe.course.application.command.CourseCreateCommand;
import com.lxpbe.course.application.command.CourseUpdateCommand;
import com.lxpbe.course.application.repository.CourseRepository;
import com.lxpbe.course.domain.Course;
import com.lxpbe.course.domain.exception.CourseErrorCode;
import com.lxpbe.course.domain.exception.CourseException;
import com.lxpbe.course.presentation.response.InstructorResponse;
import com.lxpbe.course.presentation.response.CourseDetailResponse;
import com.lxpbe.course.presentation.response.CourseListResponse;
import com.lxpbe.course.presentation.request.UpdateCourseRequest;
import com.lxpbe.tag.application.TagQueryService;
import com.lxpbe.tag.application.result.TagResult;
import com.lxpbe.user.domain.User;
import com.lxpbe.user.domain.enums.Role;
import com.lxpbe.user.infrastructure.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final TagQueryService tagQueryService;

    public Page<CourseListResponse> searchCourses(String keyword, Pageable pageable) {
        Page<Course> courses = courseRepository.searchByKeyword(keyword, pageable);

        return courses.map(course -> {
            InstructorResponse instructor = getInstructor(course.getInstructorId());
            List<TagResult> tags = tagQueryService.findByIds(course.getTags());
            return CourseListResponse.of(course, instructor, tags);
        });
    }

    public CourseDetailResponse getCourse(Long courseId) {
        Course course = getCourseOrThrow(courseId);

        List<TagResult> tags = tagQueryService.findByIds(course.getTags());

        return CourseDetailResponse.of(course, getInstructor(course.getInstructorId()), tags);
    }

    @Transactional
    public CourseDetailResponse createCourse(Long instructorId, CourseCreateCommand command) {
        User user = userRepository.findByIdAndRole(instructorId, Role.INSTRUCTOR)
                .orElseThrow(() -> new CourseException(CourseErrorCode.INVALID_INSTRUCTOR));

        Course course =  Course.create(instructorId, command);
        Course savedCourse = courseRepository.save(course);

        List<TagResult> tags = tagQueryService.findByIds(command.tags());

        return CourseDetailResponse.of(savedCourse, new InstructorResponse(instructorId, user.getName()), tags);
    }

    @Transactional
    public CourseDetailResponse updateCourse(Long instructorId, Long courseId, UpdateCourseRequest request) {
        Course course = courseRepository.findByInstructorId(courseId, instructorId)
                .orElseThrow(() -> new CourseException(CourseErrorCode.COURSE_UPDATE_DENIED));

        course.update(CourseUpdateCommand.of(courseId, request));
        List<TagResult> tags = tagQueryService.findByIds(course.getTags());

        return CourseDetailResponse.of(course, getInstructor(instructorId), tags);
    }

    @Transactional
    public void deleteCourse(Long instructorId, Long courseId) {
        Course course = courseRepository.findByInstructorId(courseId, instructorId)
                .orElseThrow(() -> new CourseException(CourseErrorCode.COURSE_DELETE_DENIED));
        courseRepository.delete(course);
    }

    public Course getCourseOrThrow(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseException(CourseErrorCode.COURSE_NOT_FOUND));
    }

    public InstructorResponse getInstructor(Long id) {
        return userRepository.findByIdAndRole(id, Role.INSTRUCTOR)
                .map(user -> new InstructorResponse(user.getId(), user.getName()))
                .orElse(InstructorResponse.unknown(id));

    }
}
