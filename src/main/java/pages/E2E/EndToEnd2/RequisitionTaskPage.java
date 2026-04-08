package pages.E2E.EndToEnd2;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;

public class RequisitionTaskPage extends BasePage {

    private WebDriver driver;
	private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    private static final int SHORT_WAIT = 5;
    private static final int MEDIUM_WAIT = 10;

    // Locators
    private By notSureLetsGuideYouBtn = By.className("s-writeARequestBtn");
    private By skipToWriteReqBtn = By.className("searchWizardFooter__skip");
    private By requisitionDescriptionField = By.id("requisition_line_description");
    private By unitPrice = By.id("requisition_line_unit_price_amount");
    private By supplierSearchField = By.className("s-supplierSearch");
    private By firstSupplierOption = By.cssSelector("ul.ComboBox__results li.s-selectOption:first-child");
    private By needByDate = By.id("requisition_line_local_need_by_date");
    private By needByDateForAddLine = By.id("requisition_line_need_by_date");

    private By startDate = By.id("requisition_line_custom_field_1");
    private By submitButton = By.xpath("//button[@type='submit']");
    private By addLine = By.id("add_requisition_line_link");
    private By saveBtn = By.cssSelector(
    	    ".line_bottom_inside button.save"
    	);

    public RequisitionTaskPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(MEDIUM_WAIT));
    }

    public void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    public void startCommand() {
        click(notSureLetsGuideYouBtn);
        click(skipToWriteReqBtn);
    }

    public void enterRequisitionDescription(String description) {
        type(requisitionDescriptionField, description);
    }

    public void selectSupplier(String supplierName) {
        click(supplierSearchField);
        type(supplierSearchField, supplierName);

        // Wait a bit for results to appear
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT));
        shortWait.until(ExpectedConditions.elementToBeClickable(firstSupplierOption)).click();
    }
    public void enterUnitPrice(String price) {
    	WebElement input = driver.findElement(By.id("requisition_line_unit_price_amount"));
    	input.clear();
        type(unitPrice, price);
    }
    
    public void enterNeedByDate(String date) {

        WebElement dateField = wait.until(
                ExpectedConditions.elementToBeClickable(needByDate)
        );

        dateField.sendKeys(Keys.CONTROL + "a");
        dateField.sendKeys(Keys.DELETE);
        dateField.sendKeys(date);
        dateField.sendKeys(Keys.TAB);
    }
    
    public void enterNeedByDateForAddLine(String date) {

        WebElement dateField = wait.until(
                ExpectedConditions.elementToBeClickable(needByDateForAddLine)
        );

        dateField.sendKeys(Keys.CONTROL + "a");
        dateField.sendKeys(Keys.DELETE);
        dateField.sendKeys(date);
        dateField.sendKeys(Keys.TAB);
    }
    public void enterStartDate(String date) {

        WebElement dateField = wait.until(
                ExpectedConditions.elementToBeClickable(startDate)
        );

        dateField.sendKeys(Keys.CONTROL + "a");
        dateField.sendKeys(Keys.DELETE);
        dateField.sendKeys(date);
        dateField.sendKeys(Keys.TAB);
    }
    
    public void validateTask() {
        click(submitButton);
    }
    public void fillRequisition(String description, String supplierName, String unitPrice,  String startDate ,String date) {
        startCommand();
        enterRequisitionDescription(description);
        selectSupplier(supplierName);
        enterUnitPrice(unitPrice);
        enterStartDate(startDate);
        enterNeedByDate(date);
    }
    
    public void saveLine() {

        WebElement save = wait.until(ExpectedConditions.elementToBeClickable(saveBtn));

        ((JavascriptExecutor) driver)
            .executeScript("arguments[0].scrollIntoView({block: 'center'});", save);

        save.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.cssSelector(".editing")
        ));

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.cssSelector(".loading, .spinner, .overlay")
        ));
    }
    
    public void addLine() {
    	WebElement addLineBtn = wait.until(
                ExpectedConditions.presenceOfElementLocated(addLine)
        );

        // Scroll to center (avoid overlay issues)
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", addLineBtn);

        wait.until(ExpectedConditions.visibilityOf(addLineBtn));
        wait.until(ExpectedConditions.elementToBeClickable(addLineBtn));

        addLineBtn.click();

    }
    
    public void fillRequisitionForAddLine(String description, String supplierName, String unitPrice,  String startDate ,String date) {
        enterRequisitionDescription(description);
        selectSupplier(supplierName);
        enterUnitPrice(unitPrice);
        enterStartDate(startDate);
        enterNeedByDateForAddLine(date);
        saveLine();
    }
    
   
  
}
