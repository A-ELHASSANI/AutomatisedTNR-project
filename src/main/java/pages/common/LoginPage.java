package pages.common;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Handles the Coupa login screen.
 */
public class LoginPage extends BasePage {

    private final By usernameField = By.id("login");
    private final By passwordField = By.id("password");
    private final By loginButton   = By.name("commit");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new org.openqa.selenium.support.ui.WebDriverWait(
                driver, java.time.Duration.ofSeconds(15));
    }

    public void login(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
        log.info("Login submitted for user: {}", username);
    }
}
