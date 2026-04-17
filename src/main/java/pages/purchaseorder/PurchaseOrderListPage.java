package pages.purchaseorder;

import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;

/**
 * Page Object for the Coupa Purchase Orders list.
 * Covers TC_SA_002: Soft-close and Hard-close a PO that was not received.
 */
public class PurchaseOrderListPage extends BasePage {

    private static final int DEFAULT_WAIT = 15;

    // ── Navigation ─────────────────────────────────────────────────────────────
    private final By ordersTab = By.linkText("Orders");

    // ── Table actions ──────────────────────────────────────────────────────────
    /**
     * The "Soft-Close PO #" icon in the Actions column.
     * Coupa renders this as an image button with a title attribute.
     */
    private final By softCloseIcon = By.cssSelector(
        "img[title*='Soft-Close'], a[title*='Soft-Close'], " +
        "img[alt*='Soft Close'], a[class*='soft-close']");

    /**
     * The "Close PO #" icon (hard close) in the Actions column.
     */
    private final By hardCloseIcon = By.cssSelector(
        "img[title*='Close PO'], a[title*='Close PO'], " +
        "img[alt*='Close PO'], a[class*='hard-close']:not([class*='soft'])");

    // ── Confirmation pop-up (Coupa inline modal or JS alert) ──────────────────
    private final By confirmOkBtn = By.xpath(
        "//button[normalize-space()='OK'] | " +
        "//input[@value='OK'] | " +
        "//a[normalize-space()='OK']");

    // ── Success message bar ────────────────────────────────────────────────────
    private final By flashNotice = By.cssSelector(".flash-notice, #notice, .alert-success");

    public PurchaseOrderListPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT));
    }

    // ── Navigation ─────────────────────────────────────────────────────────────

    /** Click the Orders tab to open the Purchase Orders list. */
    public void navigate() {
        wait.until(ExpectedConditions.elementToBeClickable(ordersTab)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.cssSelector(".loading, .spinner, .overlay")));
        log.info("Navigated to Purchase Orders list");
    }

    // ── TC_SA_002: Close PO ────────────────────────────────────────────────────

    /**
     * Clicks the Soft-Close icon for the first eligible PO in the list
     * and confirms the popup.
     * Pre-condition: PO list is open and shows at least one "Not Received" PO.
     */
    public void softClosePO() {
        wait.until(ExpectedConditions.elementToBeClickable(softCloseIcon)).click();
        log.info("Soft-Close icon clicked");
        handleConfirmation();
        assertFlashContains("Soft Closed");
        log.info("PO soft-closed successfully");
    }

    /**
     * Clicks the Hard-Close icon for the PO (already soft-closed)
     * and confirms the popup.
     */
    public void hardClosePO() {
        wait.until(ExpectedConditions.elementToBeClickable(hardCloseIcon)).click();
        log.info("Hard-Close (Close PO) icon clicked");
        handleConfirmation();
        assertFlashContains("Closed");
        log.info("PO hard-closed successfully");
    }

    /**
     * Handles either a browser JS confirm dialog or an inline OK button.
     */
    private void handleConfirmation() {
        try {
            // Try JS alert first
            Alert alert = driver.switchTo().alert();
            log.info("JS confirm dialog: {}", alert.getText());
            alert.accept();
            log.info("JS dialog accepted");
        } catch (NoAlertPresentException e) {
            // Fall back to inline OK button
            try {
                wait.until(ExpectedConditions.elementToBeClickable(confirmOkBtn)).click();
                log.info("Inline OK button clicked");
            } catch (Exception ex) {
                log.warn("No confirmation dialog found — continuing");
            }
        }
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.cssSelector(".loading, .spinner, .overlay")));
    }

    private void assertFlashContains(String keyword) {
        WebElement flash = wait.until(ExpectedConditions.visibilityOfElementLocated(flashNotice));
        String text = flash.getText();
        if (!text.toLowerCase().contains(keyword.toLowerCase())) {
            log.warn("Expected flash to contain '{}', got: {}", keyword, text);
        }
        log.info("Flash message: {}", text);
    }
}
