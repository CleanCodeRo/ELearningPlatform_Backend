package cleancode.eLearningPlatform.modulesAndLessons.service;

import cleancode.eLearningPlatform.modulesAndLessons.model.Lesson;
import cleancode.eLearningPlatform.modulesAndLessons.model.Module;
import cleancode.eLearningPlatform.modulesAndLessons.model.Week;
import cleancode.eLearningPlatform.modulesAndLessons.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    public List<Lesson> findAllLessons() {
        return lessonRepository.findAll();
    }

    public List<Lesson> findLessonByWeekId(int weekId){
        return lessonRepository.findAllByWeekIdOrderByWeek(weekId);
    }

    public Lesson findLessonById(int lessonId){
        return lessonRepository.findById(lessonId).orElse(null);
    }

   public Lesson saveLesson( Lesson lesson){
    return lessonRepository.save(lesson);
    }

    @Transactional
    public String deleteLesson(int lessonId) {
        lessonRepository.deleteById(lessonId);
        return "Deleted Lesson " + lessonId;
    }
    public Lesson updateLesson(int lessonId, Lesson updatedLesson){
        Optional<Lesson> existUpdatedOptional = lessonRepository.findById(lessonId);

        if(existUpdatedOptional.isPresent()){
            Lesson existingLesson = existUpdatedOptional.get();

            existingLesson.setName(updatedLesson.getName());
            existingLesson.setDescription(updatedLesson.getDescription());
            existingLesson.setGitHubLink(updatedLesson.getGitHubLink());

            return lessonRepository.save(existingLesson);
        }else{
            throw new IllegalArgumentException("Lesson can't be found with id : "+ lessonId);
        }
    }
}
