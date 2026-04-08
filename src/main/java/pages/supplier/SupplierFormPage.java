package pages.supplier;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/** Create / edit supplier form. */
public class SupplierFormPage extends BasePage {

    private final By nameField      = By.id("supplier_name");
    private final By statusField    = By.id("supplier_status");
    private final By saveBtn        = By.cssSelector("input[type='submit']");
    
    public SupplierFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void fillName(String name) { type(nameField, name); }

    public void save() {
        click(saveBtn);
        log.info("Supplier form saved");
    }
}
