package pages.supplier;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Handles all actions performed on the Coupa Supplier Portal (CSP).
 * Covers: business profile, legal entity, payment method, remit-to,
 *         invoice export, and RFI notifications.
 */
public class SupplierCspPage extends BasePage {

    // ── Business Profile / Legal Entity ───────────────────────────────────────
    private final By businessProfileMenu = By.linkText("Business Profile");
    private final By legalEntityLink     = By.linkText("Legal Entity");
    private final By paymentMethodLink   = By.linkText("Payment Method");
    private final By saveBtn             = By.cssSelector("input[type='submit'], button.save");

    // ── Remit-To ──────────────────────────────────────────────────────────────
    private final By addRemitToBtn    = By.xpath(
        "//a[contains(text(),'Add Remit-To')] | //button[contains(text(),'Add Remit-To')]");
    private final By remitToShareBtn  = By.cssSelector("button.share-btn, a[title='Share']");
    private final By customerDropdown = By.cssSelector("select[id*='customer'], .customer-select");
    private final By nextBtn          = By.xpath("//button[text()='Next'] | //input[@value='Next']");

    // ── Invoice Export ────────────────────────────────────────────────────────
    private final By invoiceMenu       = By.linkText("Invoices");
    private final By exportDropdown    = By.cssSelector(".export-dropdown, button[id*='export']");
    private final By legalInvoiceOpt   = By.xpath(
        "//a[contains(text(),'Legal Invoice')] | //option[contains(text(),'Legal Invoice')]");

    // ── Notifications / RFI external form ────────────────────────────────────
    private final By notificationTab  = By.cssSelector("[href*='notification'], a[id*='notification']");
    private final By externalFormLink = By.cssSelector(".external-form-link, a[class*='form']");

    public SupplierCspPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── Business Profile ──────────────────────────────────────────────────────

    /** Opens the Business Profile section. */
    public void openBusinessProfile() {
        click(businessProfileMenu);
        log.info("Opened Business Profile");
    }

    /**
     * Navigates to Business Profile > Legal Entity.
     * Fill in required fields after calling this method, then call saveProfile().
     */
    public void openLegalEntity() {
        openBusinessProfile();
        click(legalEntityLink);
        log.info("Opened Legal Entity");
    }

    /** Saves the current form/profile page. */
    public void saveProfile() {
        click(saveBtn);
        log.info("Profile saved");
    }

    // ── Payment Method / Remit-To ─────────────────────────────────────────────

    /** Navigates to Business Profile > Payment Method. */
    public void openPaymentMethod() {
        openBusinessProfile();
        click(paymentMethodLink);
        log.info("Opened Payment Method");
    }

    /** Clicks the "Add Remit-To" button on the Payment Method page. */
    public void clickAddRemitTo() {
        click(addRemitToBtn);
        log.info("Clicked Add Remit-To");
    }

    /**
     * Clicks the share icon on the first remit-to address,
     * selects "Accor" from the customer dropdown, and clicks Next.
     */
    public void shareFirstRemitToWithAccor() {
        click(remitToShareBtn);
        waitForClickable(customerDropdown);
        new Select(driver.findElement(customerDropdown)).selectByVisibleText("Accor");
        click(nextBtn);
        log.info("Remit-To shared with Accor");
    }

    // ── Invoice Export ────────────────────────────────────────────────────────

    /** Opens the Invoices tab on CSP. */
    public void openInvoices() {
        click(invoiceMenu);
        log.info("Opened Invoices tab");
    }

    /**
     * From the Invoices tab, opens the Export dropdown and selects
     * "Legal Invoice (zip)" to trigger the email export.
     */
    public void exportLegalInvoiceZip() {
        openInvoices();
        click(exportDropdown);
        click(legalInvoiceOpt);
        log.info("Legal Invoice (zip) export triggered");
    }

    // ── Notifications / RFI ───────────────────────────────────────────────────

    /** Opens the Notification tab on CSP. */
    public void openNotifications() {
        click(notificationTab);
        log.info("Opened Notification tab");
    }

    /**
     * From the Notification tab, opens the external update form
     * sent by the admin via RFI.
     */
    public void openExternalForm() {
        click(externalFormLink);
        log.info("Opened external form from CSP notification");
    }
}
