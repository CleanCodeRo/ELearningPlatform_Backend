package cleancode.eLearningPlatform.attendance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.swing.*;
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
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date attendanceDate;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private String status; // Present/Absent/Excused

    @Column(nullable = false)
    private int numberOfHours;


}
