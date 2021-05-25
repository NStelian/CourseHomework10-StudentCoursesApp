package ro.fasttrackit.tema11.realmongotests;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ro.fasttrackit.tema11.model.entity.Course;
import ro.fasttrackit.tema11.model.entity.CourseStudent;
import ro.fasttrackit.tema11.model.entity.Student;
import ro.fasttrackit.tema11.repository.CourseRepository;
import ro.fasttrackit.tema11.repository.CourseStudentRepository;
import ro.fasttrackit.tema11.repository.StudentRepository;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(initializers = StudentAppRealMongoTest.Initializer.class)
public class StudentAppRealMongoTest {
    @Container
    private static MongoDBContainer mongoDb = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    static {
        mongoDb.start();
    }

    @Autowired
    private MockMvc mvc;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseStudentRepository courseStudentRepository;
    @Autowired
    private CourseRepository courseRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Test
    @DisplayName("GET /students")
    void getAllStudentsTest() {
        studentRepository.saveAll(List.of(
                new Student("studentId1", "Steli", 23),
                new Student("studentId2", "Cipri", 23)
        ));

        mvc.perform(get("/students"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[1].name", is("Cipri")));
    }

    @SneakyThrows
    @Test
    @DisplayName("POST /students")
    void addStudentTest() {
        Student studentToAdd = new Student("studentId1", "Steli", 23);
        String requestJson = mapper.writeValueAsString(studentToAdd);

        mvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Steli")));
    }

    @SneakyThrows
    @Test
    @DisplayName("GET /students/studendId")
    void getStudentByIdTest() {
        studentRepository.saveAll(List.of(
                new Student("studentId1", "Steli", 23),
                new Student("studentId2", "Cipri", 23)
        ));

        mvc.perform(get("/students/studentId1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Steli")));
    }

    @SneakyThrows
    @Test
    @DisplayName("GET /students/studentId/courses")
    void getStudentCoursesTest() {
        courseRepository.saveAll(List.of(
                new Course("courseId1", "Math", "nice"),
                new Course("courseId2", "English", "very nice")
        ));
        courseStudentRepository.saveAll(List.of(
                new CourseStudent("courseStudentId1", "courseId1",  "studentId1", 10),
                new CourseStudent("courseStudentId2", "courseId2",  "studentId1", 10)
        ));
        studentRepository.saveAll(List.of(
                new Student("studentId1", "Steli", 23),
                new Student("studentId2", "Cipri", 23)
        ));

        mvc.perform(get("/students/studentId1/courses"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].discipline", is("Math")))
                .andExpect(jsonPath("$[1].discipline", is("English")));
    }

    @AfterEach
    void cleanup() {
        studentRepository.deleteAll();
        courseStudentRepository.deleteAll();
        courseRepository.deleteAll();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    String.format("spring.data.mongodb.uri: %s", mongoDb.getReplicaSetUrl())
            ).applyTo(configurableApplicationContext);
        }

    }
}
