package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Static wait helpers for cases where BasePage is not available (e.g. utility code).
 */
public class WaitUtils {

    private WaitUtils() {}

    public static void sleep(long millis) {
        try { Thread.sleep(millis); } catch (InterruptedException ignored) {}
    }

    public static void waitForUrl(WebDriver driver, String partialUrl, int timeoutSec) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
                .until(ExpectedConditions.urlContains(partialUrl));
    }

    public static void waitForInvisibility(WebDriver driver, By locator, int timeoutSec) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSec))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}
