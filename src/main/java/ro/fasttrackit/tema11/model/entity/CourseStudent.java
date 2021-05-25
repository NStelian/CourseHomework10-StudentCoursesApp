package ro.fasttrackit.tema11.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "course_student")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseStudent {
    @Id
    private String courseStudentId;
    private String courseId;
    private String studentId;
    private int grade;
}
