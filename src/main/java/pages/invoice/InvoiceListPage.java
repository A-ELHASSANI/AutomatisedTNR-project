package pages.invoice;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/** Invoice list page. */
public class InvoiceListPage extends BasePage {

    private final By invoicesMenu  = By.linkText("Invoices");
    private final By newInvoiceBtn = By.linkText("New Invoice");
    private final By searchField   = By.id("invoice_search");
    private final By searchBtn     = By.cssSelector("button[type='submit']");

    public InvoiceListPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void navigate() {
        click(invoicesMenu);
        log.info("Navigated to Invoices");
    }

    public void clickNewInvoice() { click(newInvoiceBtn); }

    public void searchInvoice(String ref) {
        type(searchField, ref);
        click(searchBtn);
    }

    public void openInvoiceByRef(String ref) { click(By.linkText(ref)); }
}
