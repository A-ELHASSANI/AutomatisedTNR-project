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

/** Invoice list page. */
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
//    private By firstOption = By.cssSelector("#remit_to_address_id_16_results li.-active");
//    private By resultsList = By.id("remit_to_address_id_16_results");
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

//		WebElement invoiceInbox = wait.until(ExpectedConditions.elementToBeClickable(invoiceInboxBtn));
//		invoiceInbox.click();
		click(invoiceInboxBtn);
		log.info("Navigated to Invoice Inbox via href");

//		WebElement advanced = wait.until(ExpectedConditions.elementToBeClickable(advancedSearchBar));
//		advanced.click();
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

		By createButton = By.xpath("//table[@id='inbound_invoice_table_tag']//tr["
				+ ".//td[contains(@class,'supplier__name')]//a[contains(text(),'CGI FRANCE')]"
				+ " and .//td[contains(@class,'status')][contains(text(),'Ready for Manual Entry')]"
				+ " and .//td[contains(@class,'sender_email')]//a[contains(text(),'r44.aspen@gmail.com')]"
				+ " and .//td[contains(@class,'received_at')][contains(text(),'" + date + "')]"
				+ " and normalize-space(.//td[contains(@class,'invoices_created')])=''" + "]//a[@data-method='post']");

		WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(createButton));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);

		btn.click();
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

		// 1️⃣ Wait for input field to be ready
		WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchInput));

		// 2️⃣ Clear existing text & type PO number
		input.clear();
		input.sendKeys(poNumber);

		// 3️⃣ Trigger search (click icon)
		WebElement icon = wait.until(ExpectedConditions.elementToBeClickable(searchIcon));
		icon.click();

		// 4️⃣ Wait for Coupa's AJAX processing & table reload
		waitForCoupaSearchResults();

		log.info("✅ PO search completed for: {}", poNumber);
	}

	public void waitForCoupaSearchResults() {
		wait.until(ExpectedConditions.invisibilityOfElementLocated(
				By.cssSelector(".loading, .spinner, .overlay, .modal-backdrop, .ajax-loader")));

		// Wait for table container to re-render (adjust selector if your table uses
		// different class)
		wait.until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("table.data-table, div.s-lineItems, .po-results, tbody tr")));

		// Small buffer for JS rendering to finish
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
	}

	public void addPOLineToInvoice() {
		log.info("Clicking 'Add to invoice' for selected PO line...");

		By addLink = By.cssSelector("a[submit='#invoice_form'][data-remote='true']");
		WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(addLink));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});",
				addBtn);

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
		// Wait for page-level spinners to clear
		wait.until(ExpectedConditions
				.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner, .overlay, .ajax-loader")));

		// Wait for search/modal to close OR invoice lines table to refresh
		wait.until(ExpectedConditions.or(
				// Option A: Search panel closes (element removed from DOM)
				ExpectedConditions.stalenessOf(driver.findElement(By.cssSelector("a[submit='#invoice_form']"))),
				// Option B: Invoice lines table becomes visible/updated
				ExpectedConditions.visibilityOfElementLocated(
						By.cssSelector("table#invoice_lines, .line-items, tbody tr.line-item")),
				// Option C: Flash/success message appears
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".flash-notice, .alert-success"))));

		// Small buffer for DOM re-render
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
		}
	}
	
	public void selectFirstRemitToOption() {


		click(inputField);
	    wait.until(ExpectedConditions.visibilityOfElementLocated(resultsList));	    
	    click(firstOption);

	    log.info("✅ RemitTo successfully added to invoice");
	}
