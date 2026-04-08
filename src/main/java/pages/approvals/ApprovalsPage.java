package pages.approvals;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/** Approvals dashboard – approve, reject, or delegate requests. */
public class ApprovalsPage extends BasePage {

    private final By approvalsMenu = By.linkText("Approvals");
    private final By approveBtn    = By.cssSelector("a.approve-btn, input[value='Approve']");
    private final By rejectBtn     = By.cssSelector("a.reject-btn");
    private final By commentField  = By.id("approval_comment");

    public ApprovalsPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigate() {
        click(approvalsMenu);
        log.info("Navigated to Approvals");
    }

    public void approveFirstPending() {
        click(approveBtn);
        log.info("Approved first pending item");
    }

    public void rejectWithComment(String comment) {
        click(rejectBtn);
        type(commentField, comment);
        log.info("Rejected with comment: {}", comment);
    }

    /** Open a specific approval by document name */
    public void openApproval(String documentName) {
        click(By.linkText(documentName));
    }
}
