package ro.fasttrackit.tema11.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ro.fasttrackit.tema11.model.entity.Course;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class CourseRepositoryTest {
    @Autowired
    private CourseRepository courseRepository;

    @Test
    @DisplayName("WHEN findByCourseId is called THEN the Math Course is returned")
    void findByCourseIdTest() {
        courseRepository.saveAll(List.of(
                new Course("id1", "Math", "nice"),
                new Course("id2", "English", "very nice")
        ));

        Course result = courseRepository.findByCourseId("id1");
        assertThat(result.getDiscipline())
                .isEqualTo("Math");
    }

    @Test
    @DisplayName("WHEN findByCourseId is called THEN the result is null")
    void noCoursesTest() {
        Course result = courseRepository.findByCourseId("id1");
        assertThat(result).isEqualTo(null);
    }

    @Test
    @DisplayName("WHEN findByCourseId for non-existing THEN null is returned")
    void findByMissingCourseIdTest() {
        courseRepository.saveAll(List.of(
                new Course("id1", "Math", "nice"),
                new Course("id2", "English", "very nice")
        ));

        Course result = courseRepository.findByCourseId("id3");
        assertThat(result).isEqualTo(null);
    }

    @AfterEach
    void cleanup() {
        courseRepository.deleteAll();
    }
}