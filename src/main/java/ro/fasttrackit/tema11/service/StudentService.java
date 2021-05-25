package ro.fasttrackit.tema11.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.fasttrackit.tema11.controller.exceptions.EntityNotFoundException;
import ro.fasttrackit.tema11.model.StudentCourse;
import ro.fasttrackit.tema11.model.StudentFilters;
import ro.fasttrackit.tema11.model.entity.CourseStudent;
import ro.fasttrackit.tema11.model.entity.Student;
import ro.fasttrackit.tema11.repository.CourseRepository;
import ro.fasttrackit.tema11.repository.CourseStudentRepository;
import ro.fasttrackit.tema11.repository.StudentDao;
import ro.fasttrackit.tema11.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentDao studentDao;
    private final CourseStudentRepository courseStudentRepository;
    private final CourseRepository courseRepository;

    public List<Student> getAllByFilters(StudentFilters filters) {
        return studentDao.getAll(filters);
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> getStudentById(String studentId) {
        return studentRepository.findById(studentId);
    }

    public List<StudentCourse> getStudentCourses(String studentId) {
        Student student = getStudentById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student with id " + studentId + " not Found"));

        List<CourseStudent> studentCourses = courseStudentRepository.findAll().stream()
                .filter(courseStudent -> courseStudent.getStudentId().equals(studentId))
                .collect(Collectors.toList());

        return studentCourses.stream()
                .map(courseStudent  -> createStudentCourse(courseStudent, student))
                .collect(Collectors.toList());
    }

    private StudentCourse createStudentCourse(CourseStudent courseStudent, Student student) {
        StudentCourse newStudentCourse = new StudentCourse();
        newStudentCourse.setName(student.getName());
        newStudentCourse.setAge(student.getAge());
        newStudentCourse.setDiscipline(getCourseDiscipline(courseStudent.getCourseId()));
        newStudentCourse.setGrade(courseStudent.getGrade());

        return newStudentCourse;
    }

    private String getCourseDiscipline(String courseId) {
        return courseRepository.findByCourseId(courseId).getDiscipline();
    }
}
