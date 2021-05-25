package ro.fasttrackit.tema11.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.fasttrackit.tema11.controller.exceptions.ValidationException;
import ro.fasttrackit.tema11.model.entity.CourseStudent;
import ro.fasttrackit.tema11.repository.CourseRepository;
import ro.fasttrackit.tema11.repository.StudentRepository;

import java.util.Optional;

import static java.util.Optional.empty;

@Component
@RequiredArgsConstructor
public class CourseStudentValidator {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public void validateCourseThrow(CourseStudent courseStudent) {
        existsStudent(courseStudent.getStudentId())
                .ifPresent(ex -> {
                    throw ex;
                });

        existsCourse(courseStudent.getCourseId())
                .ifPresent(ex -> {
                    throw ex;
                });
    }

    private Optional<ValidationException> existsStudent(String studentId) {
        return studentRepository.existsById(studentId)
                ? empty()
                : Optional.of(new ValidationException("Student with id " + studentId + " doesn't exist"));
    }

    private Optional<ValidationException> existsCourse(String courseId) {
        return courseRepository.existsById(courseId)
                ? empty()
                : Optional.of(new ValidationException("Course with id " + courseId + " doesn't exist"));
    }
}
