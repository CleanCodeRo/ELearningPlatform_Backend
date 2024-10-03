package cleancode.eLearningPlatform.attendance.service;


import cleancode.eLearningPlatform.attendance.model.Attendance;
import cleancode.eLearningPlatform.attendance.model.AttendanceStatus;
import cleancode.eLearningPlatform.attendance.repository.AttendanceRepository;
import cleancode.eLearningPlatform.auth.model.Response;
import cleancode.eLearningPlatform.auth.model.User;
import cleancode.eLearningPlatform.auth.repository.UserRepository;
import cleancode.eLearningPlatform.shared.dto.PageResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void createAttendanceForNewDay(){
       List<User> users = userRepository.findAll();
       List<Attendance> attendanceForToday = users.stream()
               .map(user -> Attendance.builder()
                       .date(LocalDate.now().plusDays(30))
                       .status(AttendanceStatus.UNMARKED)
                       .user(user)
                       .build())
               .collect(Collectors.toList());

       attendanceRepository.saveAll(attendanceForToday);
    }

    public Attendance saveAttendance(Attendance attendance) {
        if(attendanceRepository.existsByDateAndUserId(attendance.getDate(), attendance.getUser().getId())){
            throw new RuntimeException("Attendance with date " + attendance.getDate() + " and for user " + attendance.getUser().getId() + " already exists");
        }else{
            return attendanceRepository.save(attendance);
        }
    }

    public PageResponseDto<List<Attendance>> getAttendanceList(Pageable pageable, LocalDate startDate, LocalDate endDate, String username, Long userId) {
        Page<Attendance> attendancePage = attendanceRepository.getAttendanceList(pageable, startDate, endDate, username, userId);
        return new PageResponseDto<>(attendancePage.stream().toList() , attendancePage.getTotalElements(), attendancePage.getNumber(), attendancePage.getTotalPages());
    }

    public Attendance modifyAttendanceStatus(Long attendanceId, AttendanceStatus attendanceStatus) {
        Attendance attendance = attendanceRepository.findById(attendanceId).orElse(null);

        if(attendance != null){
            attendance.setStatus(attendanceStatus);
            return attendanceRepository.save(attendance);
        }else{
            throw new RuntimeException("Attendance with id " + attendanceId + " not found");
        }
    }

    public Response populateAttendance(int days){
        List<User> users = userRepository.findAll();
        LocalDate today = LocalDate.now();

        for (int i = 0; i <= days; i++) {
            LocalDate date = today.plusDays(i);
            for( User user : users) {
                Attendance attendance = Attendance.builder()
                        .user(user)
                        .date(date)
                        .status(AttendanceStatus.UNMARKED)
                        .build();

                attendanceRepository.save(attendance);
            }
        }

        return Response.builder().response("Done").build();
    }
}
