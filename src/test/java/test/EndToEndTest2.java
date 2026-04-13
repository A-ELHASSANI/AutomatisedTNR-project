package test;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.admin.UserAdminPage;
import pages.requisition.RequisitionListPage;
import pages.requisition.RequisitionFormPage;
import pages.goodreceipt.GoodReceiptPage;
import utils.DateUtils;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 * END-TO-END TEST 2  ← This is the focus of the current project
 * Flow: Login → Create Requisition (2 lines) → Act as User → 
 *       Submit → Receive Goods
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class EndToEndTest2 extends BaseTest {

    @Test(description = "E2E2 – Full requisition flow with good receipt")
    public void endToEndTest2() throws InterruptedException {
        startTest("E2E Test 2 – Requisition + Good Receipt");
        try {
            // Step 1: Login
            loginAsDefault();
            testReport.pass("Step 1 – Login successful");

            // Step 2: Act as requesting user
            UserAdminPage admin = new UserAdminPage(driver);
            admin.navigateToUsers();
            admin.actAsUser("Coralie MANIOU");
            pause(3000);
            testReport.pass("Step 2 – Acting as Coralie MANIOU");

            // Step 3: Create requisition with 2 lines
            RequisitionListPage listPage = new RequisitionListPage(driver);
            listPage.navigate();
            listPage.startNewRequisition();

            RequisitionFormPage form = new RequisitionFormPage(driver);

            // Line 1
            form.fillDescription("E2E2 – Item A");
            form.fillUnitPrice("100.00");
            form.selectFirstSupplier("Test");
            form.fillNeedByDate(DateUtils.inOneWeek());
            form.fillStartDate(DateUtils.getCurrentDate());
            form.saveLines();
            testReport.pass("Step 3a – Line 1 added");

            // Line 2
            form.addLine();
            form.fillDescription("E2E2 – Item B");
            form.fillUnitPrice("200.00");
            form.selectFirstSupplier("Test");
            form.fillNeedByDate(DateUtils.inOneWeek());
            form.fillStartDate(DateUtils.getCurrentDate());
            form.saveLines();
            testReport.pass("Step 3b – Line 2 added");

            form.submit();
            testReport.pass("Step 3c – Requisition submitted");

            // Step 4: Stop acting as user, go back to admin
            admin.uninspectAsUser();
            pause(2000);
            testReport.pass("Step 4 – Returned to admin account");

            // Step 5: Receive goods from requisition list
            listPage.navigate();
            listPage.clickReceiveForUser("Coralie MANIOU", "100.00");
            listPage.setReceiptDate(DateUtils.getCurrentDate());
            listPage.checkMassActionAndSubmit();
            testReport.pass("Step 5 – Good receipt submitted");

        } catch (Exception e) {
            testReport.fail("E2E Test 2 failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
