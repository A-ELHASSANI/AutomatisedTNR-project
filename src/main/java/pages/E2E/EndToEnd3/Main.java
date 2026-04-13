package pages.E2E.EndToEnd3;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import base.BaseTest;
import pages.E2E.EndToEnd2.ReportManager;
import pages.E2E.EndToEnd2.RequisitionTablePage;
import pages.E2E.EndToEnd2.RequisitionTaskPage;
import pages.E2E.EndToEnd2.SubmitRequisitionForApproval;
import pages.E2E.EndToEnd2.UserAdministrationPage;
import pages.common.LoginPage;
import pages.invoice.InvoiceListPage;
import utils.DateUtils;
import utils.EmailSender;

public class Main extends BaseTest {

	public static void main(String[] args) {
		ExtentReports extent = ReportManager.getReportInstance();
		ExtentTest testReport = extent.createTest("TC - E2E 1");

		Main test = new Main();
		EmailSender email = new EmailSender();

		try {
			test.setup();
			test.openUrl("https://accorhotels-test.coupahost.com/sessions/new");

			testReport.pass("Browser opened successfully");
			LoginPage loginPage = new LoginPage(test.driver);
			loginPage.login("TEMP-AELHA", "Clavier@123");

			RequisitionTaskPage taskPage = new RequisitionTaskPage(test.driver);
			taskPage.fillRequisitionForE2E1("Requests using CGI france", "CGI FRANCE", "N020101", "25", "2",
					DateUtils.getCurrentDate(), DateUtils.getFutureDate());
			taskPage.addAttachment("C:\\Users\\anas.elhassani\\Downloads\\TEST.pdf");
			taskPage.validateTask();
			Thread.sleep(4000);
			testReport.pass("Requisition filled and added to card successfully");
			SubmitRequisitionForApproval submitReq = new SubmitRequisitionForApproval(test.driver);
			submitReq.viewBasket();
			Thread.sleep(2000);

			submitReq.addAttachment("C:\\Users\\anas.elhassani\\Downloads\\TEST.pdf");
			Thread.sleep(2000);

			submitReq.addComment("test");
			Thread.sleep(2000);
			submitReq.typeTitle("tested title");
			Thread.sleep(4000);
			taskPage.addLine();
			taskPage.fillRequisitionForAddLineForE2E1("Second item in the DA", "CGI FRANCE", "N020101", "25", "2",
					DateUtils.getCurrentDate(), DateUtils.getFutureDate());
			Thread.sleep(4000);
			submitReq.chooseBillingAcc();
			test.pause(3000);
			submitReq.validateTask();
			testReport.pass("Requisition submited for approval successfully");
			Thread.sleep(15000);

			UserAdministrationPage userAdmin = new UserAdministrationPage(test.driver);

//	        KARIM SANKHON
			userAdmin.actAsUser("KARIM SANKHON");
			Thread.sleep(2000);
			userAdmin.actAsUser();
			testReport.pass("Acting as user KARIM SANKHON is done successfully");
			userAdmin.approveReq();
			Thread.sleep(2000);
			userAdmin.uninspectAsUser();

//	        Guillaume TAP
			userAdmin.actAsUser("Guillaume TAP");
			Thread.sleep(2000);
			userAdmin.actAsUser();
			testReport.pass("Acting as user Guillaume TAP is done successfully");
			userAdmin.approveReq();
			Thread.sleep(2000);
			userAdmin.uninspectAsUser();

//	        CYRIL PLANCHE
			userAdmin.actAsUser("CYRIL PLANCHE");
			Thread.sleep(2000);
			userAdmin.actAsUser();
			testReport.pass("Acting as user CYRIL PLANCHE is done successfully");
			userAdmin.approveReq();
			Thread.sleep(2000);
			userAdmin.uninspectAsUser();
			Thread.sleep(3000);
			testReport.pass("Requisition approved successfully");
			Thread.sleep(3000);

////_____________________________________________________________________________

			RequisitionTablePage requisitionPage = new RequisitionTablePage(test.driver);
			requisitionPage.receiveTruckIcon();
			requisitionPage.receiptDate(DateUtils.getDatePlusTwo());
			requisitionPage.submitReceiptionForOneLine();
			Thread.sleep(3000);
			testReport.pass("Requisition receipt has been successfully completed.");
////-------------------send mail from aspenr44v2@gmail.com---------ASPEN2026------------------------------------------------------
			String poNumber = userAdmin.getPOnumber();
			log.info(poNumber);
			email.sendInvoiceEmail(poNumber);

///--------------------------------------------------------------
			InvoiceListPage invoiceManagement = new InvoiceListPage(test.driver);

			userAdmin.actAsUser("CORALIE MANIOU");
			Thread.sleep(2000);
			userAdmin.actAsUser();
			testReport.pass("Acting as user CORALIE MANIOU is done successfully");
			Thread.sleep(3000);

			userAdmin.openUrl("https://accorhotels-test.coupahost.com/invoices/366585/edit");

			invoiceManagement.removePoLine();
			invoiceManagement.fillInvoiceInfo("9001", DateUtils.getCurrentDate());
			invoiceManagement.clickPickLinesFromPO();
			invoiceManagement.searchByPONumber(poNumber);
			invoiceManagement.addPOLineToInvoice();
			invoiceManagement.selectFirstRemitToOption();
			invoiceManagement.clickCheckboxSafe();
			testReport.pass("E2E 2 - successfully passed\");");
			Thread.sleep(10000);

			invoiceManagement.navigate();
			invoiceManagement.clickCreateForMatchingRow(DateUtils.getCurrentDate());
			Thread.sleep(6000);
			userAdmin.uninspectAsUser();
			Thread.sleep(3000);
			testReport.pass("Invoice created successfully");
		} catch (Exception e) {
			testReport.fail("Test Failed: " + e.getMessage());
		} finally {
			test.tearDown();
			extent.flush();
		}
	}

}
