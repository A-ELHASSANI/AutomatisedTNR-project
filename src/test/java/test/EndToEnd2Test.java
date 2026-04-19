package test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pages.admin.UserAdminPage;
import pages.goodreceipt.GoodReceiptPage;
import pages.invoice.InvoiceListPage;
import pages.requisition.RequisitionBasketPage;
import pages.requisition.RequisitionFormPage;
import utils.DateUtils;
import utils.EmailSender;

import base.BaseTest;

public class EndToEnd2Test extends BaseTest {

	// ── Page objects ──────────────────────────────────────────────────────────
	private RequisitionFormPage requisitionForm;
	private RequisitionBasketPage requisitionBasket;
	private UserAdminPage userAdmin;
	private GoodReceiptPage goodReceipt;
	private InvoiceListPage invoiceList;
	private EmailSender emailSender;

	// ── Test data read from config ────────────────────────────────────────────
	private String description;
	private String description2;
	private String supplier;
	private String commodity;
	private String unitPrice;
	private String quantity;
	private String title;
	private String comment;
	private String attachmentPath;
	private String approver1;
	private String approver2;
	private String approver3;
	private String invoiceUser;
	private String invoiceNumber;
	private String requesterDisplayName;

	/** Captured after approval — used in invoice step. */
	private String poNumber;

	// ── Setup ─────────────────────────────────────────────────────────────────

	@BeforeClass
	public void setUp() throws InterruptedException {
		super.initSuite();

		// Read all test data from config.properties 
		description = config.get("e2e.requisition.description");
		description2 = config.get("e2e.requisition.description2");
		supplier = config.get("e2e.requisition.supplier");
		commodity = config.get("e2e.requisition.commodity");
		unitPrice = config.get("e2e.requisition.unitprice");
		quantity = config.get("e2e.requisition.quantity");
		title = config.get("e2e.requisition.title");
		comment = config.get("e2e.requisition.comment");
		attachmentPath = config.get("e2e.attachment.path");
		approver1 = config.get("e2e.approver1");
		approver2 = config.get("e2e.approver2");
		approver3 = config.get("e2e.approver3");
		invoiceUser = config.get("e2e.invoice.user");
		invoiceNumber = config.get("e2e.invoice.number");
		requesterDisplayName = config.get("app.username.display");

		// Browser setup + login
		loginAsDefault();

		// Instantiate page objects once the driver is ready
		requisitionForm = new RequisitionFormPage(driver);
		requisitionBasket = new RequisitionBasketPage(driver);
		userAdmin = new UserAdminPage(driver);
		goodReceipt = new GoodReceiptPage(driver, requesterDisplayName);
		invoiceList = new InvoiceListPage(driver);
		emailSender = new EmailSender();
	}

	// ── Step 1 – Fill requisition ─────────────────────────────────────────────

	@Test(description = "TC-E2E2-01: Fill requisition and add to cart")
	public void step01_fillRequisition() {
		startTest("TC-E2E2-01: Fill requisition");

		requisitionForm.fillQuantityLine(description, supplier, commodity, unitPrice, quantity,
				DateUtils.getCurrentDate(), DateUtils.getFutureDate());
		requisitionForm.submitForm();
		pause(4000);

		testReport.pass("Requisition line filled and added to cart");
		log.info("Step 1 complete");
	}

	// ── Step 2 – Submit for approval ──────────────────────────────────────────

	@Test(description = "TC-E2E2-02: Submit requisition for approval", dependsOnMethods = "step01_fillRequisition")
	public void step02_submitForApproval() {
		startTest("TC-E2E2-02: Submit for approval");

		requisitionBasket.openBasket();
		pause(2000);
		requisitionBasket.addAttachment(attachmentPath);
		pause(2000);
		requisitionBasket.addComment(comment);
		pause(2000);
		requisitionBasket.enterTitle(title);
		pause(4000);

		// Add second line
		requisitionForm.fillAddLine(description2, supplier, commodity, unitPrice, quantity, DateUtils.getCurrentDate(),
				DateUtils.getFutureDate());
		pause(4000);

		// Fill any missing billing accounts then submit
		requisitionBasket.fillMissingBillingAccounts();
		pause(3000);
		requisitionBasket.submitForApproval();
		pause(15000);

		testReport.pass("Requisition submitted for approval");
		log.info("Step 2 complete");
	}

