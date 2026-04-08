package pages.requisition;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/** Requisition creation / edit form. */
public class RequisitionFormPage extends BasePage {

    private final By descriptionField  = By.id("requisition_line_description");
    private final By unitPriceField    = By.id("requisition_line_unit_price_amount");
    private final By supplierSearch    = By.className("s-supplierSearch");
    private final By firstSupplier     = By.cssSelector("ul.ComboBox__results li.s-selectOption:first-child");
    private final By needByDate        = By.id("requisition_line_local_need_by_date");
    private final By startDateField    = By.id("requisition_line_custom_field_1");
    private final By addLineBtn        = By.id("add_requisition_line_link");
    private final By saveLinesBtn      = By.cssSelector(".line_bottom_inside button.save");
    private final By submitBtn         = By.xpath("//button[@type='submit']");

    public RequisitionFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void fillDescription(String description) {
        type(descriptionField, description);
    }

    public void fillUnitPrice(String price) {
        type(unitPriceField, price);
    }

    public void selectFirstSupplier(String partialName) {
        WebElement field = waitForClickable(supplierSearch);
        field.sendKeys(partialName);
        pause(1500);
        click(firstSupplier);
        log.info("Selected first supplier matching: {}", partialName);
    }

    public void fillNeedByDate(String date) {
        type(needByDate, date);
        waitForClickable(needByDate).sendKeys(Keys.TAB);
    }

    public void fillStartDate(String date) {
        type(startDateField, date);
        waitForClickable(startDateField).sendKeys(Keys.TAB);
    }

    public void saveLines() {
        click(saveLinesBtn);
        pause(1500);
    }

    public void addLine() {
        click(addLineBtn);
        pause(1500);
        log.info("Added new requisition line");
    }

    public void submit() {
        click(submitBtn);
        log.info("Requisition submitted");
    }
}
