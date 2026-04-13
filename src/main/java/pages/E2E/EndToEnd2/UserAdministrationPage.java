package pages.E2E.EndToEnd2;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BaseTest;

public class UserAdministrationPage extends BaseTest {

//	private WebDriver driver;
//	private WebDriverWait wait;

	// Locators
	private By setupMenu = By.linkText("Setup");
	private By usersMenu = By.linkText("Users");
	private By searchUserField = By.id("sf_user");
	private By searchButton = By.id("sfBtn_user");
	private By actAsUserBtn = By.xpath("//img[@title='Act as this user']/parent::a");
	private By adminAcc = By.cssSelector("a[href*='act_as_user']");
	private By notificationBtn = By.cssSelector("button[aria-label='Notifications']");
	private final By notifPopover = By
			.cssSelector("[class*='coupaNotificationPopover'], [class*='notificationPopover']");
	private final By approveFromNotification = By
			.xpath("(//div[contains(@class,'coupaNotificationPopover__notificationItem')]"
					+ "//button[.//span[text()='Approuver']])[1]");
	private By toDoBtn = By.id("todoTab");
	public By poNumberElement = By.cssSelector("#msgbar a");

	public UserAdministrationPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	public void openSetup() {
		wait.until(ExpectedConditions.elementToBeClickable(setupMenu)).click();
	}

	public void openUsers() {
		wait.until(ExpectedConditions.elementToBeClickable(usersMenu)).click();
	}

	public void searchUser(String username) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(searchUserField)).clear();
		driver.findElement(searchUserField).sendKeys(username);
		driver.findElement(searchButton).click();
	}

	public void actAsUser() {
		wait.until(ExpectedConditions.elementToBeClickable(actAsUserBtn)).click();
	}

	public void actAsUser(String username) {
		openSetup();
		openUsers();
		searchUser(username);
	}

	public void closePopupIfStillOpen() {

		List<WebElement> popups = driver.findElements(notifPopover);

		if (!popups.isEmpty()) {

			try {
				Actions actions = new Actions(driver);
				actions.sendKeys(Keys.ESCAPE).perform();

			} catch (Exception e) {
				driver.findElement(By.tagName("body")).click();
			}
		}
	}

	public void uninspectAsUser() {
		((JavascriptExecutor) driver).executeScript("document.body.click();");
		WebElement admin = wait.until(ExpectedConditions.visibilityOfElementLocated(adminAcc));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", admin);

		wait.until(ExpectedConditions.elementToBeClickable(admin)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner, .overlay")));

	}

//	public void approveReq() {
//		wait.until(ExpectedConditions.elementToBeClickable(toDoBtn)).click();
//		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner, .overlay")));
//
//		WebElement requisitionRow = wait.until(ExpectedConditions.elementToBeClickable(
//		    By.xpath("//b[contains(@title, 'Anas ELHASSANI')]"))
//		);
//		requisitionRow.click();
//		pause(3000);
//	}
	public void approveReq() {
		String mainWindow = driver.getWindowHandle();
		log.info("Main window handle: {}", mainWindow);

		wait.until(ExpectedConditions.elementToBeClickable(toDoBtn)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner, .overlay")));

		log.info("Clicking on Anas ELHASSANI requisition...");
		WebElement requisitionRow = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//b[contains(@title, 'Anas ELHASSANI')]")));
		requisitionRow.click();

		log.info("Waiting for new window to open...");
		wait.until(driver -> driver.getWindowHandles().size() > 1);

		String requisitionWindow = null;
		for (String handle : driver.getWindowHandles()) {
			if (!handle.equals(mainWindow)) {
				driver.switchTo().window(handle);
				requisitionWindow = handle;
				log.info("Switched to new window. URL: {}", driver.getCurrentUrl());
				break;
			}
		}

		if (requisitionWindow == null) {
			log.error("Failed to switch to new window");
			throw new RuntimeException("New window did not open or could not be found");
		}

		wait.until(ExpectedConditions.urlContains("requisition_headers"));

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner, .overlay")));

		log.info("Clicking 'Approuver' button...");

		WebElement approveBtn = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//a[contains(@class, 'approve') and .//span[text()='Approuver']]")));
		approveBtn.click();

		pause(3000);
		log.info("Waiting for approval confirmation...");
//	    wait.until(ExpectedConditions.or(
//	        ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".success-message, .toast-success, [class*='success']")),
//	        ExpectedConditions.urlContains("success")
//	    ));
		log.info("Requisition approved successfully!");

		// Step 7: Optional - Close the new tab and return to main window
//	     driver.close();
//	     driver.switchTo().window(mainWindow);
//	     log.info("Returned to main window");	
	}

	public String getPOnumber() {
		String mainWindow = driver.getWindowHandle();
		log.info("Main window handle: {}", mainWindow);

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner, .overlay")));

		By activityBtn = By.xpath("//button[.//b[normalize-space()='2 Pc']]");

		WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(activityBtn));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);

		btn.click();

		log.info("Waiting for new window to open...");
		wait.until(driver -> driver.getWindowHandles().size() > 1);

		String requisitionWindow = null;
		for (String handle : driver.getWindowHandles()) {
			if (!handle.equals(mainWindow)) {
				driver.switchTo().window(handle);
				requisitionWindow = handle;
				log.info("Switched to new window. URL: {}", driver.getCurrentUrl());
				break;
			}
		}

		if (requisitionWindow == null) {
			log.error("Failed to switch to new window");
			throw new RuntimeException("New window did not open or could not be found");
		}
		WebElement poElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#msgbar a")));

		String fullText = poElement.getText();
		log.info("Raw PO text: {}", fullText);

		String poNumber = fullText.replace("PO #", "").trim();

		log.info("Extracted PO number: {}", poNumber);

		return poNumber;
	}

}
