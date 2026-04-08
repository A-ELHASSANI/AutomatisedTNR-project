package base;


import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import pages.common.LoginPage;
import reporting.ReportManager;

/**
 * BaseTest – TestNG lifecycle used by every test class.
 * Handles browser init, login, report initialisation, and teardown.
 */
public abstract class BaseTest extends BasePage {

    protected ExtentReports extent;
    protected ExtentTest testReport;

    @BeforeClass
    public void initSuite() {
        extent = ReportManager.getInstance();
    }

    /**
     * Call this from your @Test method to start a named test entry in the report.
     */
    protected void startTest(String testName) {
        testReport = extent.createTest(testName);
    }

    /**
     * Standard login flow – opens the Coupa URL and authenticates.
     */
    protected void loginAsDefault() throws InterruptedException {
        setup();
        openUrl(config.getBaseUrl() + "/sessions/new");
        new LoginPage(driver).login(config.getUsername(), config.getPassword());
        pause(3000);
        log.info("Logged in as {}", config.getUsername());
    }

    @AfterMethod
    public void captureResultOnFailure(ITestResult result) {
        if (!result.isSuccess() && testReport != null) {
            testReport.fail("Test failed: " + result.getThrowable().getMessage());
        }
    }

    @AfterClass
    public void closeSuite() {
        tearDown();
        if (extent != null) extent.flush();
    }
}
