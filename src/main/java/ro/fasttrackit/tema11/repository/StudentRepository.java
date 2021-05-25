package ro.fasttrackit.tema11.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ro.fasttrackit.tema11.model.entity.Student;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    Student findByStudentId(String studentId);
}
