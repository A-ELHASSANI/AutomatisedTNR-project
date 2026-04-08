package pages.E2E.EndToEnd2;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import base.BasePage;
import utils.*;

public class MainTest extends BasePage {
	public static void main(String[] args) throws InterruptedException {

		ExtentReports extent = ReportManager.getReportInstance();
		ExtentTest testReport = extent.createTest("Coupa Requisition Test");

		MainTest test = new MainTest();

		try {
			test.setup();
			test.openUrl("https://accorhotels-test.coupahost.com/sessions/new");

			testReport.pass("Browser opened successfully");
			LoginPage loginPage = new LoginPage(test.driver);
			loginPage.login("TEMP-AELHA", "Clavier@123");
//----------------------------------------WOEKED------------------------------
//	        RequisitionTaskPage taskPage = new RequisitionTaskPage(test.driver);
//            taskPage.fillRequisition("Test automatised", "#FRANCE 2023", "50",  DateUtils.getCurrentDate() , DateUtils.getFutureDate()  );
//            taskPage.validateTask();
//	        Thread.sleep(4000);
//	        testReport.pass("Requisition filled and added to card successfully");
////----------------------------------------------------------------------------
			SubmitRequisitionForApproval submitReq = new SubmitRequisitionForApproval(test.driver);
			submitReq.viewBasket();
			Thread.sleep(2000);
//
//			submitReq.addAttachment("C:\\Users\\anas.elhassani\\Downloads\\TEST.pdf");
//			Thread.sleep(2000);
//
//			submitReq.addComment("test");
//			Thread.sleep(2000);
//			submitReq.typeTitle("tested title");
//			Thread.sleep(4000);
//			taskPage.addLine();
//			taskPage.fillRequisitionForAddLine("Test automatised to add line ", "#FRANCE 2023", "50",  DateUtils.getCurrentDate() , DateUtils.getFutureDate());
//			Thread.sleep(4000);
			submitReq.chooseBillingAcc();
			test.pause(3000);
//			submitReq.validateTask();
//			testReport.pass("Requisition submited for approval successfully");
//			Thread.sleep(8000);
////-----------------------------------------------------------------------------------
//	        UserAdministrationPage userAdmin = new UserAdministrationPage(test.driver);
//	        userAdmin.actAsUser("CYRIL PLANCHE");
//	        Thread.sleep(2000);
//	        userAdmin.actAsUser();
//	        testReport.pass("Acting as user CYRIL PLANCHE is done successfully");
//	        userAdmin.approveReq();
//	        userAdmin.uninspectAsUser();
//	        Thread.sleep(3000);
//	        testReport.pass("Requisition approved successfully");
//	        Thread.sleep(3000);

////_____________________________________________________________________________
			
//			RequisitionTablePage requisitionPage = new RequisitionTablePage(test.driver);
//			requisitionPage.receiveTruckIcon();
//			requisitionPage.receiptDate(DateUtils.getDatePlusTwo());
//			requisitionPage.submitReceiption();
//			Thread.sleep(3000);
//			testReport.pass("Requisition receipt has been successfully completed.");
////---------------------------------------------------------------------------------------------
//			userAdmin.actAsUser("Coralie MANIOU");
//			Thread.sleep(2000);
//			userAdmin.actAsUser();
//			Thread.sleep(3000);
//	        testReport.pass("Acting as user Coralie MANIOU is done successfully");


		} catch (Exception e) {
			testReport.fail("Test Failed: " + e.getMessage());
		} finally {
			test.tearDown();
			extent.flush(); // Generate report
		}
	}

}
