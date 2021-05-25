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
import ro.fasttrackit.tema11.repository.CourseRepository;

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
@ContextConfiguration(initializers = CourseAppRealMongoTest.Initializer.class)
public class CourseAppRealMongoTest {
    @Container
    private static MongoDBContainer mongoDb = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    static {
        mongoDb.start();
    }

    @Autowired
    private MockMvc mvc;
    @Autowired
    private CourseRepository courseRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Test
    @DisplayName("GET /courses")
    void getAllCoursesTest() {
        courseRepository.saveAll(List.of(
                new Course("id1", "Math", "nice"),
                new Course("id2", "English", "very nice")
        ));

        mvc.perform(get("/courses"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[1].discipline", is("English")));
    }

    @SneakyThrows
    @Test
    @DisplayName("POST /courses")
    void addCourseTest() {
        Course courseToAdd = new Course("id1", "Math", "nice");
        String requestJson = mapper.writeValueAsString(courseToAdd);

        mvc.perform(post("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discipline", is("Math")));
    }

    @SneakyThrows
    @Test
    @DisplayName("GET /courses/courseId")
    void getCourseByIdTest() {
        courseRepository.saveAll(List.of(
                new Course("id1", "Math", "nice"),
                new Course("id2", "English", "very nice")
        ));

        mvc.perform(get("/courses/id1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discipline", is("Math")));
    }

    @AfterEach
    void cleanup() {
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
