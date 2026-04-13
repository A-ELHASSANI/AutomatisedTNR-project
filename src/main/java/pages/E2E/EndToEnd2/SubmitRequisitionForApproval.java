package pages.E2E.EndToEnd2;

import java.sql.Time;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;

public class SubmitRequisitionForApproval extends BasePage {
	private WebDriver driver;

	private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	private static final int SHORT_WAIT = 5;
	private static final int MEDIUM_WAIT = 10;
	RequisitionTaskPage req;

	// Locators
	private By SelectAllCheckbox = By.id("select_all_checkbox");
	private By editSelectedBtn = By.id("adjust_all");
	private By cancelEditing = By.id("reqBulkEditFormFieldCancelButton");
	private By basketIcon = By.className("s-currentCartCount");
	private By reviewBasket = By.className("coupaCartPopover__reviewCartBtn");
	private By titleBox = By.id("requisition_header_custom_field_1");
	private By commentBox = By.id("addCommentFieldId_6");
	private By addComment = By.id("add_comment_link");

	private By selectProjectButton = By.id("account_segment_3_lv_id_chosen");
	private By projectSearchField = By.cssSelector("input[type='search']");
	private By chooseButton = By.xpath("//a[contains(@class,'choose_dynamic_account')]//span[text()='Choose']");

	private By addFileAttachmentBtn = By.cssSelector("a.file-attachment");

	private By fileInput = By.xpath("//input[@type='file']");

	private By submitForApproval = By.id("submit_for_approval_link");
	private static final Logger log = LogManager.getLogger(SubmitRequisitionForApproval.class);
	int counter = 0;

	public SubmitRequisitionForApproval(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(MEDIUM_WAIT));
	}

	public void clickWhenItClickable(By locator) {
		WebElement cart = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		cart.click();
	}

	public void viewBasket() {
		clickWhenItClickable(basketIcon);
		clickWhenItClickable(reviewBasket);
	}

	public void editSelected() {
		pause(2000);
		WebElement selectCheckBox = wait.until(ExpectedConditions.elementToBeClickable(SelectAllCheckbox));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", selectCheckBox);

		selectCheckBox.click();
		log.info("Edit clicked succefully !! ");
		clickWhenItClickable(editSelectedBtn);
		log.info("Edit clicked succefully !! ");

		WebElement cancelBtn = wait.until(ExpectedConditions.elementToBeClickable(cancelEditing));
		cancelBtn.click();
		log.info("Clicked 'cancelBtn' button");
		if (selectCheckBox.isSelected()) {
			selectCheckBox.click();
		}
		pause(2000);
	}

	public void typeTitle(String text) {
		type(titleBox, text);
	}

	public void selectHorsProjet() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		try {
			pause(2000);
			WebElement projetTrigger = wait.until(ExpectedConditions.elementToBeClickable(selectProjectButton));
			projetTrigger.click();

			WebElement arrow = projetTrigger.findElement(By.tagName("b"));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", arrow);

			String xpath = "//ul[contains(@id, '_chosen_results')]//li[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'hors projet')]";
			WebElement horsProjetOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

			log.info("Found option: {}", horsProjetOption.getText());

			horsProjetOption.click();
			log.info("Selected 'Hors projet'");

			WebElement chooseBtn = wait.until(ExpectedConditions.elementToBeClickable(chooseButton));
			chooseBtn.click();
			log.info("Clicked 'Choose' button");

		} catch (Exception e) {
			log.error("Failed to select Hors projet: {}", e.getMessage());
			throw e;
		}
	}

	public void chooseBillingAcc() {

		List<WebElement> accounts = driver.findElements(By.cssSelector(".acct_picker"));

		for (int i = 0; i < accounts.size(); i++) {
			final int index = i;
			List<WebElement> refreshedAccounts = driver.findElements(By.cssSelector(".acct_picker"));
			WebElement account = refreshedAccounts.get(i);

			if (account.getAttribute("class").contains("missing")) {

				try {
					WebElement glass = account.findElement(By.className("sprite-magnifier"));

					((JavascriptExecutor) driver).executeScript("arguments[0].click();", glass);
					selectHorsProjet();

					log.info("Please select project AND click 'Choose' manually...");

					wait.until(driver -> {
						List<WebElement> updatedAccounts = driver.findElements(By.cssSelector(".acct_picker"));
						return !updatedAccounts.get(index).getAttribute("class").contains("missing");
					});

					log.info("Account filled → moving to next");
					Thread.sleep(6000);
					counter++;

				} catch (Exception e) {
					e.printStackTrace();
					log.warn("Error: " + e.getMessage());
				}
			}
		}

		log.info("Total processed: " + counter);
	}

	public void addComment(String text) {
		type(commentBox, text);
		click(addComment);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner, .overlay")));
	}

	public void saveNewLine() {
		click(submitForApproval);
	}

	public void validateTask() {
		click(submitForApproval);
	}

	public void validatRequisition(String title, String supplierName, String unitPrice, String date, String startDate) {
		typeTitle(title);
	}

	public void addAttachment(String filePath) {
		wait.until(ExpectedConditions.elementToBeClickable(addFileAttachmentBtn)).click();
		;
		wait.until(ExpectedConditions.presenceOfElementLocated(fileInput)).sendKeys(filePath);
		;

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner, .uploading")));

	}
}
