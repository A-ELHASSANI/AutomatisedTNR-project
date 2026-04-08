package pages.goodreceipt;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Good Receipt page – handles receipt creation from a purchase order.
 */
public class GoodReceiptPage extends BasePage {

    private final By ordersMenu       = By.linkText("Orders");
    private final By purchaseOrdersLink = By.linkText("Purchase Orders");
    private final By receiveBtn       = By.cssSelector("a[id^='receive_']");
    private final By receiptDateField = By.cssSelector("input[aria-label='Date']");
    private final By quantityField    = By.cssSelector("input[id^='receipt_line_quantity_']");
    private final By submitBtn        = By.id("receive_button");

    public GoodReceiptPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigateToPurchaseOrders() {
        click(ordersMenu);
        click(purchaseOrdersLink);
        log.info("Navigated to Purchase Orders");
    }

    public void openReceiptForPO(String poNumber) {
        By poLink = By.linkText(poNumber);
        click(poLink);
        click(receiveBtn);
        log.info("Opened Good Receipt for PO: {}", poNumber);
    }

    public void setReceiptDate(String date) {
        WebElement el = waitForClickable(receiptDateField);
        el.clear();
        el.sendKeys(date);
    }

    public void setQuantity(String qty) {
        WebElement el = waitForClickable(quantityField);
        el.clear();
        el.sendKeys(qty);
    }

    public void submit() {
        click(submitBtn);
        log.info("Good Receipt submitted");
    }
}