	// ── Step 3 – Approve (3 approvers) ───────────────────────────────────────

	@Test(description = "TC-E2E2-03: Approver 1 approves", dependsOnMethods = "step02_submitForApproval")
	public void step03_approver1() {
		startTest("TC-E2E2-03: " + approver1 + " approves");

		userAdmin.actAsUser(approver1);
		pause(2000);
		userAdmin.approveRequisition(requesterDisplayName);
		pause(2000);
		userAdmin.returnToAdmin();

		testReport.pass(approver1 + " approved successfully");
		log.info("Step 3 complete — approver: {}", approver1);
	}

	@Test(description = "TC-E2E2-04: Approver 2 approves", dependsOnMethods = "step03_approver1")
	public void step04_approver2() {
		startTest("TC-E2E2-04: " + approver2 + " approves");

		userAdmin.actAsUser(approver2);
		pause(2000);
		userAdmin.approveRequisition(requesterDisplayName);
		pause(2000);
		userAdmin.returnToAdmin();

		testReport.pass(approver2 + " approved successfully");
		log.info("Step 4 complete — approver: {}", approver2);
	}

	@Test(description = "TC-E2E2-05: Approver 3 approves", dependsOnMethods = "step04_approver2")
	public void step05_approver3() {
		startTest("TC-E2E2-05: " + approver3 + " approves");

		userAdmin.actAsUser(approver3);
		pause(2000);
		userAdmin.approveRequisition(requesterDisplayName);
		pause(2000);
		userAdmin.returnToAdmin();
		pause(3000);

		testReport.pass(approver3 + " approved — requisition fully approved");
		log.info("Step 5 complete — approver: {}", approver3);
	}

	// ── Step 4 – Good Receipt ─────────────────────────────────────────────────

	@Test(description = "TC-E2E2-06: Record good receipt", dependsOnMethods = "step05_approver3")
	public void step06_goodReceipt() {
		startTest("TC-E2E2-06: Good receipt");

		goodReceipt.receiveFirstLine(DateUtils.getDatePlusTwo());
		pause(2000);

		testReport.pass("Good receipt recorded successfully");

		log.info("Step 6 complete");
	}

	// ── Step 5 – Invoice ──────────────────────────────────────────────────────

	@Test(description = "TC-E2E2-07: Send invoice email and process invoice"
			, dependsOnMethods = "step06_goodReceipt")
	public void step07_invoice() throws Exception {
		startTest("TC-E2E2-07: Invoice");
		// Capture PO number
		poNumber = userAdmin.getPOnumber();
		log.info("PO Number: {}", poNumber);

		// Send invoice email with the PO number
		emailSender.sendInvoiceEmail(poNumber);
		pause(20000);
		userAdmin.actAsUser(invoiceUser);
		pause(10000);

		// Find and confirm the invoice row
		invoiceList.navigate();
		invoiceList.clickCreateForMatchingRow(DateUtils.getCurrentDate());
		pause(6000);
		// Navigate to the new invoice (Coupa creates it after the email)
//        openUrl(config.getBaseUrl() + "/invoices/xxxxx/edit");

		invoiceList.removePoLine();
		invoiceList.fillInvoiceInfo(invoiceNumber, DateUtils.getCurrentDate());
		invoiceList.searchByPONumber(poNumber);
		invoiceList.addPOLineToInvoice();
		invoiceList.selectFirstRemitToOption();
		invoiceList.clickCheckboxSafe();
		pause(10000);

		userAdmin.returnToAdmin();
		pause(3000);

		testReport.pass("Invoice created and linked to PO: " + poNumber);
		log.info("Step 7 complete — E2E 2 PASSED");
	}
}
