package pages.invoice;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;

public class InvoiceListPage extends BasePage {

	private final By invoicesButton = By.linkText("Invoices");
	private By invoiceInboxBtn = By.xpath("//a[@href='/inbound_invoices']");
	private By advancedSearchBar = By.id("si_inbound_invoice");
	private By dropdownLocator = By.cssSelector("select[data-cond-col-select='true']");
	private By serachViaAdvanced = By.id("search_advanced_button_inbound_invoice");
	private By dateOpDropdown = By.cssSelector("select[data-key='received_at']");
	private Select dropdown;
	private By deleteInvoiceLine = By.className("s-deleteInvoiceLine");
	private By invoiceNumber = By.className("s-invoiceNumber");
	private By invoiceDate = By.id("local_invoice_date_3");
	private By pickPoBtn = By.cssSelector("button.s-pickLinesFromPo");
	private By inputField = By.xpath("//input[starts-with(@id,'remit_to_address_id_')]");
	private By searchInput = By.id("sf_order_line");
	private By searchIcon = By.id("sfBtn_order_line");
	private By resultsList = By.cssSelector("[id^='remit_to_address_id_'][id$='_results']");
	private By firstOption = By.cssSelector("[id^='remit_to_address_id_'][id$='_results'] li.-active");
    private By checkbox = By.cssSelector("input.s-invoiceLineLevelTaxation");
    private By spinner = By.cssSelector(".loading, .spinner, .overlay");
    private By calculateBtn = By.cssSelector("button.s-calculateButton");
	private By amountField = By.cssSelector("div.totalAmount div.amount.s-amount");
	private By controlTotalInput = By.cssSelector("input.s-supplierTotalAmount");
    private By submitBtn = By.xpath("//button[contains(@class,'s-submit') and .//span[text()='Submit']]");
    



	public InvoiceListPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	public void navigate() {
		click(invoicesButton);
		log.info("Navigated to Invoices");

		click(invoiceInboxBtn);
		log.info("Navigated to Invoice Inbox via href");

		click(advancedSearchBar);

		WebElement dropdownElement = wait.until(ExpectedConditions.elementToBeClickable(dropdownLocator));
		dropdown = new Select(dropdownElement);
		dropdown.selectByVisibleText("Date Received");

		WebElement dropdownEl = wait.until(ExpectedConditions.elementToBeClickable(dateOpDropdown));
		dropdown = new Select(dropdownEl);
		dropdown.selectByValue("today");

		log.info("Filter condition set to: today");

		click(serachViaAdvanced);

	}

	public void clickCreateForMatchingRow(String date) {
	    By createButton = By.xpath(
	        "//table[@id='inbound_invoice_table_tag']//tr[" +
	        ".//td[contains(@class,'supplier__name')]//a[contains(text(),'CGI FRANCE')]" +
	        " and .//td[contains(@class,'status')][contains(text(),'Ready for Manual Entry')]" +
	        " and .//td[contains(@class,'sender_email')]//a[contains(text(),'r44.aspen@gmail.com')]" +
	        " and .//td[contains(@class,'received_at')][contains(text(),'" + date + "')]" +
	        " and normalize-space(.//td[contains(@class,'invoices_created')])=''" +
	        "]//a[@data-method='post']"
	    );

	    int maxRetries = 20;
	    int attempt = 0;

	    while (attempt < maxRetries) {
	        try {
	            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
	            WebElement btn = shortWait.until(ExpectedConditions.visibilityOfElementLocated(createButton));

	            ((JavascriptExecutor) driver).executeScript(
	                "arguments[0].scrollIntoView({block:'center'});", btn
	            );
	            btn.click();
	            log.info("Create button clicked on attempt {}", attempt + 1);
	            return; 

	        } catch (Exception e) {
	            attempt++;
	            log.warn("Create button not visible (attempt {}), waiting 30s then refreshing...", attempt);
	            pause(30000);
	            driver.navigate().refresh();
	            wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
	        }
	    }

	    throw new RuntimeException("Create button not found after " + maxRetries + " attempts");
	}
	public void removePoLine() {
		jsClick(deleteInvoiceLine);
	}

	public void enterNeedByDate(String date) {

		WebElement dateField = wait.until(ExpectedConditions.elementToBeClickable(invoiceDate));

		dateField.sendKeys(Keys.CONTROL + "a");
		dateField.sendKeys(Keys.DELETE);
		dateField.sendKeys(date);
		dateField.sendKeys(Keys.TAB);
	}

	public void enterInvNumber(String invNumber) {
		click(invoiceNumber);
		type(invoiceNumber, invNumber);
	}

	public void fillInvoiceInfo(String invNumber, String date) {
		enterInvNumber(invNumber);
		enterNeedByDate(date);
		clickPickLinesFromPO();
	}

