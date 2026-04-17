package pages.goodreceipt;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Good Receipt page – handles receipt creation from a purchase order.
 */
public class GoodReceiptPage extends BasePage {

    private static final int DEFAULT_WAIT = 10;

    // ── Navigation ────────────────────────────────────────────────────────────
    private final By requestsMenu     = By.linkText("Requests");

    // ── Receipt form ──────────────────────────────────────────────────────────
    private final By receiptDateField = By.cssSelector("input[aria-label='Date']");
    private final By massActionCb     = By.id("mass_action_cb");
    private final By firstRowCb       = By.cssSelector("#receive_req_order_line_tbody tr:first-child input.qr-cb");
    private final By submitButton     = By.id("receive_button");

    /** Username shown in the "Requested by" column — read from config at construction time. */
    private final String requestedBy;

    public GoodReceiptPage(WebDriver driver, String requestedByName) {
        this.driver      = driver;
        this.wait        = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT));
        this.requestedBy = requestedByName;
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    public void openRequests() {
        wait.until(ExpectedConditions.elementToBeClickable(requestsMenu)).click();
        log.info("Navigated to Requests");
    }

    // ── Row lookup ────────────────────────────────────────────────────────────

    /**
     * Builds a dynamic XPath to find the row for the current user in "Ordered" status.
     * No hardcoded amounts — just user name + Ordered status.
     */
    private By orderedRowLocator() {
        return By.xpath(
            "//tr[" +
            ".//td[contains(@class,'requested_by') and contains(.,'" + requestedBy + "')] " +
            "and .//td[contains(.,'Ordered')]" +
            "]"
        );
    }

    /** Clicks the truck/receive icon on the first matching ordered row. */
    public void clickReceiveButton() {
        WebElement row = wait.until(
            ExpectedConditions.visibilityOfElementLocated(orderedRowLocator()));
        WebElement receiveBtn = row.findElement(
            By.cssSelector("img[id^='receive_requisition_']"));
        wait.until(ExpectedConditions.elementToBeClickable(receiveBtn)).click();
        log.info("Receive button clicked for user: {}", requestedBy);
    }

    // ── Receipt form ──────────────────────────────────────────────────────────

    public void enterReceiptDate(String date) {
        WebElement dateInput = wait.until(
            ExpectedConditions.elementToBeClickable(receiptDateField));
        dateInput.clear();
        dateInput.sendKeys(date);
        log.info("Receipt date set: {}", date);
    }

    /** Selects all lines via the mass-action checkbox. */
    public void selectAllLines() {
        WebElement cb = wait.until(ExpectedConditions.elementToBeClickable(massActionCb));
        if (!cb.isSelected()) {
            cb.click();
        }
    }

    /** Selects only the first line (partial receipt). */
    public void selectFirstLineOnly() {
        WebElement cb = wait.until(ExpectedConditions.elementToBeClickable(firstRowCb));
        if (!cb.isSelected()) {
            cb.click();
        }
    }

    public void submit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        log.info("Receipt submitted");
    }

    // ── Compound actions ──────────────────────────────────────────────────────

    /** Opens Requests, finds the ordered row, and opens the receive form. */
    public void navigateToReceiveForm() {
        openRequests();
        clickReceiveButton();
    }
    
    /** Opens Requests, finds the ordered row, and opens the receive form. */
    public void navigateToReceiveFormByActivity() {
        openRequests();
        clickReceiveButton();
    }

    /** Full receipt: selects all lines and submits. */
    public void receiveAll(String date) {
        navigateToReceiveForm();
        enterReceiptDate(date);
        selectAllLines();
        submit();
    }

    /** Partial receipt: selects only the first line and submits. */
    public void receiveFirstLine(String date) {
        navigateToReceiveForm();
        enterReceiptDate(date);
        selectFirstLineOnly();
        submit();
        pause(10000);
        goHome();
        
    }
    
    /** Full receipt: selects all lines and submits. */
    public void receiveItems(String date) {
    	navigateToReceiveFormByActivity();
        enterReceiptDate(date);
        selectAllLines();
        submit();
    }
    public void goHome() {
    	WebElement home = wait.until(ExpectedConditions.elementToBeClickable(By.id("home")));
    	home.click();
    }
}
