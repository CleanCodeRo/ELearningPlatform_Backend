package cleancode.eLearningPlatform.attendance.service;


import cleancode.eLearningPlatform.attendance.model.Attendance;
import cleancode.eLearningPlatform.attendance.model.AttendanceStatus;
import cleancode.eLearningPlatform.attendance.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;

    public Attendance saveAttendance(Attendance attendance) {
        if(attendanceRepository.existsByDateAndUserId(attendance.getDate(), attendance.getUser().getId())){
            throw new RuntimeException("Attendance with date " + attendance.getDate() + " and for user " + attendance.getUser().getId() + " already exists");
        }else{
            return attendanceRepository.save(attendance);
        }
    }

    public List<Attendance> getAttendanceList(Pageable pageable, LocalDate startDate, LocalDate endDate, String username) {
        System.out.println(startDate + " " + endDate + " "  + " Username : " + username);

        return  attendanceRepository.getAttendanceList(pageable, startDate, endDate, username);
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
}
