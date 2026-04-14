package pages.requisition;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;

/**
 * Page Object for the Coupa requisition basket / cart review screen.
 * Covers: opening the basket, adding attachments, adding comments,
 * setting the title, filling billing account segments, and submitting for approval.
 */
public class RequisitionBasketPage extends BasePage {

    private static final int MEDIUM_WAIT = 10;

    // ── Cart / basket ─────────────────────────────────────────────────────────
    private final By basketIcon        = By.className("s-currentCartCount");
    private final By reviewBasketBtn   = By.className("coupaCartPopover__reviewCartBtn");

    // ── Header fields ─────────────────────────────────────────────────────────
    private final By titleField        = By.id("requisition_header_custom_field_1");
    private final By commentField      = By.id("addCommentFieldId_6");
    private final By addCommentBtn     = By.id("add_comment_link");

    // ── Attachment ────────────────────────────────────────────────────────────
    private final By addFileBtn        = By.cssSelector("a.file-attachment");
    private final By fileInput         = By.xpath("//input[@type='file']");

    // ── Billing account ───────────────────────────────────────────────────────
    private final By allAccountPickers  = By.cssSelector(".acct_picker");
    private final By magnifierIcon      = By.className("sprite-magnifier");
    private final By projectDropdown    = By.id("account_segment_3_lv_id_chosen");
    private final By chooseButton       = By.xpath("//a[contains(@class,'choose_dynamic_account')]//span[text()='Choose']");
    private final By SelectAllCheckbox 	= By.id("select_all_checkbox");
	private final By editSelectedBtn 	= By.id("adjust_all");
	private final By cancelEditing 		= By.id("reqBulkEditFormFieldCancelButton");

    // ── Submit ────────────────────────────────────────────────────────────────
    private final By submitForApprovalBtn = By.id("submit_for_approval_link");

    // ── Spinner ───────────────────────────────────────────────────────────────
    private final By spinner = By.cssSelector(".loading, .spinner, .overlay");

    public RequisitionBasketPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(MEDIUM_WAIT));
    }

    // ── Basket navigation ─────────────────────────────────────────────────────

    /** Clicks the cart icon and then "Review basket". */
    public void openBasket() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(basketIcon)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(reviewBasketBtn)).click();
        log.info("Basket opened");
    }

    // ── Header fields ─────────────────────────────────────────────────────────

    public void enterTitle(String title) {
        type(titleField, title);
        log.info("Title set: {}", title);
    }

    public void addComment(String comment) {
        type(commentField, comment);
        click(addCommentBtn);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
        log.info("Comment added: {}", comment);
    }

    // ── Attachment ────────────────────────────────────────────────────────────

    public void addAttachment(String filePath) {
        wait.until(ExpectedConditions.elementToBeClickable(addFileBtn)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(fileInput)).sendKeys(filePath);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.cssSelector(".loading, .spinner, .uploading")));
        log.info("Attachment added: {}", filePath);
    }

    // ── Billing account ───────────────────────────────────────────────────────

    /**
     * Iterates all account pickers on the page.
     * For every picker marked as "missing", opens the project chooser
     * and selects "Hors projet", then confirms with "Choose".
     */
    public void fillMissingBillingAccounts() {
        List<WebElement> pickers = driver.findElements(allAccountPickers);
        int filled = 0;

        for (int i = 0; i < pickers.size(); i++) {
            List<WebElement> refreshed = driver.findElements(allAccountPickers);
            WebElement picker = refreshed.get(i);

            if (!picker.getAttribute("class").contains("missing")) {
                continue;
            }

            try {
                WebElement glass = picker.findElement(magnifierIcon);
                ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", glass);

                selectHorsProjet();

                final int idx = i;
                wait.until(d -> {
                    List<WebElement> updated = d.findElements(allAccountPickers);
                    return !updated.get(idx).getAttribute("class").contains("missing");
                });

                pause(6000);
                filled++;
                log.info("Billing account {} filled", i + 1);

            } catch (Exception e) {
                log.warn("Could not fill billing account {}: {}", i + 1, e.getMessage());
            }
        }

        log.info("Total billing accounts filled: {}", filled);
    }

    /** Selects "Hors projet" from the project segment chosen dropdown. */
    private void selectHorsProjet() {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement trigger = longWait.until(ExpectedConditions.elementToBeClickable(projectDropdown));
        trigger.click();
        WebElement arrow = trigger.findElement(By.tagName("b"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", arrow);

        String horsProjetXpath =
            "//ul[contains(@id,'_chosen_results')]//li[contains(" +
            "translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
            "'hors projet')]";

        longWait.until(ExpectedConditions.elementToBeClickable(By.xpath(horsProjetXpath))).click();
        log.info("'Hors projet' selected");

        longWait.until(ExpectedConditions.elementToBeClickable(chooseButton)).click();
        log.info("'Choose' clicked");
    }
    
    public void editSelected() {
		pause(2000);
		WebElement selectCheckBox = wait.until(ExpectedConditions.elementToBeClickable(SelectAllCheckbox));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", selectCheckBox);

		selectCheckBox.click();
		log.info("Edit clicked succefully !! ");
		WebElement edit = waitForVisible(editSelectedBtn);
		edit.click();
		log.info("Edit clicked succefully !! ");

		WebElement cancelBtn = wait.until(ExpectedConditions.elementToBeClickable(cancelEditing));
		cancelBtn.click();
		log.info("Clicked 'cancelBtn' button");
		if (selectCheckBox.isSelected()) {
			selectCheckBox.click();
		}
		pause(2000);
	}
//    public void clickWhenItClickable(By locator) {
//		WebElement cart = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
//		cart.click();
//	}
    // ── Submit ────────────────────────────────────────────────────────────────

    public void submitForApproval() {
        click(submitForApprovalBtn);
        log.info("Requisition submitted for approval");
    }
}
