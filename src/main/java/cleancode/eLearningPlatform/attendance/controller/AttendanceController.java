package cleancode.eLearningPlatform.attendance.controller;

import cleancode.eLearningPlatform.attendance.model.Attendance;
import cleancode.eLearningPlatform.attendance.model.AttendanceStatus;
import cleancode.eLearningPlatform.attendance.service.AttendanceService;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;

    @GetMapping("/filterBy")
    public ResponseEntity<List<Attendance>> getAttendanceList(
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate,
            @RequestParam(name = "username", defaultValue = "") String username,
            @RequestParam(name = "page") int pageNumber,
            @RequestParam(name = "numberOfItems") int numberOfItems
    ) {
        Pageable pageable = PageRequest.of(pageNumber, numberOfItems);
        List<Attendance> attendanceList = attendanceService.getAttendanceList(pageable, startDate, endDate, username.equals("") ? "" : username);

        if (attendanceList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(attendanceList);
        }
    }

    @GetMapping("/attendanceValues")
    public ResponseEntity<List<AttendanceStatus>> getAttendanceValues() {
        return ResponseEntity.ok(Arrays.asList(AttendanceStatus.values()));
    }

    @PostMapping()
    public ResponseEntity<?> saveAttendance(@RequestBody Attendance attendance) {
        try {
            Attendance savedAttendance = attendanceService.saveAttendance(attendance);
            return ResponseEntity.ok(savedAttendance);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PatchMapping("/changeStatus")
    public ResponseEntity<?> modifyAttendanceStatus(
            @RequestParam(name = "attendanceId") Long attendanceId,
            @RequestParam(name = "attendanceStatus") AttendanceStatus attendanceStatus
    ) {
        try{
            Attendance attendance = attendanceService.modifyAttendanceStatus(attendanceId, attendanceStatus);
            return ResponseEntity.ok( attendance);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }



    }

}
