package pages.invoice;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/** Invoice creation / edit form. */
public class InvoiceFormPage extends BasePage {

    private final By invoiceNumberField = By.id("invoice_invoice_number");
    private final By invoiceDateField   = By.id("invoice_invoice_date");
    private final By dueDateField       = By.id("invoice_due_date");
    private final By submitBtn          = By.cssSelector("input[type='submit']");
    private final By saveBtn            = By.cssSelector("button.save");

    public InvoiceFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void fillInvoiceNumber(String num) { type(invoiceNumberField, num); }
    public void fillInvoiceDate(String date)  { type(invoiceDateField, date); }
    public void fillDueDate(String date)      { type(dueDateField, date); }

    public void save() {
        click(saveBtn);
        log.info("Invoice saved");
    }

    public void submit() {
        click(submitBtn);
        log.info("Invoice submitted");
    }
}
