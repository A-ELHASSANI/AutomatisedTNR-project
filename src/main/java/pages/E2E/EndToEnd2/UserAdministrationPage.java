package pages.E2E.EndToEnd2;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserAdministrationPage {

	private WebDriver driver;
	private WebDriverWait wait;

	public UserAdministrationPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	// Locators
	private By setupMenu = By.linkText("Setup");
	private By usersMenu = By.linkText("Users");
	private By searchUserField = By.id("sf_user");
	private By searchButton = By.id("sfBtn_user");
	private By actAsUserBtn = By.xpath("//img[@title='Act as this user']/parent::a");
	private By adminAcc = By.cssSelector("a[href*='act_as_user']");
	private By notificationBtn = By.cssSelector("button[aria-label='Notifications']");
//	private By approveFromNotification = By.xpath(
//			"(//div[contains(@class,'coupaNotificationPopover__notificationItem')]//button[.//span[text()='Approuver']])[1]");
	private final By notifPopover = By.cssSelector(
		    "[class*='coupaNotificationPopover'], [class*='notificationPopover']"
		);

		private final By approveFromNotification = By.xpath(
		    "(//div[contains(@class,'coupaNotificationPopover__notificationItem')]"
		    + "//button[.//span[text()='Approuver']])[1]"
		);
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

//	public void approveReq() {
//		wait.until(ExpectedConditions.elementToBeClickable(notificationBtn)).click();
//		  wait.until(ExpectedConditions.invisibilityOfElementLocated(
//			        By.cssSelector(".loading, .spinner, .overlay")
//			    ));
//
//		wait.until(ExpectedConditions.elementToBeClickable(approveFromNotification)).click();
//
//	}
	
	public void approveReq() {

	    // Step 1 — click the bell
	    wait.until(ExpectedConditions.elementToBeClickable(notificationBtn)).click();

	    // Step 2 — wait for the POPOVER PANEL to appear in the DOM
	    // This is the correct signal: the panel must exist before its children can be clickable.
	    // invisibilityOf(.loading) was wrong — that element may never appear,
	    // so the wait passes instantly before the popover has rendered.
	    wait.until(ExpectedConditions.visibilityOfElementLocated(notifPopover));

	    // Step 3 — now wait for the approve button inside the popover to be clickable
	    wait.until(ExpectedConditions.elementToBeClickable(approveFromNotification)).click();

	    // Step 4 — wait for the popover to CLOSE before returning
	    // This prevents the next stage from running while the closing animation is still active.
	    wait.until(ExpectedConditions.invisibilityOfElementLocated(notifPopover));
	}

//	CYRIL PLANCHE
	public void actAsUser(String username) {
		openSetup();
		openUsers();
		searchUser(username);
	}

//	public void uninspectAsUser() {
//		wait.until(ExpectedConditions.elementToBeClickable(adminAcc)).click();
//	}
	
	public void uninspectAsUser() {

	    // Wait for page loading / overlay to disappear
	    wait.until(ExpectedConditions.invisibilityOfElementLocated(
	        By.cssSelector(".loading, .spinner, .overlay")
	    ));

	    WebElement admin = wait.until(
	        ExpectedConditions.visibilityOfElementLocated(adminAcc)
	    );

	    ((JavascriptExecutor) driver)
	        .executeScript("arguments[0].scrollIntoView({block: 'center'});", admin);

	    wait.until(ExpectedConditions.elementToBeClickable(admin)).click();
	}
}
