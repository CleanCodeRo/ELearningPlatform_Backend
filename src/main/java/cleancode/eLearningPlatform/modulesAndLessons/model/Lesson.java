package cleancode.eLearningPlatform.modulesAndLessons.model;

import cleancode.eLearningPlatform.auth.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue
    private int id;
    private String name;

    @Column(length = 1000)
    private String description;

    private String gitHubLink;

    @JsonBackReference(value = "week")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Week week;

    @JsonBackReference(value = "user")
    @ManyToMany(fetch = FetchType.LAZY,  mappedBy = "completedLessons")
    private List<User> users;


}
