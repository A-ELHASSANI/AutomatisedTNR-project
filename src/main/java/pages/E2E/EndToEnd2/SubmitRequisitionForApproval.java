package pages.E2E.EndToEnd2;

import java.time.Duration;
import java.util.List;
import org.apache.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;

public class SubmitRequisitionForApproval extends BasePage {
	private WebDriver driver;

	private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	private static final int SHORT_WAIT = 5;
	private static final int MEDIUM_WAIT = 10;
	RequisitionTaskPage req;

	// Locators
	private By basketIcon = By.className("s-currentCartCount");
	private By reviewBasket = By.className("coupaCartPopover__reviewCartBtn");
	private By titleBox = By.id("requisition_header_custom_field_1");
	private By glassIcon = By.className("sprite-magnifier");
	private By commentBox = By.id("addCommentFieldId_6");
	private By addComment = By.id("add_comment_link");

	private By chooseUO = By.id("account_segment_1_lv_id_chosen");
	private By chooseDPT = By.id("account_segment_2_lv_id_chosen");
	private By choosePRJ = By.id("account_segment_3_lv_id_chosen");
	private By selectProjectButton = By.xpath("//button[contains(text(),'Select')]");
	private By projectSearchField = By.cssSelector("input[type='search']");
	private By projectResult = By.xpath("//li[contains(.,'hors projet')]");
	private By chooseButton = By.xpath("//a[contains(@class,'choose_dynamic_account')]//span[text()='Choose']");

	private By addFileAttachmentBtn = By.cssSelector("a.file-attachment");
//	private By addFileAttachmentBtn = By.className("file-attachment");

	private By fileInput = By.xpath("//input[@type='file']");

	private By submitForApproval = By.id("submit_for_approval_link");
	private static final Logger log = LogManager.getLogger(SubmitRequisitionForApproval.class);
	int counter = 0;

	public SubmitRequisitionForApproval(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(MEDIUM_WAIT));
	}
	// ---------------- Helper Methods ----------------

	public void click(By locator) {
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}

	public void clickWhenItClickable(By locator) {
		WebElement cart = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		cart.click();
	}

	public void type(By locator, String text) {
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		element.clear();
		element.sendKeys(text);
	}

	public void viewBasket() {
		clickWhenItClickable(basketIcon);
		clickWhenItClickable(reviewBasket);
	}

	public void typeTitle(String text) {
		type(titleBox, text);
	}

//	public void chooseBillingAcc() {
//
//	    List<WebElement> accounts = driver.findElements(By.cssSelector(".acct_picker"));
//
//	    for (int i = 0; i < accounts.size(); i++) {
//	        final int index = i;
//	        List<WebElement> refreshedAccounts = driver.findElements(By.cssSelector(".acct_picker"));
//	        WebElement account = refreshedAccounts.get(i);
//
//	        if (account.getAttribute("class").contains("missing")) {
//
//	            try {
//	            	
//	            	WebElement glass = account.findElement(By.className("sprite-magnifier"));
//	            	((JavascriptExecutor) driver).executeScript("arguments[0].click();", glass);
//	            	
//	            	 // STEP 2 — Wait for the popup to appear in the DOM
////	                wait.until(d -> !driver.findElements(
////	                    By.cssSelector(".chosen-container-single")
////	                ).isEmpty());
//	            	super.pause(3000);
//	                // Find and open the Chosen dropdown
//	                WebElement chosenContainer = account.findElement(
//	                    By.cssSelector(".chosen-container-single")
//	                );
//	                WebElement chosenSingle = chosenContainer.findElement(
//	                    By.cssSelector("a.chosen-single")
//	                );
//
//	                ((JavascriptExecutor) driver).executeScript(
//	                    "arguments[0].click();", chosenSingle
//	                );
//
//	                log.info("Chosen dropdown opened → searching for 'Hors Projet'...");
//
//	                // Wait for the dropdown results list to be visible
//	                wait.until(d -> {
//	                    List<WebElement> updatedAccounts = driver.findElements(
//	                        By.cssSelector(".acct_picker")
//	                    );
//	                    WebElement ul = updatedAccounts.get(index)
//	                        .findElement(By.cssSelector(".chosen-results"));
//	                    return ul.isDisplayed();
//	                });
//
//	                // Find and click the "Hors Projet" option in the dropdown list
//	                List<WebElement> options = account.findElements(
//	                    By.cssSelector(".chosen-results li")
//	                );
//
//	                WebElement horsProjetOption = options.stream()
//	                    .filter(opt -> opt.getText().contains("Hors Projet"))
//	                    .findFirst()
//	                    .orElseThrow(() -> new RuntimeException("'Hors Projet' option not found in dropdown"));
//
//	                ((JavascriptExecutor) driver).executeScript(
//	                    "arguments[0].click();", horsProjetOption
//	                );
//
//	                log.info("'Hors Projet' selected successfully → moving to next");
//
//	                // Wait until the account is no longer marked as missing
//	                wait.until(d -> {
//	                    List<WebElement> updatedAccounts = driver.findElements(
//	                        By.cssSelector(".acct_picker")
//	                    );
//	                    return !updatedAccounts.get(index).getAttribute("class").contains("missing");
//	                });
//
//	                Thread.sleep(3000);
//	                counter++;
//
//	            } catch (Exception e) {
//	                e.printStackTrace();
//	                log.warn("Error: " + e.getMessage());
//	            }
//	        }
//	    }
//
//	    log.info("Total processed: " + counter);
//	}
	public void chooseBillingAcc() {

		List<WebElement> accounts = driver.findElements(By.cssSelector(".acct_picker"));

		for (int i = 0; i < accounts.size(); i++) {
			final int index = i;
			List<WebElement> refreshedAccounts = driver.findElements(By.cssSelector(".acct_picker"));
			WebElement account = refreshedAccounts.get(i);

			if (account.getAttribute("class").contains("missing")) {

				try {
					WebElement glass = account.findElement(By.className("sprite-magnifier"));

					// Selenium opens the popup
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", glass);

					log.info("Please select project AND click 'Choose' manually...");

					// WAIT until USER clicks Choose (account no longer missing)
					wait.until(driver -> {
						List<WebElement> updatedAccounts = driver.findElements(By.cssSelector(".acct_picker"));
						return !updatedAccounts.get(index).getAttribute("class").contains("missing");
					});

					log.info("Account filled → moving to next");
					Thread.sleep(3000);
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

//	public void addLine(String description, String supplierName, String unitPrice, String date, String startDate) {
//		RequisitionTaskPage pageToAddLine = new RequisitionTaskPage(driver);
//		pageToAddLine.fillRequisitionForAddLine(description, supplierName, unitPrice, date, startDate);
//	}

	public void saveNewLine() {
		click(submitForApproval);
	}

	public void validateTask() {
		click(submitForApproval);
	}

	public void validatRequisition(String title, String supplierName, String unitPrice, String date, String startDate) {
		typeTitle(title);
	}

	public void selectProject(String projectName) {

		click(selectProjectButton);

		wait.until(ExpectedConditions.visibilityOfElementLocated(projectSearchField)).sendKeys(projectName);

		By result = By.xpath("//li[contains(.,'" + projectName + "')]");
		wait.until(ExpectedConditions.elementToBeClickable(result)).click();

		click(chooseButton);
	}

	public void addAttachment(String filePath) {
		wait.until(ExpectedConditions.elementToBeClickable(addFileAttachmentBtn)).click();
		;
		wait.until(ExpectedConditions.presenceOfElementLocated(fileInput)).sendKeys(filePath);
		;

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner, .uploading")));

	}
}
