package cleancode.eLearningPlatform.specialKatas.service;

import cleancode.eLearningPlatform.auth.model.Response;
import cleancode.eLearningPlatform.auth.model.User;
import cleancode.eLearningPlatform.auth.repository.UserRepository;
import cleancode.eLearningPlatform.auth.service.UserService;
import cleancode.eLearningPlatform.modulesAndLessons.model.Status;
import cleancode.eLearningPlatform.specialKatas.enums.Category;
import cleancode.eLearningPlatform.specialKatas.model.Kata;
import cleancode.eLearningPlatform.specialKatas.model.KataPaginationResponse;
import cleancode.eLearningPlatform.specialKatas.model.PaginationRequest;
import cleancode.eLearningPlatform.specialKatas.repository.KataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class KataService {
    private final KataRepository kataRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    Random random = new Random();

    private List<Kata> kataOfTheDay = new ArrayList<>();

    @Cacheable(value = "kataCache", key = "#root.methodName")
    public List<Kata> getKataOfTheDay() {
        return kataOfTheDay;
    }

    //    cron = "0 0 0 * * ?"  runs daily at midnight
//    fixedRate = 1000 * 20 runs at a timer
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void selectKataOfTheDay() {
        List<Kata> firstGradeKatas = kataRepository.findAllByLevel(List.of(1, 2));
        List<Kata> secondGradeKatas = kataRepository.findAllByLevel(List.of(3, 4));
        List<Kata> thirdGradeKatas = kataRepository.findAllByLevel(List.of(5, 6));
        List<Kata> fourthGradeKatas = kataRepository.findAllByLevel(List.of(7, 8));

        kataOfTheDay = getOneRandomItemFromLists(List.of(firstGradeKatas, secondGradeKatas, thirdGradeKatas, fourthGradeKatas));
    }

    public KataPaginationResponse findAllKatas(PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getNumberOfItems());

        return KataPaginationResponse
                .builder()
                .katas(kataRepository.getSomeKata(pageable))
                .numberOfKatas(kataRepository.count())
                .build();
    }

    public Kata saveKata(Kata kata) {
        Optional<Kata> existingKata = kataRepository.findByTitleAndKataLink(kata.getTitle(), kata.getKataLink());
        // Kata does not exist, save it
        return existingKata.orElseGet(() -> kataRepository.save(kata));
    }

    public boolean kataExists(String title, String kataLink) {
        Optional<Kata> existingKata = kataRepository.findByTitleAndKataLink(title, kataLink);
        return existingKata.isPresent();
    }

    @Modifying
    @Transactional
    public Response deleteKata(int id) {
        kataRepository.deleteById(id);
        userService.removeKataFromAllUsers(id);
        return Response.builder().response("Kata deleted").build();
    }

    private List<Kata> getOneRandomItemFromLists(List<List<Kata>> lists) {
        List<Kata> result = new ArrayList<>();
        for (List<Kata> kataList : lists) {
            int randomIndex = random.nextInt(kataList.size());
            result.add(kataList.get(randomIndex));
        }

        return result;
    }

    public Kata getKataById(int kataId) {
        return kataRepository.findById(kataId).orElse(null);
    }

    public Kata editKata(Kata kata) {
        boolean kataExists = kataRepository.existsByTitle(kata.getTitle());

        if (kataExists) {
            return null;
        } else {
            return kataRepository.save(kata);
        }
    }

    public KataPaginationResponse getFilteredKatas(Category category, String status, Integer difficulty, Long userId, Pageable pageable) {
        List<Kata> initialFilter = new ArrayList<>();
        long totalNumberOfKatas = 0;

//        if (!category.equals(Category.ALL) && difficulty != 0 && !status.equals("ALL")) {
//            initialFilter = kataRepository.findByCategoryAndLevelAndCompletedByUsers(category, difficulty, userId, pageable);
//            totalNumberOfKatas = kataRepository.countByCategoryAndLevelAndCompletedByUsers(category, difficulty, userId);
//        }else if(!category.equals(Category.ALL) && difficulty != 0){
//            initialFilter = kataRepository.findByCategoryAndLevel(category, difficulty, pageable);
//            totalNumberOfKatas = kataRepository.countByCategoryAndLevel(category, difficulty);
//        }else if(!category.equals(Category.ALL)  && !status.equals("ALL")){
//            initialFilter = kataRepository.findByCategoryAndCompletedByUsers(category, userId, pageable);
//            totalNumberOfKatas = kataRepository.countByCategoryAndCompletedByUsers(category, difficulty);
//        }


        return KataPaginationResponse
                .builder()
                .katas(initialFilter)
                .numberOfKatas(totalNumberOfKatas)
                .build();
    }

    public Response addOrRemoveUserFromKata(Long userId, int kataId, Status status) {
        User user = userRepository.findById(userId).orElse(null);
        Kata kata = kataRepository.findById(kataId).orElse(null);

        assert user != null;
        assert kata != null;

        if (!kata.getCompletedByUsers().contains(userId)) {
            if (user.getRankPoints() == null) {
                user.setRankPoints(0);
            }
            kata.getCompletedByUsers().add(userId);
            user.setRankPoints(user.getRankPoints() + (54 - (kata.getLevel() * 6)));
        }
        kataRepository.save(kata);
        userRepository.save(user);
        return Response.builder().response("User has been updated").build();
    }

}
