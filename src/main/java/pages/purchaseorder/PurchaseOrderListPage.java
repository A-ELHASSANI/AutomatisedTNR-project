package pages.purchaseorder;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/** Purchase Order list/search page. */
public class PurchaseOrderListPage extends BasePage {

    private final By ordersMenu     = By.linkText("Orders");
    private final By poLink         = By.linkText("Purchase Orders");
    private final By searchField    = By.id("search_field");
    private final By searchBtn      = By.cssSelector("button[type='submit']");

    public PurchaseOrderListPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigate() {
        click(ordersMenu);
        click(poLink);
        log.info("Navigated to Purchase Orders");
    }

    public void searchPO(String poNumber) {
        type(searchField, poNumber);
        click(searchBtn);
        log.info("Searched PO: {}", poNumber);
    }

    public void openPO(String poNumber) {
        click(By.linkText(poNumber));
    }
}
