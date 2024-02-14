package cleancode.eLearningPlatform.specialKatas.model;

import cleancode.eLearningPlatform.auth.model.User;
import cleancode.eLearningPlatform.specialKatas.enums.Category;
import cleancode.eLearningPlatform.specialKatas.enums.Language;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class Kata {
    @Id
    @GeneratedValue
    private int id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String kataLink;

    @Min(1)
    @Max(8)
    private int level;

    @Enumerated(EnumType.STRING)
    private Language language;

    @ElementCollection(targetClass = Category.class,fetch = FetchType.EAGER)
    @CollectionTable(name = "kata_categories", joinColumns = @JoinColumn(name = "kata_id"))
    @Enumerated(EnumType.STRING)
    private List<Category> category;


}
