package cleancode.eLearningPlatform.modulesAndLessons.service;

import cleancode.eLearningPlatform.auth.model.User;
import cleancode.eLearningPlatform.auth.repository.UserRepository;
import cleancode.eLearningPlatform.auth.service.UserService;
import cleancode.eLearningPlatform.modulesAndLessons.model.Lesson;
import cleancode.eLearningPlatform.modulesAndLessons.model.Module;
import cleancode.eLearningPlatform.modulesAndLessons.model.Week;
import cleancode.eLearningPlatform.modulesAndLessons.repository.ModuleRepository;
import cleancode.eLearningPlatform.modulesAndLessons.repository.WeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeekService {
    private final WeekRepository weekRepository;
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public Week findWeekById(int weekId){
        return weekRepository.findById(weekId).orElse(null);
    }

    public List<Week> findAllWeeksByModuleId(int moduleId){
        List<Week> weeks = weekRepository.findAllByModuleIdOrderByNumber(moduleId);

        for(int i = 0; i < weeks.size(); i++){
            List<Lesson> lessons = weekRepository.getLessonsByWeekId(weeks.get(i).getId());
            weeks.get(i).setLessons(lessons);
        }

        return weeks;
    }

    @Transactional
    @Modifying
    public Week saveWeek(Week week, String authHeader){
        if(!userService.checkIfUserAdmin(authHeader)) return null;

        Module module = moduleRepository.findById(week.getModule().getId()).orElse(null);
        System.out.println("Week service, saved week " + week);
        userService.removeModuleFromAllUsers(module, false, new ArrayList<>());

        return weekRepository.save(week);
    }

    @Modifying
    public String deleteWeekById(int weekId, String authHeader){
        if(!userService.checkIfUserAdmin(authHeader)) return "Get lost";

        Week deletedWeek = weekRepository.findById(weekId).orElse(null);
        List<User> users =  userRepository.findAll();

        userService.removeWeekFromAllUsers(deletedWeek, false, false ,users);
        weekRepository.delete(deletedWeek);

        return "Deleted Week " +weekId + " Succesfull";
    }

    public Week updateWeek(int weekId, Week updatedWeek, String authHeader){
        if(!userService.checkIfUserAdmin(authHeader)) return null;
        Optional<Week> existingWeekOptional = weekRepository.findById(weekId);

        if(existingWeekOptional.isPresent()){
            Week existingWeek = existingWeekOptional.get();

            existingWeek.setNumber(updatedWeek.getNumber());
            existingWeek.setCategories(updatedWeek.getCategories());

            return weekRepository.save(existingWeek);
        }else{
            throw new IllegalArgumentException("Week with id: " + weekId + "can't be found");
        }
    }

    public Week updatePermissionToWeek(int weekId, int userId, String authHeader) {
        if(!userService.checkIfUserAdmin(authHeader)) return null;
        Optional<Week> week = weekRepository.findById(weekId);

        if(week.isPresent()){
            if(week.get().getUsersWithAccessWeek().contains(userId)){
                week.get().getUsersWithAccessWeek().remove(Integer.valueOf(userId));
                return weekRepository.save(week.get());
            }else{
                week.get().getUsersWithAccessWeek().add(userId);
                return weekRepository.save(week.get());
            }
        }
        return null;
    }
}
