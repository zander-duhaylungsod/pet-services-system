package CCE104;

public class ReportRecord {
    private int reportID;
    private String reportTitle;
    private String reportType;
    private String generatedDate; // Using String for UI compatibility
    private String content;
    private int createdBy;

    // Constructor
    public ReportRecord(int reportID, String reportTitle, String reportType, String generatedDate, String content, int createdBy) {
        this.reportID = reportID;
        this.reportTitle = reportTitle;
        this.reportType = reportType;
        this.generatedDate = generatedDate;
        this.content = content;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(String generatedDate) {
        this.generatedDate = generatedDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    // Singleton Instance and Selection Handling
    private static ReportRecord instance;
    private static ReportRecord selectedReport;
    private ReportsController reportsController;

    public static ReportRecord getSelectedReport() {
        return selectedReport;
    }

    public static void setSelectedReport(ReportRecord report) {
        selectedReport = report;
    }

    private ReportRecord() {}

    public static ReportRecord getInstance() {
        if (instance == null) {
            instance = new ReportRecord();
        }
        return instance;
    }

    public ReportsController getReportsController() {
        return reportsController;
    }

    public void setReportsController(ReportsController reportsController) {
        this.reportsController = reportsController;
    }

    @Override
    public String toString() {
        return "ReportRecord{" +
                "reportID=" + reportID +
                ", reportTitle='" + reportTitle + '\'' +
                ", reportType='" + reportType + '\'' +
                ", generatedDate='" + generatedDate + '\'' +
                ", content='" + content + '\'' +
                ", createdBy=" + createdBy +
                '}';
    }
}
