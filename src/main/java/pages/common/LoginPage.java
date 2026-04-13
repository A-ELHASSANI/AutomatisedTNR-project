package pages.common;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Handles the Coupa login screen.
 */
public class LoginPage extends BasePage {

    private final By usernameField = By.id("user_login");
	private final By passwordField = By.id("user_password");
	private final By loginButton = By.xpath("//button[@type='submit']");

//	public void login(String user, String pass) {
//		driver.findElement(username).sendKeys(user);
//		driver.findElement(password).sendKeys(pass);
//		driver.findElement(loginButton).click();
//	}

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new org.openqa.selenium.support.ui.WebDriverWait(
                driver, java.time.Duration.ofSeconds(15));
    }
//    public LoginPage(WebDriver driver) {
//		this.driver = driver;
//	}

    public void login(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
        log.info("Login submitted for user: {}", username);
    }
}
