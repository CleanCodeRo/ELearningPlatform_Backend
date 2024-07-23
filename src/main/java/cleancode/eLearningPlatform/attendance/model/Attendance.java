package cleancode.eLearningPlatform.attendance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "attendance")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate attendanceDate;

    @Column(nullable = false)
    private long userId;

    @Column
    private Boolean morning = false ;
    @Column
    private Boolean evening = false;

    @Column(nullable = false)
    private String status; // Present/Absent/Excused

    @Column(nullable = false)
    private int numberOfHours;



}
