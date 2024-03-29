package cleancode.eLearningPlatform.modulesAndLessons.service;

import cleancode.eLearningPlatform.auth.model.User;
import cleancode.eLearningPlatform.auth.repository.UserRepository;
import cleancode.eLearningPlatform.auth.service.UserService;
import cleancode.eLearningPlatform.modulesAndLessons.model.Module;
import cleancode.eLearningPlatform.modulesAndLessons.model.Week;
import cleancode.eLearningPlatform.modulesAndLessons.repository.ModuleRepository;
import cleancode.eLearningPlatform.modulesAndLessons.repository.WeekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
        return weekRepository.findAllByModuleIdOrderByNumber(moduleId);
    }

    @Transactional
    @Modifying
    public Week saveWeek(Week week){
        Module module = moduleRepository.findById(week.getModule().getId()).orElse(null);
        userService.removeModuleFromAllUsers(module, false, new ArrayList<>());

        return weekRepository.save(week);
    }

    @Modifying
    public String deleteWeekById(int weekId){
        Week deletedWeek = weekRepository.findById(weekId).orElse(null);
        List<User> users =  userRepository.findAll();

        userService.removeWeekFromAllUsers(deletedWeek, false, false ,users);
        weekRepository.delete(deletedWeek);
        System.out.println("DELETE WEEK " + weekId + "_________________________________________________");
        return "Deleted Week " +weekId + " Succesfull";
    }

    public Week updateWeek(int weekId, Week updatedWeek){
        Optional<Week> existingWeekOptional = weekRepository.findById(weekId);

        if(existingWeekOptional.isPresent()){
            Week existingWeek = existingWeekOptional.get();

            existingWeek.setName(updatedWeek.getName());
            existingWeek.setNumber(updatedWeek.getNumber());
            existingWeek.setImgLink(updatedWeek.getImgLink());

            return weekRepository.save(existingWeek);
        }else{
            throw new IllegalArgumentException("Week with id: " + weekId + "can't be found");
        }
    }
}
