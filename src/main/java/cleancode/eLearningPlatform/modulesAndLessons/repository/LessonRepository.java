package cleancode.eLearningPlatform.modulesAndLessons.repository;

import cleancode.eLearningPlatform.modulesAndLessons.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findAllByWeekIdOrderByOptionalAscNumberAsc(int weekId);

    @Query("SELECT l.week.lessons FROM Lesson l WHERE l.id = :lessonId")
    List<Lesson> getRestOfLessons(int lessonId);

}
