package ro.fasttrackit.tema11.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ro.fasttrackit.tema11.model.entity.CourseStudent;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class CourseStudentRepositoryTest {
    @Autowired
    private CourseStudentRepository courseStudentRepository;

    @Test
    @DisplayName("WHEN findAllByCourseId is called THEN the students are returned")
    void findAllByCourseIdTest() {
        courseStudentRepository.saveAll(List.of(
                new CourseStudent("courseStudendId1", "courseId1", "studentId1", 10),
                new CourseStudent("courseStudendId2", "courseId1", "studentId2", 10)
        ));

        List<CourseStudent> result = courseStudentRepository.findAllByCourseId("courseId1");
        assertThat(result.get(0).getStudentId())
                .isEqualTo("studentId1");
        assertThat(result.get(1).getStudentId())
                .isEqualTo("studentId2");
    }

    @Test
    @DisplayName("WHEN findAllByCourseId is called THEN the list is empty")
    void noCourseStudentsTest() {
        List<CourseStudent> result = courseStudentRepository.findAllByCourseId("id1");
        assertThat(result.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("WHEN findAllByCourseId for non-existing THEN empty list is returned")
    void findByMissingCourseStudentIdTest() {
        courseStudentRepository.saveAll(List.of(
                new CourseStudent("courseStudendId1", "courseId1", "studentId1", 10),
                new CourseStudent("courseStudendId2", "courseId1", "studentId2", 10)
        ));

        List<CourseStudent> result = courseStudentRepository.findAllByCourseId("id3");
        assertThat(result.isEmpty()).isEqualTo(true);
    }

    @AfterEach
    void cleanup() {
        courseStudentRepository.deleteAll();
    }

}