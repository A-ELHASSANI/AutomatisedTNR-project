package base;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BasePage {

	protected WebDriver driver;
	protected WebDriverWait wait;
	protected static final Logger log = LogManager.getLogger(BasePage.class);
	protected final ConfigManager config = ConfigManager.getInstance();

	/**
	 * Initialisation du navigateur
	 */
	public void setup() {

		// Télécharge automatiquement la bonne version de ChromeDriver
		WebDriverManager.chromedriver().setup();

		// Options Chrome (tu peux ajouter headless plus tard si besoin)
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");

		// Création du driver
		driver = new ChromeDriver(options);

		// Wait global (10 secondes max)
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	public void tearDown() {

		if (driver != null) {
			driver.quit();
			log.info("Browser closed");
		}
	}

	/**
	 * Ouvrir une URL
	 */
	public void openUrl(String url) {
		driver.get(url);
		log.info("Opened URL: {}", url);
	}

	/**
	 * Retourne le driver (utile pour Page Object)
	 */
	public WebDriver getDriver() {
		return driver;
	}

	public WebDriverWait getWait() {
		return wait;
	}

	public WebElement waitForClickable(By locator) {
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public WebElement waitForVisible(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public void click(By locator) {
		waitForClickable(locator).click();
	}

	public void type(By locator, String text) {
		WebElement el = waitForClickable(locator);
		el.clear();
		el.sendKeys(text);
	}

	public void jsClick(By locator) {
		WebElement el = waitForVisible(locator);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
	}

	public void pause(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ignored) {
		}
	}

}
