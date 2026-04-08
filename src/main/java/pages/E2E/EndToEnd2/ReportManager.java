package pages.E2E.EndToEnd2;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ReportManager {
	 private static ExtentReports extent;

	    public static ExtentReports getReportInstance() {

	        if (extent == null) {
	            ExtentSparkReporter reporter = new ExtentSparkReporter("test-report.html");
	            reporter.config().setReportName("Coupa Automation Report");

	            extent = new ExtentReports();
	            extent.attachReporter(reporter);
	        }

	        return extent;
	    }
	}
