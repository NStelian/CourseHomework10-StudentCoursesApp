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
import ro.fasttrackit.tema11.model.entity.Course;
import ro.fasttrackit.tema11.service.CourseService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ro.fasttrackit.tema11.controller.CourseControllerTest.TestBeans;

@WebMvcTest
@ContextConfiguration(classes = {StudentCourseApplication.class, TestBeans.class})
public class CourseControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CourseService courseService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("GET /courses")
    void getAllCoursesTest() throws Exception {
        doReturn(List.of(
                new Course("id1", "Math", "nice"),
                new Course("id2", "English", "very nice")
        )).when(courseService).getAll();

        mvc.perform(get("/courses"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[1].discipline", is("English")));

        verify(courseService, times(1)).getAll();
    }

    @Test
    @DisplayName("POST /courses")
    void addCourseTest() throws Exception {
        Course courseToAdd = new Course("id1", "Math", "nice");
        doReturn(courseToAdd)
                .when(courseService).addCourse(courseToAdd);

        String requestJson = mapper.writeValueAsString(courseToAdd);

        mvc.perform(post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discipline", is("Math")));

        verify(courseService, times(1)).addCourse(courseToAdd);
    }

    @Test
    @DisplayName("GET /courses/courseId")
    void getCourseByIdTest() throws Exception {
        doReturn(
                Optional.empty()).when(courseService).getCourseById("id1");

        mvc.perform(get("/courses/id1"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /courses/courseId")
    void getCourseByIdAndThrowExceptionTest() throws Exception {
        doReturn(
                Optional.of(new Course("id1", "Math", "nice"))).when(courseService).getCourseById("id1");

        mvc.perform(get("/courses/id1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discipline", is("Math")));
    }

    @Configuration
    static class TestBeans {
        @Bean
        CourseService courseService() {
            return mock(CourseService.class);
        }
    }
}
