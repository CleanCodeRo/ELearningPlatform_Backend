package cleancode.eLearningPlatform.modulesAndLessons.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue
    private int id;
    private String name;

    @Nullable
    private Integer number;

    @Column(length = 1000)
    private String description;

    private String gitHubLink;
    private boolean optional;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Week week;


    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", gitHubLink='" + gitHubLink + '\'' +
                ", optional=" + optional +
                ", week=" + week +
                '}';
    }
}
