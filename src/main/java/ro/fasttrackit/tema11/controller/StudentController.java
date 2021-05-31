package ro.fasttrackit.tema11.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.tema11.controller.exceptions.EntityNotFoundException;
import ro.fasttrackit.tema11.model.StudentCourse;
import ro.fasttrackit.tema11.model.StudentFilters;
import ro.fasttrackit.tema11.model.entity.Student;
import ro.fasttrackit.tema11.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    List<Student> getAll(StudentFilters filters) {
        return studentService.getAllByFilters(filters);
    }

    @PostMapping
    Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @GetMapping("{studentId}")
    Student getStudentById(@PathVariable String studentId) {
        return studentService.getStudentById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student with id " + studentId + " not Found"));
    }

    @GetMapping("{studentId}/courses")
    List<StudentCourse> getStudentCourses(@PathVariable String studentId) {
        return studentService.getStudentCourses(studentId);
    }
}
