package ro.fasttrackit.tema11.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ro.fasttrackit.tema11.StudentCourseApplication;
import ro.fasttrackit.tema11.model.entity.CourseStudent;
import ro.fasttrackit.tema11.model.entity.Student;
import ro.fasttrackit.tema11.service.CourseStudentService;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ro.fasttrackit.tema11.controller.CourseStudentControllerTest.TestBeans;

@WebMvcTest
@ContextConfiguration(classes = {StudentCourseApplication.class, TestBeans.class})
class CourseStudentControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CourseStudentService courseStudentService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("GET /course/courseId/students")
    void getAllStudentsForCourseIdTest() throws Exception {
        doReturn(List.of(
                new Student("id1", "Steli", 23),
                new Student("id2", "Cipri", 23)
        )).when(courseStudentService).getAllStudentsForCourseId("courseId1");

        mvc.perform(get("/course/courseId1/students"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[1].name", is("Cipri")));

        verify(courseStudentService, times(1)).getAllStudentsForCourseId("courseId1");
    }

    @Test
    @DisplayName("POST /course/courseId/students")
    void registerStudentToCourseTest() throws Exception {
        CourseStudent courseStudent = new CourseStudent("courseStudentId1", "courseId1", "studentId1", 10);
        doReturn(
                courseStudent
        ).when(courseStudentService).registerStudentToCourse("courseId1", courseStudent);

        String requestJson = mapper.writeValueAsString(courseStudent);

        mvc.perform(post("/course/courseId1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade", is(10)));

        verify(courseStudentService, times(1)).registerStudentToCourse("courseId1", courseStudent);
    }

    @Configuration
    static class TestBeans {
        @Bean
        CourseStudentService courseStudentService() {
            return mock(CourseStudentService.class);
        }
    }
}