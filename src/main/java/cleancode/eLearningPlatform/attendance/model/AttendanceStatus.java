package cleancode.eLearningPlatform.attendance.model;

public enum AttendanceStatus {
    PRESENT("#4ec49d"),
    LATE("#efbf04"),
    ABSENT("#ff4500"),
    EXCUSED("#1e90ff"),
    UNMARKED("#2b2a29");

    private final String defaultColor;

    AttendanceStatus(String defaultColor) {
        this.defaultColor = defaultColor;
    }

    public String getDefaultColor() {
        return defaultColor;
    }

}
