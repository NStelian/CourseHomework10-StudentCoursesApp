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
import ro.fasttrackit.tema11.model.StudentCourse;
import ro.fasttrackit.tema11.model.entity.Student;
import ro.fasttrackit.tema11.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ro.fasttrackit.tema11.controller.StudentControllerTest.TestBeans;

@WebMvcTest
@ContextConfiguration(classes = {StudentCourseApplication.class, TestBeans.class})
public class StudentControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private StudentService studentService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("GET /students")
    void getAllStudentsTest() throws Exception {
        doReturn(List.of(
                new Student("id1", "Steli", 23),
                new Student("id2", "Cipri", 23)
        )).when(studentService).getAllByFilters(any());

        mvc.perform(get("/students"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[1].name", is("Cipri")));

        verify(studentService, times(1)).getAllByFilters(any());
    }

    @Test
    @DisplayName("POST /students")
    void addStudentTest() throws Exception {
        Student studentToAdd = new Student("asd", "Steli", 23);
        doReturn(studentToAdd)
                .when(studentService).addStudent(studentToAdd);

        String requestJson = mapper.writeValueAsString(studentToAdd);

        mvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Steli")));

        verify(studentService, times(1)).addStudent(studentToAdd);
    }

    @Test
    @DisplayName("GET /students/studentId")
    void getStudentByIdTest() throws Exception {
        doReturn(
                Optional.of(new Student("asd", "Steli", 23))).when(studentService).getStudentById("id1");

        mvc.perform(get("/students/id1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Steli")));

        verify(studentService, times(1)).getStudentById("id1");
    }

    @Test
    @DisplayName("GET /students/studentId")
    void getStudentByIdAndThrowExceptionTest() throws Exception {
        doReturn(
                Optional.empty()).when(studentService).getStudentById("id1");

        mvc.perform(get("/students/id1"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /students/studentId/courses")
    void getStudentCoursesTest() throws Exception {

        doReturn(getStudentCourseList()).when(studentService).getStudentCourses("id1");
        mvc.perform(get("/students/id1/courses"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name", is("Steli")));

        verify(studentService, times(1)).getStudentCourses("id1");
    }

    public static List<StudentCourse> getStudentCourseList() {
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setName("Steli");
        studentCourse.setAge(23);
        studentCourse.setDiscipline("Math");
        studentCourse.setGrade(10);

        return List.of(studentCourse);
    }

    @Configuration
    static class TestBeans {
        @Bean
        StudentService studentService() {
            return mock(StudentService.class);
        }
    }
}
