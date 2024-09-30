package cleancode.eLearningPlatform.attendance.controller;

import cleancode.eLearningPlatform.attendance.model.Attendance;
import cleancode.eLearningPlatform.attendance.model.AttendanceStatus;
import cleancode.eLearningPlatform.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

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
        List<Attendance> attendanceList = attendanceService.getAttendanceList(pageable, startDate, endDate, username);

        return ResponseEntity.ok(attendanceList);
    }

    @GetMapping("/values")
    public ResponseEntity<Map<AttendanceStatus, String>> getAttendanceValues() {
        List<AttendanceStatus> statuses = Arrays.asList(AttendanceStatus.values());
        Map<AttendanceStatus, String> result = new LinkedHashMap<>();

        statuses.stream().forEach(status -> result.put(status, status.getDefaultColor()) );
        return ResponseEntity.ok(result);
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

    @PostMapping("/populate/{days}")
    public ResponseEntity<?> saveAttendance(@PathVariable int days) {
       return ResponseEntity.ok(attendanceService.populateAttendance(days));
    }

    @PatchMapping("/changeStatus")
    public ResponseEntity<?> modifyAttendanceStatus(
            @RequestParam(name = "attendanceId") Long attendanceId,
            @RequestParam(name = "attendanceStatus") AttendanceStatus attendanceStatus
    ) {
        try{
            Attendance attendance = attendanceService.modifyAttendanceStatus(attendanceId, attendanceStatus);
            return ResponseEntity.ok(attendance);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }



    }

}
