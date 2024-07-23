package cleancode.eLearningPlatform.attendance.service;

import cleancode.eLearningPlatform.attendance.model.Attendance;
import cleancode.eLearningPlatform.attendance.repository.AttendanceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;



    public Attendance getAttendanceByUserAndDate(long userId, LocalDate date) {

        return attendanceRepository.findByUserIdAndAttendanceDate(userId, date).orElse(null);

    }


    @Transactional
    public Attendance saveOrUpdateAttendance(Attendance entry) {
        if (entry == null) {
            throw new IllegalArgumentException("Attendance entry cannot be null");
        }

        // Ensure that the status is not null or empty
        if (entry.getStatus() == null || entry.getStatus().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }

        // Ensure that numberOfHours is non-negative
        if (entry.getNumberOfHours() < 0) {
            throw new IllegalArgumentException("Number of hours cannot be negative");
        }

        // Check if attendance record exists for the given user and date
        Optional<Attendance> attendance = attendanceRepository.findByUserIdAndAttendanceDate(entry.getUserId(), entry.getAttendanceDate());

        if (attendance.isPresent()) {
            Attendance existingAttendance = attendance.get();
            existingAttendance.setStatus(entry.getStatus());
            existingAttendance.setNumberOfHours(entry.getNumberOfHours());
            existingAttendance.setMorning(entry.getMorning());
            existingAttendance.setEvening(entry.getEvening());
            return attendanceRepository.save(existingAttendance);
        } else {
            return attendanceRepository.save(entry);
        }
    }


    public Attendance findAttendanceByUserIdAndDate(long userId, LocalDate attendanceDate) {
      return  attendanceRepository.findByUserIdAndAttendanceDate(userId,attendanceDate).get();
    }

    public List<Attendance> getAttendanceByUser(long userId) {

        return attendanceRepository.findAttendancesByUserId(userId);
    }
}
