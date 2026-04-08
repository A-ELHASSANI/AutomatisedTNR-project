package reporting;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import config.ConfigManager;

/**
 * Singleton – one ExtentReports instance per JVM run.
 * Each execution generates a timestamped HTML report.
 */
public class ReportManager {

    private static ExtentReports instance;

    private ReportManager() {}

    public static ExtentReports getInstance() {
        if (instance == null) {
            String folder   = ConfigManager.getInstance().get("report.folder");
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String reportPath = folder + File.separator + "report_" + timestamp + ".html";

            new File(folder).mkdirs();

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("Coupa Automation Suite");
            spark.config().setDocumentTitle("Coupa Test Report");

            instance = new ExtentReports();
            instance.attachReporter(spark);
            instance.setSystemInfo("Application", "Coupa Procurement");
            instance.setSystemInfo("Environment", ConfigManager.getInstance().getBaseUrl());
        }
        return instance;
    }

    /** Call between test runs to reset (e.g. for parallel module execution). */
    public static void reset() { instance = null; }
}
