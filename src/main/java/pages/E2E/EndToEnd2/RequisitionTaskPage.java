package pages.E2E.EndToEnd2;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;

public class RequisitionTaskPage extends BasePage {

//    private WebDriver driver;
//	private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
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
    private By quantityField = By.id("requisition_line_quantity");
    private By amountRadioBtn = By.id("requisition_line_line_type_requisitionamountline");
    private By quantityRadioBtn = By.id("requisition_line_line_type_requisitionquantityline");
    
    private By commodityResults = By.cssSelector("ul.ui-autocomplete");
    private By firstCommodityOption = By.cssSelector("ul.ui-autocomplete li.ui-menu-item a");
    private By commoditySearchField = By.id("requisition_line_commodity_id_input");
    private By addFileAttachmentBtn = By.cssSelector("a.file-attachment");
	private By fileInput = By.xpath("//input[@type='file']");
	private By supplierPartNumber = By.id("requisition_line_source_part_num");
	private By spinner = By.cssSelector(".loading, .spinner, .overlay");
	
    public RequisitionTaskPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(MEDIUM_WAIT));
    }

    public void startCommand() {
        click(notSureLetsGuideYouBtn);
        click(skipToWriteReqBtn);
    }
    public void selectAmountIfNotSelected() {

        WebElement amount = wait.until(
            ExpectedConditions.presenceOfElementLocated(amountRadioBtn)
        );

        if (!amount.isSelected()) {
            wait.until(ExpectedConditions.elementToBeClickable(amount)).click();
        }
    }
    
    public void selectQuantityIfNotSelected() {

        WebElement amount = wait.until(
            ExpectedConditions.presenceOfElementLocated(quantityRadioBtn)
        );

        if (!amount.isSelected()) {
            wait.until(ExpectedConditions.elementToBeClickable(amount)).click();
        }
    }
    
    public boolean isQuantitySelected() {
        WebElement amount = driver.findElement(quantityRadioBtn);
        return amount.isSelected();
    }
    public boolean isAmountSelected() {
        WebElement amount = driver.findElement(amountRadioBtn);
        return amount.isSelected();
    }

    public void enterRequisitionDescription(String description) {
        type(requisitionDescriptionField, description);
    }

    public void selectSupplier(String supplierName) {
        click(supplierSearchField);
        type(supplierSearchField, supplierName);

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
    public void fillRequisition(String description , String supplierName, String commodity , String unitPrice, String quantity ,  String startDate ,String date) {
        startCommand();
        selectQuantityIfNotSelected();
        enterRequisitionDescription(description);
        selectSupplier(supplierName);
        enterCommodityField(commodity);
        enterUnitPrice(unitPrice);
        enterQuantityOfItems(quantity);
        enterStartDate(startDate);
        enterNeedByDate(date);
    }
    
    public void fillRequisitionForE2E1(String description , String supplierName, String commodity , String unitPrice, String suppPartNum ,  String startDate ,String date) {
        startCommand();
        selectAmountIfNotSelected();
        enterRequisitionDescription(description);
        selectSupplier(supplierName);
        enterCommodityField(commodity);
        selectContractBacking();
        enterUnitPrice(unitPrice);
        enterSupplierPartNumber(suppPartNum);
        enterStartDate(startDate);
        enterNeedByDate(date);
    }
    
    public void enterSupplierPartNumber(String suppPartNum) {
    	type(supplierPartNumber, suppPartNum); 
    }
    
    public void addAttachment(String filePath) {
		wait.until(ExpectedConditions.elementToBeClickable(addFileAttachmentBtn)).click();
		;
		wait.until(ExpectedConditions.presenceOfElementLocated(fileInput)).sendKeys(filePath);
		;

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner, .uploading")));
	}
    
    public void selectContractBacking() {

        // 1. Wait page ready
        wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));

        // 2. Select "Contract" from Backing Document dropdown
        By backingDropdown = By.id("requisition_line_backing");
        By contractSelector = By.id("requisition_line_contract_selector");
        By contractDropdown = By.cssSelector("select.requisition_line_contract_id");

        WebElement backingSelect = wait.until(ExpectedConditions.elementToBeClickable(backingDropdown));
        new Select(backingSelect).selectByValue("contract");
        log.info("Backing document set to: Contract");

        // 3. Wait until contract field becomes visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(contractSelector));
        log.info("Contract selector is now visible");

        // 4. Select second option "CGI FRANCE Régie Informatique 2017 (Published)"
        WebElement contractSelect = wait.until(ExpectedConditions.elementToBeClickable(contractDropdown));
        Select contractSelectObj = new Select(contractSelect);
        contractSelectObj.selectByIndex(1); // index 0 = empty option, index 1 = CGI FRANCE
        log.info("Contract selected: {}", contractSelectObj.getFirstSelectedOption().getText());

        // 5. Wait page to settle
        wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
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
    
    public void enterCommodityField(String commodity) {
    	 WebElement input = wait.until(
    		        ExpectedConditions.elementToBeClickable(commoditySearchField)
    		    );

    		    input.click();
    		    input.clear();
    		    input.sendKeys(commodity);

    		    wait.until(ExpectedConditions.visibilityOfElementLocated(commodityResults));
    		    WebElement firstOption = wait.until(
    		        ExpectedConditions.elementToBeClickable(firstCommodityOption)
    		    );

    		    firstOption.click();
    }
    public void enterQuantityOfItems(String quantity) {
    	click(quantityField);
        type(quantityField, quantity);

    }
    public void fillRequisitionForAddLine(String description ,String supplierName, String commodity,  String unitPrice, String quantity, String startDate ,String date) {
        selectQuantityIfNotSelected();
    	enterRequisitionDescription(description);
        selectSupplier(supplierName);
        enterCommodityField(commodity);
        enterUnitPrice(unitPrice);
        enterQuantityOfItems(quantity);
        enterStartDate(startDate);
        enterNeedByDateForAddLine(date);
        saveLine();
    }
    
    public void fillRequisitionForAddLineForE2E1(String description ,String supplierName, String commodity,  String unitPrice, String quantity, String startDate ,String date) {
        selectAmountIfNotSelected();
    	enterRequisitionDescription(description);
        selectSupplier(supplierName);
        enterCommodityField(commodity);
        enterUnitPrice(unitPrice);
        enterQuantityOfItems(quantity);
        enterStartDate(startDate);
        enterNeedByDateForAddLine(date);
        saveLine();
    }
    
   
  
}
