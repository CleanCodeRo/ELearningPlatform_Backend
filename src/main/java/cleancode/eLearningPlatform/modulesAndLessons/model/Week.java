package cleancode.eLearningPlatform.modulesAndLessons.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Week {


    @Id
    @GeneratedValue
    private int id;

    private int number;
    private String name;

    @JsonBackReference(value = "module")
    @ManyToOne(fetch=FetchType.LAZY)
    private Module module;

//    @JsonBackReference(value = "lessons")
    @OneToMany(mappedBy = "week",fetch = FetchType.EAGER ,cascade = CascadeType.ALL)
    private List<Lesson> lessons = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "week_categories", joinColumns = @JoinColumn(name = "week_id"))
    private List<String> categories = new ArrayList<>();

    @Override
    public String toString() {
        return "Week{" +
                "id=" + id +
                ", number=" + number +
                ", name='" + name + '\'' +
                '}';
    }
}
