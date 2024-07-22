package cleancode.eLearningPlatform.attendance.controller;

import cleancode.eLearningPlatform.attendance.model.Attendance;
import cleancode.eLearningPlatform.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/date")
    public List<Attendance> getAttendanceByDate(@RequestParam Date date) {
        return attendanceService.getAttendanceByDate(date);
    }

    @GetMapping("/user")
    public List<Attendance> getAttendanceByUserAndDate(@RequestParam long userId, @RequestParam Date date) {
        return attendanceService.getAttendanceByUserAndDate(userId, date);
    }

    @PostMapping
    public Attendance saveOrUpdateAttendance(@RequestBody Attendance entry) {
        return attendanceService.saveOrUpdateAttendance(entry);
    }
}
