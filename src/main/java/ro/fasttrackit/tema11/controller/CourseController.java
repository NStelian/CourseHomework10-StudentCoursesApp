package ro.fasttrackit.tema11.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.tema11.controller.exceptions.EntityNotFoundException;
import ro.fasttrackit.tema11.model.entity.Course;
import ro.fasttrackit.tema11.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    List<Course> getAll() {
        return courseService.getAll();
    }

    @PostMapping
    Course addCourse(@RequestBody Course course) {
        return courseService.addCourse(course);
    }

    @GetMapping("{courseId}")
    Course getCourseById(@PathVariable String courseId) {
        return courseService.getCourseById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course with id " + courseId + " not Found"));
    }
}
