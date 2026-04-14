package pages.requisition;

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

/** Requisition creation / edit form. */
public class RequisitionFormPage extends BasePage {
//
//    private final By descriptionField  = By.id("requisition_line_description");
//    private final By unitPriceField    = By.id("requisition_line_unit_price_amount");
//    private final By supplierSearch    = By.className("s-supplierSearch");
//    private final By firstSupplier     = By.cssSelector("ul.ComboBox__results li.s-selectOption:first-child");
//    private final By needByDate        = By.id("requisition_line_local_need_by_date");
//    private final By startDateField    = By.id("requisition_line_custom_field_1");
//    private final By addLineBtn        = By.id("add_requisition_line_link");
//    private final By saveLinesBtn      = By.cssSelector(".line_bottom_inside button.save");
//    private final By submitBtn         = By.xpath("//button[@type='submit']");
//
//    public RequisitionFormPage(WebDriver driver) {
//        this.driver = driver;
//        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
//    }
//
//    public void fillDescription(String description) {
//        type(descriptionField, description);
//    }
//
//    public void fillUnitPrice(String price) {
//        type(unitPriceField, price);
//    }
//
//    public void selectFirstSupplier(String partialName) {
//        WebElement field = waitForClickable(supplierSearch);
//        field.sendKeys(partialName);
//        pause(1500);
//        click(firstSupplier);
//        log.info("Selected first supplier matching: {}", partialName);
//    }
//
//    public void fillNeedByDate(String date) {
//        type(needByDate, date);
//        waitForClickable(needByDate).sendKeys(Keys.TAB);
//    }
//
//    public void fillStartDate(String date) {
//        type(startDateField, date);
//        waitForClickable(startDateField).sendKeys(Keys.TAB);
//    }
//
//    public void saveLines() {
//        click(saveLinesBtn);
//        pause(1500);
//    }
//
//    public void addLine() {
//        click(addLineBtn);
//        pause(1500);
//        log.info("Added new requisition line");
//    }
//
//    public void submit() {
//        click(submitBtn);
//        log.info("Requisition submitted");
//    }
	private static final int SHORT_WAIT = 5;
	private static final int MEDIUM_WAIT = 10;

	// ── Form entry locators ───────────────────────────────────────────────────
	private final By notSureLetsGuideYouBtn = By.className("s-writeARequestBtn");
	private final By skipToWriteReqBtn = By.className("searchWizardFooter__skip");
	private final By descriptionField = By.id("requisition_line_description");
	private final By unitPriceField = By.id("requisition_line_unit_price_amount");
	private final By supplierSearchField = By.className("s-supplierSearch");
	private final By firstSupplierOption = By.cssSelector("ul.ComboBox__results li.s-selectOption:first-child");
	private final By needByDateField = By.id("requisition_line_local_need_by_date");
	private final By needByDateAddLineField = By.id("requisition_line_need_by_date");
	private final By startDateField = By.id("requisition_line_custom_field_1");
	private final By quantityField = By.id("requisition_line_quantity");
	private final By supplierPartNumberField = By.id("requisition_line_source_part_num");
	private final By commoditySearchField = By.id("requisition_line_commodity_id_input");
	private final By firstCommodityOption = By.cssSelector("ul.ui-autocomplete li.ui-menu-item a");
	private final By addFileAttachmentBtn = By.cssSelector("a.file-attachment");
	private final By fileInput = By.xpath("//input[@type='file']");
	private final By addLineLink = By.id("add_requisition_line_link");
	private final By saveLineBtn = By.cssSelector(".line_bottom_inside button.save");
	private final By submitButton = By.xpath("//button[@type='submit']");

	// ── Line-type radio buttons ───────────────────────────────────────────────
	private final By amountRadioBtn = By.id("requisition_line_line_type_requisitionamountline");
	private final By quantityRadioBtn = By.id("requisition_line_line_type_requisitionquantityline");

	// ── Contract backing ──────────────────────────────────────────────────────
	private final By spinner = By.cssSelector(".loading, .spinner, .overlay");
	private final By backingDropdown = By.id("requisition_line_backing");
	private final By contractSelector = By.id("requisition_line_contract_selector");
	private final By contractDropdown = By.cssSelector("select.requisition_line_contract_id");

