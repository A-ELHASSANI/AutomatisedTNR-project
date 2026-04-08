package pages.E2E.EndToEnd2;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

	private WebDriver driver;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}

	private By username = By.id("user_login");
	private By password = By.id("user_password");
	private By loginButton = By.xpath("//button[@type='submit']");

	public void login(String user, String pass) {
		driver.findElement(username).sendKeys(user);
		driver.findElement(password).sendKeys(pass);
		driver.findElement(loginButton).click();
	}
}
