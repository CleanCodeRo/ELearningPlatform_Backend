package cleancode.eLearningPlatform.attendance.controller;

import cleancode.eLearningPlatform.attendance.model.Attendance;
import cleancode.eLearningPlatform.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/overall")
    public List<Attendance> getAttendanceByDate(@RequestParam long userId) {
        return attendanceService.getAttendanceByUser(userId);
    }

    @GetMapping("/user")
    public ResponseEntity<Attendance> getAttendanceByUser(
            @RequestParam(name = "userId") long userId,
            @RequestParam(name= "attendanceDate") LocalDate attendanceDate) {
//        System.out.println(userId + " userId");
//        System.out.println(attendanceDate + " attendanceDate");
//        return null;
        try {
            System.out.println(userId + " userId");
            System.out.println(attendanceDate + " attendanceDate");

            Attendance attendance = attendanceService.getAttendanceByUserAndDate(userId, attendanceDate);
            return ResponseEntity.ok(attendance);
        } catch (RuntimeException e) {
            // Return 404 Not Found if attendance is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }



    @PostMapping
    public ResponseEntity<Attendance> createAttendance(@RequestBody Attendance attendance) {
        Attendance savedAttendance = attendanceService.saveOrUpdateAttendance(attendance);
        return ResponseEntity.ok(savedAttendance);
    }

}
