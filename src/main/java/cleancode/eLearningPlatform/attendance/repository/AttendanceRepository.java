package cleancode.eLearningPlatform.attendance.repository;

import cleancode.eLearningPlatform.attendance.model.Attendance;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    boolean existsByDateAndUserId(LocalDate date, Long userId);

    @Query("SELECT a FROM Attendance a " +
            "WHERE (cast(:startDate as date) is null OR a.date >= :startDate) " +
            "AND (cast(:endDate as date) is null OR a.date <= :endDate) " +
            "AND (:username IS NULL OR :username = '' OR lower(CONCAT(COALESCE(a.user.firstName, ''), ' ', COALESCE(a.user.lastName, ''))) LIKE lower(concat('%', :username, '%')))")
    List<Attendance> getAttendanceList(
            Pageable pageable,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("username") String username
    );
}

