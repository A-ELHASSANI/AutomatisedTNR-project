package pages.supplier;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/** Lists all suppliers and provides navigation, CSP invite, RFI, PO method, and announcement actions. */
public class SupplierListPage extends BasePage {

    // ── Existing locators ─────────────────────────────────────────────────────
    private final By suppliersMenu  = By.linkText("Suppliers");
    private final By newSupplierBtn = By.linkText("New Supplier");
    private final By searchField    = By.id("supplier_search");
    private final By searchBtn      = By.cssSelector("button[type='submit']");

    // ── CSP Invitation ────────────────────────────────────────────────────────
    private final By cspInviteAction = By.cssSelector("a[href*='invite_to_csp'], a[title*='Invite']");
    private final By inviteAllBtn    = By.xpath("//button[contains(text(),'Send to All')] | //a[contains(text(),'Send to All')]");

    // ── Request for Information (RFI) ─────────────────────────────────────────
    private final By rfiActionBtn  = By.cssSelector("a[title='Send Request for Information'], img[alt*='Send Request']");
    private final By rfiRequestBtn = By.xpath("//button[text()='Request'] | //input[@value='Request']");
    private final By rfiSendBtn    = By.xpath("//button[contains(text(),'Send Info Request')] | //input[@value='Send Info Request']");
    private final By rfiSuccessMsg = By.cssSelector(".flash-notice, .success-message");

    // ── PO Method ─────────────────────────────────────────────────────────────
    private final By poMethodDropdown = By.cssSelector("select[id*='po_method'], select[name*='po_method']");
    private final By supplierSaveBtn  = By.cssSelector("input[type='submit'][value='Save'], button.save-supplier");

    // ── Remit-To admin ────────────────────────────────────────────────────────
    private final By setupMenu      = By.linkText("Setup");
    private final By suppliersSetup = By.linkText("Suppliers");
    private final By remitToLink    = By.linkText("Remit-To");

    // ── Announcements ─────────────────────────────────────────────────────────
    private final By homePageMenu      = By.linkText("Home Page");
    private final By announcementsLink = By.linkText("Announcements");
    private final By createAnnouncBtn  = By.linkText("Create");
    private final By cspPortalRadio    = By.xpath("//input[@type='radio' and contains(@id,'csp')]");
    private final By announceSaveBtn   = By.cssSelector("input[type='submit'], button[type='submit']");
    private final By editAnnouncIcon   = By.cssSelector("a[title='Edit'], img[alt='Edit']");

    public SupplierListPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── Existing methods ──────────────────────────────────────────────────────

    public void navigate() {
        click(suppliersMenu);
        log.info("Navigated to Supplier list");
    }

    public void clickNewSupplier() {
        click(newSupplierBtn);
    }

    public void searchSupplier(String name) {
        type(searchField, name);
        click(searchBtn);
        log.info("Searched for supplier: {}", name);
    }

    public void openSupplierByName(String name) {
        click(By.linkText(name));
    }

    // ── CSP Invitation ────────────────────────────────────────────────────────

    /**
     * Sends a CSP invitation to all contacts of the currently open supplier.
     * Pre-condition: supplier must already be open in edit/detail mode.
     */
    public void sendCspInvitationToAllContacts() {
        click(cspInviteAction);
        pause(1500);
        click(inviteAllBtn);
        log.info("CSP invitation sent to all contacts");
    }

    // ── Request for Information (RFI) ─────────────────────────────────────────

    /**
     * Searches for a supplier, then clicks the green RFI arrow icon in the Actions column.
     */
    public void clickSendRfi(String supplierName) {
        searchSupplier(supplierName);
        click(rfiActionBtn);
        log.info("Clicked RFI action for supplier: {}", supplierName);
    }

    /**
     * In the RFI popup: clicks Request then Send Info Request.
     * Pre-condition: popup is already open with a form pre-selected.
     */
    public void submitRfi() {
        click(rfiRequestBtn);
        pause(1000);
        click(rfiSendBtn);
        log.info("RFI submitted");
    }

    public String getRfiConfirmationMessage() {
        return waitForVisible(rfiSuccessMsg).getText().trim();
    }

    // ── PO Method Update ─────────────────────────────────────────────────────

    /**
     * Updates the PO Method field to "Email" for the currently open supplier and saves.
     */
    public void updatePoMethodToEmail() {
        waitForClickable(poMethodDropdown);
        new Select(driver.findElement(poMethodDropdown)).selectByVisibleText("Email");
        click(supplierSaveBtn);
        log.info("PO Method updated to Email");
    }

    // ── Remit-To Administration ───────────────────────────────────────────────

    /** Navigates to Setup > Suppliers > Remit-To. */
    public void navigateToRemitToAdmin() {
        click(setupMenu);
        click(suppliersSetup);
        click(remitToLink);
        log.info("Navigated to Remit-To admin page");
    }

    // ── CSP Announcements ─────────────────────────────────────────────────────

    /**
     * Creates a new announcement visible on the Coupa Supplier Portal.
     *
     * @param title announcement title
     * @param body  announcement body text
     */
    public void createAnnouncement(String title, String body) {
        click(setupMenu);
        click(homePageMenu);
        click(announcementsLink);
        click(createAnnouncBtn);
        type(By.cssSelector("input[id*='title'], input[name*='title']"), title);
        type(By.cssSelector("textarea[id*='body'], textarea[name*='body']"), body);
        click(cspPortalRadio);
        click(announceSaveBtn);
        log.info("Announcement '{}' created", title);
    }

    /**
     * Opens the first announcement in the list, adds an extra "Show On" tab and saves.
     *
     * @param showOnTab tab label to enable, e.g. "Orders" or "Invoices"
     */
    public void editAnnouncementAddTab(String showOnTab) {
        click(setupMenu);
        click(homePageMenu);
        click(announcementsLink);
        click(editAnnouncIcon);
        By showOnCheckbox = By.xpath(
            "//label[contains(text(),'" + showOnTab + "')]//preceding-sibling::input");
        click(showOnCheckbox);
        click(announceSaveBtn);
        log.info("Announcement updated to show on: {}", showOnTab);
    }
}