	public void clickPickLinesFromPO() {
		log.info("🔍 Clicking 'Pick lines from PO' button...");

		wait.until(ExpectedConditions
				.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner, .overlay, .modal-backdrop")));
		pause(3000);
		WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(pickPoBtn));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});",
				btn);
		try {
			btn.click();
			log.info("Standard click succeeded");
		} catch (ElementClickInterceptedException e) {
			log.warn("Standard click intercepted, trying fallbacks...");

			try {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
				log.info("JavaScript click succeeded");
			} catch (Exception e2) {
				log.warn("JS click failed, trying Actions...");
				new Actions(driver).moveToElement(btn).click().perform();
				log.info("Actions click succeeded");
			}
		}
		log.info("'Pick lines from PO' clicked");

		log.info(" Waiting for PO selection modal...");

		log.info("PO selection modal is ready");
	}

	public void searchByPONumber(String poNumber) {
		log.info("Searching for PO: {}", poNumber);

		WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchInput));

		input.clear();
		input.sendKeys(poNumber);

		WebElement icon = wait.until(ExpectedConditions.elementToBeClickable(searchIcon));
		icon.click();

		waitForCoupaSearchResults();

		log.info("✅ PO search completed for: {}", poNumber);
	}

	public void waitForCoupaSearchResults() {
		wait.until(ExpectedConditions.invisibilityOfElementLocated(
				By.cssSelector(".loading, .spinner, .overlay, .modal-backdrop, .ajax-loader")));

		wait.until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("table.data-table, div.s-lineItems, .po-results, tbody tr")));

		pause(2000);
	}

	public void addPOLineToInvoice() {
	    log.info("Clicking 'Add to invoice' for selected PO line...");

	    By addLink = By.xpath(
	        "//tr[.//td[contains(@class,'line_num') and normalize-space()='1']]" +
	        "//a[@title='Add to invoice']"
	    );

	    WebElement addBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(addLink));

	    ((JavascriptExecutor) driver).executeScript(
	        "arguments[0].scrollIntoView({block: 'center'});", addBtn);

	    wait.until(ExpectedConditions.elementToBeClickable(addBtn));

	    try {
	        addBtn.click();
	        log.info("Standard click succeeded");
	    } catch (ElementClickInterceptedException e) {
	        log.warn("Intercepted, using JS fallback...");
	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);
	    }

	    waitForLineToAttachToForm();

	    By finishBtn = By.xpath("//a[.//span[text()='Finish']]");
	    jsClick(finishBtn);

	    log.info("✅ PO line successfully added to invoice");
	}
	private void waitForLineToAttachToForm() {
		wait.until(ExpectedConditions
				.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner, .overlay, .ajax-loader")));

		wait.until(ExpectedConditions.or(
				ExpectedConditions.stalenessOf(driver.findElement(By.cssSelector("a[submit='#invoice_form']"))),
				ExpectedConditions.visibilityOfElementLocated(
						By.cssSelector("table#invoice_lines, .line-items, tbody tr.line-item")),
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".flash-notice, .alert-success"))));

		pause(2000);
	}
	
	public void selectFirstRemitToOption() {


		click(inputField);
	    wait.until(ExpectedConditions.visibilityOfElementLocated(resultsList));	    
	    click(firstOption);
	    pause(4000);
	    log.info("✅ RemitTo successfully added to invoice");
	}

	
	public void clickCheckboxSafe() {

	    wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
	    
	    WebElement cb = wait.until(ExpectedConditions.presenceOfElementLocated(checkbox));
	    ((JavascriptExecutor) driver).executeScript(
	        "arguments[0].scrollIntoView({block:'center'});", cb
	    );
	    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cb);
	    log.info("Checkbox clicked");

	    wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
	    pause(3000);
	    
	    By taxCodeToggle = By.cssSelector("div.s-taxCode div.s-comboBoxContainer span.s-toggleResults");
	    By secondOption = By.cssSelector("div.s-taxCode ul.ComboBox__results li.s-selectOption:nth-child(2)");

	    WebElement toggle = wait.until(ExpectedConditions.elementToBeClickable(taxCodeToggle));
	    ((JavascriptExecutor) driver).executeScript(
	        "arguments[0].scrollIntoView({block:'center'});", toggle
	    );
	    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", toggle);
	    log.info("Tax code dropdown opened");

	    WebElement secondItem = wait.until(ExpectedConditions.elementToBeClickable(secondOption));
	    String selectedText = secondItem.getText();
	    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", secondItem);
	    log.info("Tax code selected: {}", selectedText);

	    wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
	    pause(5000);

	    WebElement calculate = wait.until(
	        ExpectedConditions.presenceOfElementLocated(calculateBtn)
	    );
	    ((JavascriptExecutor) driver).executeScript(
	        "arguments[0].scrollIntoView({block:'center'});", calculate
	    );

	    try {
	        wait.until(ExpectedConditions.elementToBeClickable(calculate));
	        calculate.click();
	    } catch (Exception e) {
	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", calculate);
	    }
	    log.info("Calculate clicked");

	    wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
	    pause(4000);
	   
	    fillControlTotalWithCalculatedAmount();
	    
	    pause(4000);
	    try {
	        wait.until(ExpectedConditions.elementToBeClickable(calculate));
	        calculate.click();
	    } catch (Exception e) {
	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", calculate);
	    }
	    log.info("Calculate clicked");
	    pause(4000);
	    jsClick(submitBtn);
	    log.info("Invoice successfully created");
	    wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));

	}
	public void fillControlTotalWithCalculatedAmount() {

	    wait.until(driver -> {
	        String text = driver.findElement(amountField).getText(); 
	        return !text.isEmpty() && !text.equals("0.00");
	    });

	    String amount = driver.findElement(amountField).getText();
	    log.info("Captured amount: {}", amount);

	    WebElement input = wait.until(ExpectedConditions.elementToBeClickable(controlTotalInput));
	    input.clear();
	    input.sendKeys(amount);
	}
}
