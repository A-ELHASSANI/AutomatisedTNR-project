package pages.purchaseorder;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/** Purchase Order detail / edit form. */
public class PurchaseOrderFormPage extends BasePage {

    private final By approveBtn   = By.cssSelector("input[value='Approve']");
    private final By submitBtn    = By.cssSelector("input[type='submit']");
    private final By cancelBtn    = By.cssSelector("a.cancel-order");
    private final By statusBadge  = By.cssSelector(".status-badge");

    public PurchaseOrderFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void approve() {
        click(approveBtn);
        log.info("Purchase Order approved");
    }

    public String getStatus() {
        return waitForVisible(statusBadge).getText();
    }

    public void submit() {
        click(submitBtn);
    }
}
