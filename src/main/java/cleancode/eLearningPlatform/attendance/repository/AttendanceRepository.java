package cleancode.eLearningPlatform.attendance.repository;

import cleancode.eLearningPlatform.attendance.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findAttendancesByUserId(long userId);
    Optional<Attendance> findByUserIdAndAttendanceDate(long userId, LocalDate attendanceDate);

}
