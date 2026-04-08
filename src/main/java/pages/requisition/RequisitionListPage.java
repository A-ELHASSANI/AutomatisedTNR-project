package pages.requisition;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/** Requisition list / table page. */
public class RequisitionListPage extends BasePage {

    private final By requestsMenu     = By.linkText("Requests");
    private final By newReqBtn        = By.className("s-writeARequestBtn");
    private final By skipGuideBtn     = By.className("searchWizardFooter__skip");

    // Good-receipt specific
    private final By receiveButton    = By.cssSelector("img[id^='receive_requisition_']");
    private final By receiptDateField = By.cssSelector("input[aria-label='Date']");
    private final By massCheckbox     = By.id("mass_action_cb");
    private final By submitReceiptBtn = By.id("receive_button");

    public RequisitionListPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigate() {
        click(requestsMenu);
        log.info("Navigated to Requisition list");
    }

    public void startNewRequisition() {
        click(newReqBtn);
        pause(1500);
        // Skip the guided wizard if it appears
        try { click(skipGuideBtn); } catch (Exception ignored) {}
        log.info("Started new requisition");
    }

    /** Find a row by requester name and click its receive (truck) icon. */
    public void clickReceiveForUser(String requesterName, String amount) {
        By row = By.xpath(
                "//tr[.//td[contains(@class,'requested_by') and contains(text(),'" + requesterName + "')]" +
                " and .//span[text()='" + amount + "']]");
        WebElement rowEl = wait.until(ExpectedConditions.visibilityOfElementLocated(row));
        rowEl.findElement(receiveButton).click();
        log.info("Clicked receive for: {} / {}", requesterName, amount);
    }

    public void setReceiptDate(String date) {
        WebElement dateInput = wait.until(ExpectedConditions.elementToBeClickable(receiptDateField));
        dateInput.clear();
        dateInput.sendKeys(date);
    }

    public void checkMassActionAndSubmit() {
        WebElement cb = wait.until(ExpectedConditions.elementToBeClickable(massCheckbox));
        if (!cb.isSelected()) cb.click();
        click(submitReceiptBtn);
        log.info("Receipt submitted");
    }
}
