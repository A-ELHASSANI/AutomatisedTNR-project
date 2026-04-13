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

	private By setupMenu = By.linkText("Setup");
	private By usersMenu = By.linkText("Users");
	private By searchUserField = By.id("sf_user");
	private By searchButton = By.id("sfBtn_user");
	private By actAsUserBtn = By.xpath("//img[@title='Act as this user']/parent::a");
	private By adminAcc = By.cssSelector("a[href*='act_as_user']");
    private final By adminMenu    = By.linkText("Admin");
    private final By usersLink    = By.linkText("Users");

    public UserAdminPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
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
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner, .overlay")));

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
}
