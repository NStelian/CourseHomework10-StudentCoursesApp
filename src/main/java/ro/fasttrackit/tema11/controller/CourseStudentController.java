//package ro.fasttrackit.tema11.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import ro.fasttrackit.tema11.model.entity.CourseStudent;
//import ro.fasttrackit.tema11.model.entity.Student;
//import ro.fasttrackit.tema11.service.CourseStudentService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("course")
//@RequiredArgsConstructor
//public class CourseStudentController {
//    private final CourseStudentService courseStudentService;
//
//    @GetMapping("{courseId}/students")
//    List<Student> getAllStudentsForCourseId(@PathVariable String courseId) {
//        return courseStudentService.getAllStudentsForCourseId(courseId);
//    }
//
//    @PostMapping("{courseId}/students")
//    CourseStudent registerStudentToCourse(@PathVariable String courseId, @RequestBody CourseStudent courseStudent) {
//        return courseStudentService.registerStudentToCourse(courseId, courseStudent);
//    }
//}
