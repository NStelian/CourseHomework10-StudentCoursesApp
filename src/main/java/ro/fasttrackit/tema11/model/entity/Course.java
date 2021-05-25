package ro.fasttrackit.tema11.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "courses")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
    @Id
    private String courseId;
    @NonNull
    private String discipline;
    @NonNull
    private String description;
}