	public RequisitionFormPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(MEDIUM_WAIT));
	}

	// ── Navigation ────────────────────────────────────────────────────────────

	/** Clicks "Not sure?" then "Skip to write a request" to open the blank form. */
	public void startNewRequest() {
		click(notSureLetsGuideYouBtn);
		click(skipToWriteReqBtn);
	}

	// ── Line-type selection ───────────────────────────────────────────────────

	public void selectQuantityLineType() {
		WebElement radio = wait.until(ExpectedConditions.presenceOfElementLocated(quantityRadioBtn));
		if (!radio.isSelected()) {
			wait.until(ExpectedConditions.elementToBeClickable(radio)).click();
		}
	}

	public void selectAmountLineType() {
		WebElement radio = wait.until(ExpectedConditions.presenceOfElementLocated(amountRadioBtn));
		if (!radio.isSelected()) {
			wait.until(ExpectedConditions.elementToBeClickable(radio)).click();
		}
	}

	// ── Field entry helpers ───────────────────────────────────────────────────

	public void enterDescription(String description) {
		type(descriptionField, description);
	}

	public void selectSupplier(String supplierName) {
		click(supplierSearchField);
		type(supplierSearchField, supplierName);
		new WebDriverWait(driver, Duration.ofSeconds(SHORT_WAIT))
				.until(ExpectedConditions.elementToBeClickable(firstSupplierOption)).click();
		log.info("Supplier selected: {}", supplierName);
	}

	public void enterCommodity(String commodityCode) {
		type(commoditySearchField, commodityCode);
		wait.until(ExpectedConditions.elementToBeClickable(firstCommodityOption)).click();
		log.info("Commodity selected: {}", commodityCode);
	}

	public void enterUnitPrice(String price) {
		WebElement input = driver.findElement(unitPriceField);
		input.clear();
		input.sendKeys(price);
	}

	public void enterQuantity(String quantity) {
		type(quantityField, quantity);
	}

	public void enterSupplierPartNumber(String partNumber) {
		type(supplierPartNumberField, partNumber);
	}

	private void clearAndSendDate(By locator, String date) {
		WebElement field = wait.until(ExpectedConditions.elementToBeClickable(locator));
		field.sendKeys(Keys.CONTROL + "a");
		field.sendKeys(Keys.DELETE);
		field.sendKeys(date);
		field.sendKeys(Keys.TAB);
	}

	public void enterNeedByDate(String date) {
		clearAndSendDate(needByDateField, date);
	}

	public void enterNeedByDateForAddLine(String date) {
		clearAndSendDate(needByDateAddLineField, date);
	}

	public void enterStartDate(String date) {
		clearAndSendDate(startDateField, date);
	}

	// ── Contract backing ──────────────────────────────────────────────────────

	/**
	 * Sets the Backing Document to "Contract" and selects the first available
	 * contract (index 1, skipping the blank placeholder at index 0).
	 */
	public void selectContractBacking() {
		wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));

		WebElement backing = wait.until(ExpectedConditions.elementToBeClickable(backingDropdown));
		new Select(backing).selectByValue("contract");
		log.info("Backing set to: Contract");

		wait.until(ExpectedConditions.visibilityOfElementLocated(contractSelector));

		WebElement contractEl = wait.until(ExpectedConditions.elementToBeClickable(contractDropdown));
		Select contractSelect = new Select(contractEl);
		contractSelect.selectByIndex(1);
		log.info("Contract chosen: {}", contractSelect.getFirstSelectedOption().getText());

		wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
	}

	// ── Attachment ────────────────────────────────────────────────────────────

	public void addAttachment(String filePath) {
		wait.until(ExpectedConditions.elementToBeClickable(addFileAttachmentBtn)).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(fileInput)).sendKeys(filePath);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner, .uploading")));
		log.info("Attachment added: {}", filePath);
	}

	// ── Multi-line ────────────────────────────────────────────────────────────

	public void clickAddLine() {
		WebElement link = wait.until(ExpectedConditions.elementToBeClickable(addLineLink));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", link);
		link.click();
		pause(1000);
	}

	public void saveCurrentLine() {
		WebElement save = wait.until(ExpectedConditions.elementToBeClickable(saveLineBtn));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", save);
		save.click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".editing")));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
	}

	public void selectSMDL() {

	}
	// ── Submit ────────────────────────────────────────────────────────────────

	public void submitForm() {
		click(submitButton);
		log.info("Requisition form submitted");
	}

	// ── Compound fill methods ─────────────────────────────────────────────────

	/**
	 * Fills a standard Quantity-type requisition line (E2E 2 first line).
	 */
	public void fillQuantityLine(String description, String supplier, String commodity, String unitPrice,
			String quantity, String startDate, String needByDate) {
		startNewRequest();
		selectQuantityLineType();
		enterDescription(description);
		selectSupplier(supplier);
		enterCommodity(commodity);
		enterUnitPrice(unitPrice);
		enterQuantity(quantity);
		enterStartDate(startDate);
		enterNeedByDate(needByDate);
	}

	/**
	 * Fills a second (add-line) Quantity-type requisition line after the basket
	 * page.
	 */
	public void fillAddLine(String description, String supplier, String commodity, String unitPrice, String quantity,
			String startDate, String needByDate) {
		clickAddLine();
		selectQuantityLineType();
		enterDescription(description);
		selectSupplier(supplier);
		enterCommodity(commodity);
		enterUnitPrice(unitPrice);
		enterQuantity(quantity);
		enterStartDate(startDate);
		enterNeedByDateForAddLine(needByDate);
		saveCurrentLine();
	}

	public void fillAddLineWithContract(String description, String supplier, String commodity, String unitPrice,
			String quantity, String startDate, String needByDate) {
		clickAddLine();
		selectQuantityLineType();
		enterDescription(description);
		selectSupplier(supplier);
		enterCommodity(commodity);
		enterUnitPrice(unitPrice);
		enterQuantity(quantity);
		enterStartDate(startDate);
		enterNeedByDateForAddLine(needByDate);
		selectContractBacking();
		saveCurrentLine();
	}

	/**
	 * Fills an Amount-type requisition line with contract backing (E2E 1).
	 */
	public void fillAmountLineWithContract(String description, String supplier, String commodity, String unitPrice,
			String partNumber, String startDate, String needByDate) {
		startNewRequest();
		selectAmountLineType();
		enterDescription(description);
		selectSupplier(supplier);
		enterCommodity(commodity);
		selectContractBacking();
		enterUnitPrice(unitPrice);
		enterSupplierPartNumber(partNumber);
		enterStartDate(startDate);
		enterNeedByDate(needByDate);
	}

	/**
	 * Fills a standard Quantity-type requisition line (E2E 2 first line).
	 */
	public void fillFullForm(String description, String supplier, String commodity, String unitPrice, String quantity,
			String startDate, String needByDate, String file) {
		startNewRequest();
		selectQuantityLineType();
		enterDescription(description);
		selectSupplier(supplier);
		enterCommodity(commodity);
		enterUnitPrice(unitPrice);
		enterQuantity(quantity);
		enterStartDate(startDate);
		enterNeedByDate(needByDate);
		addAttachment(file);
		selectSMDL();
	}
}
