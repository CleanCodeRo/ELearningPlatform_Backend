package cleancode.eLearningPlatform.attendance.repository;

import cleancode.eLearningPlatform.attendance.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByAttendanceDate(Date attendanceDate);
    List<Attendance> findByUserIdAndAttendanceDate(long userId, Date attendanceDate);
}