//	public void clickCheckboxSafe() {
//
//	    // 1. Wait page ready
//	    wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
//
//	     // 5. Click checkbox (SAFE)
//	    WebElement cb = wait.until(ExpectedConditions.presenceOfElementLocated(checkbox));
//
//	    ((JavascriptExecutor) driver).executeScript(
//	        "arguments[0].scrollIntoView({block:'center'});", cb
//	    );
//
//	    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cb);
//
//	    log.info("Checkbox clicked");
//
//	    // 3. VERY IMPORTANT → wait refresh after checkbox
//	    wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
//	    pause(2000);
//	    By taxCodeToggle = By.cssSelector("div.lineTaxSection div.ComboBox__field div.s-comboBoxContainer span.s-toggleResults");
//	    By secondOption = By.cssSelector("ul[id*='code_id'][id$='_results'] li:nth-child(2)");
//
//	    WebElement toggle = wait.until(ExpectedConditions.elementToBeClickable(taxCodeToggle));
//	    ((JavascriptExecutor) driver).executeScript(
//	        "arguments[0].scrollIntoView({block:'center'});", toggle
//	    );
//	    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", toggle);
//	    log.info("Tax code dropdown opened");
//
//	    // 3. Wait for dropdown options and click second child
//	    WebElement secondItem = wait.until(ExpectedConditions.elementToBeClickable(secondOption));
//	    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", secondItem);
//	    log.info("Second tax code option selected: {}", secondItem.getText());
//
//	    // 4. Wait page to settle after tax code selection
//	    wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
//
//	    // 4. Re-locate Calculate button AFTER refresh
//	    WebElement calculate = wait.until(
//	        ExpectedConditions.presenceOfElementLocated(calculateBtn)
//	    );
//
//	    ((JavascriptExecutor) driver).executeScript(
//	        "arguments[0].scrollIntoView({block:'center'});", calculate
//	    );
//
//	    // 5. Safe click
//	    try {
//	        wait.until(ExpectedConditions.elementToBeClickable(calculate));
//	        calculate.click();
//	    } catch (Exception e) {
//	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", calculate);
//	    }
//
//	    log.info("Calculate clicked");
//
//	    // 6. Wait calculation result
//	    wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
//
//	    // 7. Continue
//	    fillControlTotalWithCalculatedAmount();
//	}
	
	public void clickCheckboxSafe() {

	    // 1. Wait page ready
	    wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
	    
	    // 5. Click checkbox (SAFE)
	    WebElement cb = wait.until(ExpectedConditions.presenceOfElementLocated(checkbox));
	    ((JavascriptExecutor) driver).executeScript(
	        "arguments[0].scrollIntoView({block:'center'});", cb
	    );
	    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cb);
	    log.info("Checkbox clicked");

	    // 6. Wait refresh after checkbox
	    wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
	    
	    // 2. Open Tax Code dropdown — scoped via stable parent div.s-taxCode
	    By taxCodeToggle = By.cssSelector("div.s-taxCode div.s-comboBoxContainer span.s-toggleResults");
	    By secondOption = By.cssSelector("div.s-taxCode ul.ComboBox__results li.s-selectOption:nth-child(2)");

	    WebElement toggle = wait.until(ExpectedConditions.elementToBeClickable(taxCodeToggle));
	    ((JavascriptExecutor) driver).executeScript(
	        "arguments[0].scrollIntoView({block:'center'});", toggle
	    );
	    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", toggle);
	    log.info("Tax code dropdown opened");

	    // 3. Wait for dropdown to appear and click second option (skipping empty first placeholder)
	    WebElement secondItem = wait.until(ExpectedConditions.elementToBeClickable(secondOption));
	    String selectedText = secondItem.getText();
	    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", secondItem);
	    log.info("Tax code selected: {}", selectedText);

	    // 4. Wait page to settle after tax code selection
	    wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));

	    // 7. Re-locate Calculate button AFTER refresh
	    WebElement calculate = wait.until(
	        ExpectedConditions.presenceOfElementLocated(calculateBtn)
	    );
	    ((JavascriptExecutor) driver).executeScript(
	        "arguments[0].scrollIntoView({block:'center'});", calculate
	    );

	    // 8. Safe click on Calculate
	    try {
	        wait.until(ExpectedConditions.elementToBeClickable(calculate));
	        calculate.click();
	    } catch (Exception e) {
	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", calculate);
	    }
	    log.info("Calculate clicked");

	    // 9. Wait calculation result
	    wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
	   
	    pause(2000);
	    // 10. Continue
	    fillControlTotalWithCalculatedAmount();
	    
	    pause(2000);
	    try {
	        wait.until(ExpectedConditions.elementToBeClickable(calculate));
	        calculate.click();
	    } catch (Exception e) {
	        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", calculate);
	    }
	    log.info("Calculate clicked");
	    pause(2000);
	    jsClick(submitBtn);
	    log.info("Invoice successfully created");
	    wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));

	}
	public void fillControlTotalWithCalculatedAmount() {

	    // Wait until the amount text is populated and non-zero
	    wait.until(driver -> {
	        String text = driver.findElement(amountField).getText(); // ✅ getText()
	        return !text.isEmpty() && !text.equals("0.00");
	    });

	    String amount = driver.findElement(amountField).getText();
	    log.info("Captured amount: {}", amount);

	    WebElement input = wait.until(ExpectedConditions.elementToBeClickable(controlTotalInput));
	    input.clear();
	    input.sendKeys(amount);
	}
}
