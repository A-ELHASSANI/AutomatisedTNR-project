package pages.supplier;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Supplier Information Management (SIM) Form page.
 * Handles the "Supplier Creation Form - Aspen" workflow:
 *   open form → fill → select payment method → submit for approval → approver review.
 */
public class SupplierSimFormPage extends BasePage {

    // ── Locators ──────────────────────────────────────────────────────────────
    private final By formsMenu            = By.linkText("Forms");
    private final By supplierCreationForm = By.linkText("Supplier Creation Form - Aspen");
    private final By formContainer        = By.cssSelector("form, .sim-form, .form-container");
    private final By paymentMethodEft     = By.xpath("//input[@type='checkbox' and contains(@id,'EFT')]");
    private final By paymentMethodWire    = By.xpath("//input[@type='checkbox' and contains(@id,'WIRE')]");
    private final By submitForApprovalBtn = By.xpath(
        "//input[@value='Submit for Approval'] | //button[contains(text(),'Submit for Approval')]");
    private final By editBtn              = By.xpath(
        "//a[contains(text(),'Edit')] | //input[@value='Edit']");
    private final By approveBtn           = By.xpath(
        "//input[@value='Approve'] | //a[contains(text(),'Approve')]");
    private final By formStatusBadge      = By.cssSelector(".status-badge, span.status");

    public SupplierSimFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    /** Opens the Supplier Creation Form - Aspen from the top Forms menu. */
    public void openSupplierCreationForm() {
        click(formsMenu);
        click(supplierCreationForm);
        log.info("Opened Supplier Creation Form - Aspen");
    }

    // ── Assertions / state ────────────────────────────────────────────────────

    /** Returns true if the SIM form container is visible on the page. */
    public boolean isSimFormDisplayed() {
        try {
            waitForVisible(formContainer);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns the text of the status badge (e.g. "Pending Approval", "Approved"). */
    public String getFormStatus() {
        return waitForVisible(formStatusBadge).getText().trim();
    }

    // ── Form actions ──────────────────────────────────────────────────────────

    /** Selects EFT – Bank Transfer as the payment method. */
    public void selectPaymentMethodEft() {
        WebElement cb = waitForClickable(paymentMethodEft);
        if (!cb.isSelected()) cb.click();
        log.info("Payment method: EFT selected");
    }

    /** Selects WIRE – Direct Debit as the payment method. */
    public void selectPaymentMethodWire() {
        WebElement cb = waitForClickable(paymentMethodWire);
        if (!cb.isSelected()) cb.click();
        log.info("Payment method: WIRE selected");
    }

    /** Clicks the "Submit for Approval" button. */
    public void submitForApproval() {
        click(submitForApprovalBtn);
        log.info("Form submitted for approval");
    }

    // ── Approver actions ──────────────────────────────────────────────────────

    /**
     * Clicks the Edit button on the form response page.
     * Used by: Approver / Ultimate Approver role.
     */
    public void clickEditOnFormResponse() {
        click(editBtn);
        log.info("Clicked Edit on form response");
    }

    /**
     * Approves the form.
     * Used by: Approver, AIO Supplier admin, Global Purchasing Administrator.
     */
    public void approveForm() {
        click(approveBtn);
        log.info("Form approved");
    }
}
