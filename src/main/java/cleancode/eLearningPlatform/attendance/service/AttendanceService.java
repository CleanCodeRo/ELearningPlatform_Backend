package cleancode.eLearningPlatform.attendance.service;

import cleancode.eLearningPlatform.attendance.model.Attendance;
import cleancode.eLearningPlatform.attendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;


    public List<Attendance> getAttendanceByDate(Date date) {
        return attendanceRepository.findByAttendanceDate(date);
    }

    public List<Attendance> getAttendanceByUserAndDate(long userId, Date date) {
        return attendanceRepository.findByUserIdAndAttendanceDate(userId, date);
    }

    public Attendance saveOrUpdateAttendance(Attendance entry) {
        return attendanceRepository.save(entry);
    }
}
