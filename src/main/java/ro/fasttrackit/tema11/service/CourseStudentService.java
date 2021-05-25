package ro.fasttrackit.tema11.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.fasttrackit.tema11.model.entity.CourseStudent;
import ro.fasttrackit.tema11.model.entity.Student;
import ro.fasttrackit.tema11.repository.CourseStudentRepository;
import ro.fasttrackit.tema11.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseStudentService {
    private final CourseStudentRepository courseStudentRepository;
    private final StudentRepository studentRepository;
    private final CourseStudentValidator validator;

    public List<Student> getAllStudentsForCourseId(String courseId) {
        List<CourseStudent> courseStudents = courseStudentRepository.findAllByCourseId(courseId);

        return courseStudents
                .stream()
                .map(courseStudent -> studentRepository.findByStudentId(courseStudent.getStudentId()))
                .collect(Collectors.toList());
    }

    public CourseStudent registerStudentToCourse(String courseId, CourseStudent courseStudent) {
        courseStudent.setCourseId(courseId);
        validator.validateCourseThrow(courseStudent);
        return courseStudentRepository.save(courseStudent);
    }
}
