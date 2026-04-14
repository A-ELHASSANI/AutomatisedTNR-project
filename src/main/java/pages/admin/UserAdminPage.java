package pages.admin;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * System Administration – user management page.
 */
public class UserAdminPage extends BasePage {

    private static final int DEFAULT_WAIT = 20;

    // ── Setup menu navigation ─────────────────────────────────────────────────

	private By setupMenu = By.linkText("Setup");
	private By usersMenu = By.linkText("Users");
	private By searchUserField = By.id("sf_user");
	private By searchButton = By.id("sfBtn_user");
	private By actAsUserBtn = By.xpath("//img[@title='Act as this user']/parent::a");
	private By adminAcc = By.cssSelector("a[href*='act_as_user']");
    private final By adminMenu    = By.linkText("Admin");
    private final By usersLink    = By.linkText("Users");
    
    // ── Return to admin ───────────────────────────────────────────────────────
    private final By returnToAdminLink = By.cssSelector("a[href*='act_as_user']");

    // ── Approval ──────────────────────────────────────────────────────────────
    private final By toDoTab          = By.id("todoTab");
    private final By spinner          = By.cssSelector(".loading, .spinner, .overlay");

    // ── PO number ─────────────────────────────────────────────────────────────
    public final By poNumberElement   = By.cssSelector("#msgbar a");

    public UserAdminPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT));
    }
   
    public void navigateToUsers() {
        click(adminMenu);
        click(usersLink);
        log.info("Navigated to Users admin page");
    }

    public void actAsUser(String userName) {
    	openSetup();
    	openUsers();
    	searchUser(userName);
    	pause(2000);
    	click(actAsUserBtn);
        log.info("Acting as user: {}", userName);
    }

    public void uninspectAsUser() {
		wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));

		WebElement admin = wait.until(ExpectedConditions.visibilityOfElementLocated(adminAcc));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", admin);

		wait.until(ExpectedConditions.elementToBeClickable(admin)).click();
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
	
//---------------------------------------------------------------------------
    /** Returns to the original admin session. */
    public void returnToAdmin() {
        ((JavascriptExecutor) driver).executeScript("document.body.click();");
        WebElement link = wait.until(ExpectedConditions.visibilityOfElementLocated(adminAcc));
        ((JavascriptExecutor) driver)
            .executeScript("arguments[0].scrollIntoView({block:'center'});", link);
        wait.until(ExpectedConditions.elementToBeClickable(link)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
        log.info("Returned to admin account");
    }
    
    /**
     * Clicks the To-Do tab, finds the requisition row for "Anas ELHASSANI",
     * opens it in a new window, clicks "Approuver", then closes the window.
     *
     * @param requesterName display name shown in the requisition list (e.g. "Anas ELHASSANI")
     */
    public void approveRequisition(String requesterName) {
        String mainWindow = driver.getWindowHandle();

        wait.until(ExpectedConditions.elementToBeClickable(toDoTab)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));

        By requisitionLink = By.xpath("//b[contains(@title,'" + requesterName + "')]");
        log.info("Looking for requisition by: {}", requesterName);
        wait.until(ExpectedConditions.elementToBeClickable(requisitionLink)).click();

        // Wait for the approval window to open
        wait.until(d -> d.getWindowHandles().size() > 1);

        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(mainWindow)) {
                driver.switchTo().window(handle);
                log.info("Switched to approval window: {}", driver.getCurrentUrl());
                break;
            }
        }

        wait.until(ExpectedConditions.urlContains("requisition_headers"));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));

        By approveBtn = By.xpath("//a[contains(@class,'approve') and .//span[text()='Approuver']]");
        wait.until(ExpectedConditions.elementToBeClickable(approveBtn)).click();
        log.info("Approuver clicked");

        pause(3000);

        driver.close();
        driver.switchTo().window(mainWindow);
        log.info("Approval window closed, back to main window");
    }

    // ── PO Number ─────────────────────────────────────────────────────────────

    /**
     * Reads the PO number from the message bar after a requisition is approved.
     * Returns the link text (e.g. "PO-00123").
     */
//    public String getPONumber() {
//        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(poNumberElement));
//        String po = el.getText().trim();
//        log.info("PO number found: {}", po);
//        return po;
//    }
    
    public String getPONumber() {
		String mainWindow = driver.getWindowHandle();
		log.info("Main window handle: {}", mainWindow);

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner, .overlay")));

		By activityBtn = By.xpath("//button[.//b[normalize-space()='2 Requests using cgi france, 2 Second item in the da']]");

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
    
  //---------------------------------------------------------------------------

}
