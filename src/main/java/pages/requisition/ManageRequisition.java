package pages.requisition;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BaseTest;

public class ManageRequisition extends BaseTest {

	public ManageRequisition(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	private By notificationBtn = By.cssSelector("button[aria-label='Notifications']");
	private final By notifPopover = By
			.cssSelector("[class*='coupaNotificationPopover'], [class*='notificationPopover']");
	private final By approveFromNotification = By
			.xpath("(//div[contains(@class,'coupaNotificationPopover__notificationItem')]"
					+ "//button[.//span[text()='Approuver']])[1]");

	// -------------------------worked one from notification-------------------
//	public void approveReq() {
//
//		wait.until(ExpectedConditions.elementToBeClickable(notificationBtn)).click();
//		wait.until(ExpectedConditions.visibilityOfElementLocated(notifPopover));
//		wait.until(ExpectedConditions.elementToBeClickable(approveFromNotification)).click();
//		pause(3000);
//		wait.until(ExpectedConditions.elementToBeClickable(notificationBtn)).click();
//		wait.until(ExpectedConditions.invisibilityOfElementLocated(notifPopover));
//	}
	
}
