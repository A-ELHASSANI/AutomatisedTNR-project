package pages.E2E.EndToEnd2;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RequisitionTablePage {
	private WebDriver driver;
	private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	private By requestsMenu = By.linkText("Requests");
	private By requisitionRow = By.xpath(
			"//tr[.//td[contains(@class,'requested_by') and contains(text(),'Anas ELHASSANI')] and .//span[text()='100.00']]");

	private By receiveButton = By.cssSelector("img[id^='receive_requisition_']");
	private By receiptDateField = By.cssSelector("input[aria-label='Date']");
	private By massActionCheckbox = By.id("mass_action_cb");
	private By submitButton = By.id("receive_button");


	
	public RequisitionTablePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	
	public void openRequests() {
		wait.until(ExpectedConditions.elementToBeClickable(requestsMenu)).click();
	}

	public void clickReceiveForReq() {

	        WebElement row = wait.until(
	                ExpectedConditions.visibilityOfElementLocated(requisitionRow)
	        );

	        WebElement receiveBtn = row.findElement(receiveButton);

	        wait.until(ExpectedConditions.elementToBeClickable(receiveBtn));
	        receiveBtn.click();
	}
	public void receiptDate(String date) {
		WebElement dateInput = wait.until(
		        ExpectedConditions.elementToBeClickable(receiptDateField));

		dateInput.clear();
		dateInput.sendKeys(date);
	}
	
	public void receiptAll() {
		WebElement checkbox = wait.until(
		        ExpectedConditions.elementToBeClickable(massActionCheckbox));

		if (!checkbox.isSelected()) {
		    checkbox.click();
		}
	}
	public void receiveTruckIcon() {
		openRequests();
		clickReceiveForReq();
	}
	
	public void submitReceiption() {
		receiptAll();
		wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();		
	}
}
