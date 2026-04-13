package utils;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WindowHelper {
	/**
	 * Switches to the most recently opened window/tab
	 * @param driver WebDriver instance
	 * @param mainWindowHandle The original window handle to exclude
	 * @param wait WebDriverWait instance
	 * @return The handle of the new window
	 */
	public static String switchToNewWindow(WebDriver driver, String mainWindowHandle, WebDriverWait wait) {
	    wait.until(d -> d.getWindowHandles().size() > 1);
	    
	    for (String handle : driver.getWindowHandles()) {
	        if (!handle.equals(mainWindowHandle)) {
	            driver.switchTo().window(handle);
	            return handle;
	        }
	    }
	    throw new RuntimeException("No new window found");
	}

	/**
	 * Switches to window by URL pattern (more robust when multiple tabs exist)
	 */
	public static void switchToWindowByUrl(WebDriver driver, String urlPattern, int timeoutSec) {
	    new WebDriverWait(driver, Duration.ofSeconds(timeoutSec)).until(d -> {
	        for (String handle : d.getWindowHandles()) {
	            d.switchTo().window(handle);
	            if (d.getCurrentUrl().contains(urlPattern)) {
	                return true;
	            }
	        }
	        return false;
	    });
	}
	
//	::::::::::    utility of helper     :::::: 
//		// Replace the manual loop with:
//		switchToNewWindow(driver, mainWindow, wait);
//		// OR if you know the URL pattern:
//		switchToWindowByUrl(driver, "164183", 15); // waits for URL containing request ID
}
