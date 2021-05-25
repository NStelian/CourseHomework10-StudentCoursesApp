package ro.fasttrackit.tema11.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ro.fasttrackit.tema11.model.entity.Student;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    @Test
    @DisplayName("WHEN findByStudentId is called THEN first student is returned")
    void findByStudentIdTest() {
        studentRepository.saveAll(List.of(
                new Student("id1", "Steli", 23),
                new Student("id2", "Cipri", 23)
        ));

        Student result = studentRepository.findByStudentId("id1");
        assertThat(result.getName())
                .isEqualTo("Steli");
    }

    @Test
    @DisplayName("WHEN findByStudentId is called THEN the result is null")
    void noStudentsTest() {
        Student result = studentRepository.findByStudentId("id1");
        assertThat(result).isEqualTo(null);
    }

    @Test
    @DisplayName("WHEN findByStudentId for non-existing THEN null is returned")
    void findByMissingStudentIdTest() {
        studentRepository.saveAll(List.of(
                new Student("id1", "Steli", 23),
                new Student("id2", "Cipri", 23)
        ));

        Student result = studentRepository.findByStudentId("id3");
        assertThat(result).isEqualTo(null);
    }

    @AfterEach
    void cleanup() {
        studentRepository.deleteAll();
    }
}