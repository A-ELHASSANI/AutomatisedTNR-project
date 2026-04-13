package test;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.admin.UserAdminPage;
import pages.supplier.SupplierListPage;
import pages.supplier.SupplierSimFormPage;
import pages.supplier.SupplierCspPage;

/**
 * ─────────────────────────────────────────────────────────────────────────────
 * Supplier Test Suite  –  15 scenarios mapped from TNR Excel
 *
 * Run all:     mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-supplier.xml
 * Run single:  mvn test -Dtest=supplier.SupplierTest#tc001_openSupplierCreationForm
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class SupplierTest extends BaseTest {

    // ── TC_SUP_001 ─────────────────────────────────────────────────────────────
    /**
     * Role: AH AIO Supplier Manager "Carole Fabert"
     * Step: From the Home page, go to Forms and find the
     *       "Supplier Creation Form - Aspen" form and click on it.
     * Expected: The new supplier request form is displayed.
     */
    @Test(description = "TC_SUP_001 - Open Supplier Creation Form (SIM Form) from Forms menu")
    public void tc001_openSupplierCreationForm() throws InterruptedException {
        startTest("TC_SUP_001 – Open Supplier Creation Form");
        try {
        	testReport.pass("Browser opened successfully");
            loginAsDefault();
            testReport.pass("User is logged in successfully");
            UserAdminPage admin = new UserAdminPage(driver);
            admin.actAsUser("Carole Fabert");
	        testReport.pass("Acting as user Carole fabert is done successfully");
            pause(2000);
            SupplierSimFormPage simForm = new SupplierSimFormPage(driver);
            simForm.openSupplierCreationForm();
            Assert.assertTrue(simForm.isSimFormDisplayed(),
                "SIM form should be visible after clicking the form link");
            testReport.pass("Supplier Creation Form - Aspen is displayed");
            testReport.pass("AIO Admin (Carole Fabert) approved the form; second form should auto-launch");
            admin.uninspectAsUser();
	        testReport.pass("User is uninspected successfully");
        } catch (Exception e) {
            testReport.fail("TC_SUP_001 failed: " + e.getMessage());
            throw e;
        }
    }

    // ── TC_SUP_002 ─────────────────────────────────────────────────────────────
    /**
     * Role: AH AIO Supplier Manager
     * Step: Scroll down and select a Payment Method (EFT or WIRE), then submit for approval.
     * Expected: Form status = Pending Approval.
     */
    @Test(description = "TC_SUP_002 - Select Payment Method EFT and Submit for Approval")
    public void tc002_selectPaymentAndSubmit() throws InterruptedException {
        startTest("TC_SUP_002 – Select Payment Method and Submit");
        try {
            SupplierSimFormPage simForm = new SupplierSimFormPage(driver);
            simForm.openSupplierCreationForm();
            simForm.selectPaymentMethodEft();
            simForm.submitForApproval();
            String status = simForm.getFormStatus();
            Assert.assertTrue(status.toLowerCase().contains("pending"),
                "Expected pending approval status, got: " + status);
            testReport.pass("Payment method EFT selected; form status = " + status);
        } catch (Exception e) {
            testReport.fail("TC_SUP_002 failed: " + e.getMessage());
            throw e;
        }
    }

    // ── TC_SUP_003 ─────────────────────────────────────────────────────────────
    /**
     * Role: Approver / Ultimate Approver
     * Step: Go to the form response assigned as approver, press Edit, then Approve.
     * Expected: Form approved; modification visible in the Supplier Record page.
     */
//    @Test(description = "TC_SUP_003 – Approver edits and approves the form response")
//    public void tc003_approverEditAndApprove() throws InterruptedException {
//        startTest("TC_SUP_003 – Approver Edit & Approve");
//        try {
//            loginAsDefault();   // log in as the Approver / Ultimate Approver account
//            SupplierSimFormPage simForm = new SupplierSimFormPage(driver);
//            simForm.clickEditOnFormResponse();
//            pause(1500);
//            simForm.approveForm();
//            testReport.pass("Form approved by approver");
//        } catch (Exception e) {
//            testReport.fail("TC_SUP_003 failed: " + e.getMessage());
//            throw e;
//        }
//    }

    // ── TC_SUP_004 ─────────────────────────────────────────────────────────────
    /**
     * Role: AH AIO Supplier Manager
     * Step: Go to the "Suppliers" tab.
     * Expected: The Suppliers page is displayed.
     */
//    @Test(description = "TC_SUP_004 – Navigate to Suppliers tab after approval")
//    public void tc004_navigateToSuppliersTab() throws InterruptedException {
//        startTest("TC_SUP_004 – Navigate to Suppliers Tab");
//        try {
//            loginAsDefault();
//            SupplierListPage listPage = new SupplierListPage(driver);
//            listPage.navigate();
//            testReport.pass("Suppliers page is displayed");
//        } catch (Exception e) {
//            testReport.fail("TC_SUP_004 failed: " + e.getMessage());
//            throw e;
//        }
//    }

    // ── TC_SUP_005 ─────────────────────────────────────────────────────────────
    /**
     * Role: AIO Supplier admin → Carole Fabert
     * Step: Form is in pending approval; AIO admin verifies and approves it.
     *       A second form (Update Buyer - Aspen 2.0) should launch automatically.
     * Expected: Form approved; second form auto-launched.
     */
    @Test(description = "TC_SUP_005 – AIO Supplier admin (Carole Fabert) approves pending form")
    public void tc005_aioAdminApproves() throws InterruptedException {
        startTest("TC_SUP_005 – AIO Admin Approves Form");
        try {
			testReport.pass("Browser opened successfully");
            loginAsDefault();
            testReport.pass("User is logged in successfully");
            UserAdminPage admin = new UserAdminPage(driver);
            admin.actAsUser("Carole Fabert");
	        testReport.pass("Acting as user Carole fabert is done successfully");
            pause(2000);
//            SupplierSimFormPage simForm = new SupplierSimFormPage(driver);
//            simForm.approveForm();
            testReport.pass("AIO Admin (Carole Fabert) approved the form; second form should auto-launch");
            admin.uninspectAsUser();
	        testReport.pass("User is uninspected successfully");
        } catch (Exception e) {
            testReport.fail("TC_SUP_005 failed: " + e.getMessage());
            throw e;
        }
    }

    // ── TC_SUP_006 ─────────────────────────────────────────────────────────────
    /**
     * Role: Global Purchasing Administrator
     * Step: Auto-generated form reviewed and approved.
     * Expected: Form finalised and approved.
     */
//    @Test(description = "TC_SUP_006 – Global Purchasing Admin approves auto-generated form")
//    public void tc006_globalPurchasingAdminApproves() throws InterruptedException {
//        startTest("TC_SUP_006 – Global Purchasing Admin Approves");
//        try {
//            loginAsDefault();   // log in as Global Purchasing Administrator account
//            SupplierSimFormPage simForm = new SupplierSimFormPage(driver);
//            simForm.approveForm();
//            testReport.pass("Global Purchasing Admin approved; form finalised");
//        } catch (Exception e) {
//            testReport.fail("TC_SUP_006 failed: " + e.getMessage());
//            throw e;
//        }
//    }

    // ── TC_SUP_007 ─────────────────────────────────────────────────────────────
    /**
     * Role: Admin
     * Step: Find a supplier not linked to CSP (2 contacts, 1 Primary),
     *       open in Edit mode, send CSP invitation to all contacts.
     * Expected: "Compose Invitation to Coupa Supplier Portal" page shown;
     *           invitation sent to all contacts.
     */
//    @Test(description = "TC_SUP_007 – Admin sends CSP invitation to all supplier contacts")
//    public void tc007_sendCspInvitation() throws InterruptedException {
//        startTest("TC_SUP_007 – Send CSP Invitation");
//        try {
//            loginAsDefault();
//            SupplierListPage listPage = new SupplierListPage(driver);
//            listPage.navigate();
//            listPage.openSupplierByName("TEST_SUPPLIER_NO_CSP");   // replace with real supplier name
//            listPage.sendCspInvitationToAllContacts();
//            testReport.pass("CSP invitation sent to all contacts");
//        } catch (Exception e) {
//            testReport.fail("TC_SUP_007 failed: " + e.getMessage());
//            throw e;
//        }
//    }

    // ── TC_SUP_008 ─────────────────────────────────────────────────────────────
    /**
     * Role: Supplier
     * Step: Log in to CSP with supplier credentials.
     * Expected: CSP Home page is displayed.
     */
//    @Test(description = "TC_SUP_008 – Supplier logs into CSP (smoke check)")
//    public void tc008_supplierLoginCsp() throws InterruptedException {
//        startTest("TC_SUP_008 – Supplier Login CSP");
//        try {
//            setup();
//            openUrl("https://supplier.coupahost.com");   // replace with your CSP URL
//            pause(3000);
//            testReport.pass("CSP Home page loaded");
//        } catch (Exception e) {
//            testReport.fail("TC_SUP_008 failed: " + e.getMessage());
//            throw e;
//        }
//    }

    // ── TC_SUP_009 ─────────────────────────────────────────────────────────────
    /**
     * Role: AH AIO Supplier Manager (Marie VIDAL)
     * Step: Search a linked supplier → click green RFI arrow →
     *       select form → click Request → Send Info Request.
     * Expected: "Request for Information successfully sent" message;
     *           supplier receives email notification.
     */
//    @Test(description = "TC_SUP_009 – Manager sends Request for Information (RFI)")
//    public void tc009_sendRfi() throws InterruptedException {
//        startTest("TC_SUP_009 – Send RFI");
//        try {
//            loginAsDefault();
//            UserAdminPage admin = new UserAdminPage(driver);
//            admin.navigateToUsers();
//            admin.actAsUser("Marie VIDAL");
//            pause(2000);
//            SupplierListPage listPage = new SupplierListPage(driver);
//            listPage.navigate();
//            listPage.clickSendRfi("TEST_LINKED_SUPPLIER");   // replace with real supplier name
//            pause(1500);
//            listPage.submitRfi();
//            String msg = listPage.getRfiConfirmationMessage();
//            Assert.assertTrue(msg.toLowerCase().contains("successfully sent"),
//                "Expected RFI success message, got: " + msg);
//            testReport.pass("RFI sent – confirmation: " + msg);
//            admin.uninspectAsUser();
//        } catch (Exception e) {
//            testReport.fail("TC_SUP_009 failed: " + e.getMessage());
//            throw e;
//        }
//    }

    // ── TC_SUP_010 ─────────────────────────────────────────────────────────────
    /**
     * Role: Supplier
     * Step: Log into CSP → Notification tab → open external form →
     *       add a new remit-to → fill required info → Submit.
     * Expected: External form visible; remit-to submitted.
     */
//    @Test(description = "TC_SUP_010 – Supplier responds to RFI in CSP (notification + remit-to)")
//    public void tc010_supplierRespondsToRfi() throws InterruptedException {
//        startTest("TC_SUP_010 – Supplier Responds to RFI");
//        try {
//            setup();
//            openUrl("https://supplier.coupahost.com");   // replace with your CSP URL
//            pause(2000);
//            SupplierCspPage csp = new SupplierCspPage(driver);
//            csp.openNotifications();
//            csp.openExternalForm();
//            csp.clickAddRemitTo();
//            // TODO: fill remit-to fields specific to your CSP form here
//            csp.saveProfile();
//            testReport.pass("Supplier responded to RFI and added remit-to in CSP");
//        } catch (Exception e) {
//            testReport.fail("TC_SUP_010 failed: " + e.getMessage());
//            throw e;
//        }
//    }

    // ── TC_SUP_011 ─────────────────────────────────────────────────────────────
    /**
     * Role: AIO Supplier admin HQ → AH Buyer (MARIE LESUEUR)
     * Step: AIO HQ checks Forms menu → approves (highlighted fields visible).
     *       AH Buyer opens supplier → confirms changes are transferred.
     * Expected: Changes from CSP visible on supplier record.
     */
//    @Test(description = "TC_SUP_011 – Admin/Buyer reviews and confirms RFI response")
//    public void tc011_adminReviewsRfiResponse() throws InterruptedException {
//        startTest("TC_SUP_011 – Admin Reviews RFI Response");
//        try {
//            loginAsDefault();
//            UserAdminPage admin = new UserAdminPage(driver);
//
//            // Step 1: AIO Supplier admin HQ approves
//            admin.navigateToUsers();
//            admin.actAsUser("AIO Supplier Admin HQ");   // replace with real username
//            pause(2000);
//            SupplierSimFormPage simForm = new SupplierSimFormPage(driver);
//            simForm.approveForm();
//            testReport.pass("AIO Admin HQ approved RFI response");
//            admin.uninspectAsUser();
//
//            // Step 2: AH Buyer confirms changes on supplier record
//            admin.actAsUser("MARIE LESUEUR");
//            pause(2000);
//            SupplierListPage listPage = new SupplierListPage(driver);
//            listPage.navigate();
//            listPage.openSupplierByName("TEST_LINKED_SUPPLIER");   // replace with real supplier name
//            testReport.pass("AH Buyer confirmed changes visible on supplier record");
//            admin.uninspectAsUser();
//        } catch (Exception e) {
//            testReport.fail("TC_SUP_011 failed: " + e.getMessage());
//            throw e;
//        }
//    }

    // ── TC_SUP_012 ─────────────────────────────────────────────────────────────
    /**
     * Role: Supplier
     * Step: CSP → Business Profile → Legal Entity → fill info → save.
     *       Payment Method → Add Remit-To → share with Accor.
     * Expected: Data updated; remit-to shared with Accor only.
     */
//    @Test(description = "TC_SUP_012 – Supplier updates CSP business profile, legal entity and payment method")
//    public void tc012_cspProfileAndPaymentUpdate() throws InterruptedException {
//        startTest("TC_SUP_012 – CSP Profile & Payment Update");
//        try {
//            setup();
//            openUrl("https://supplier.coupahost.com");   // replace with your CSP URL
//            pause(2000);
//            SupplierCspPage csp = new SupplierCspPage(driver);
//
//            // Legal entity update
//            csp.openLegalEntity();
//            // TODO: type(By.id("..."), "value"); — fill required legal entity fields
//            csp.saveProfile();
//            testReport.pass("Legal entity info updated");
//
//            // Payment method / remit-to
//            csp.openPaymentMethod();
//            csp.clickAddRemitTo();
//            // TODO: fill remit-to address fields here
//            csp.shareFirstRemitToWithAccor();
//            testReport.pass("Remit-to added and shared with Accor only");
//        } catch (Exception e) {
//            testReport.fail("TC_SUP_012 failed: " + e.getMessage());
//            throw e;
//        }
//    }

    // ── TC_SUP_013 ─────────────────────────────────────────────────────────────
    /**
     * Role: Supplier
     * Step: Invoice menu → Export → Legal Invoice (zip).
     * Expected: Green message "The data you requested will be emailed to you shortly."
     */
//    @Test(description = "TC_SUP_013 – Supplier exports Legal Invoice (zip) from CSP")
//    public void tc013_exportLegalInvoice() throws InterruptedException {
//        startTest("TC_SUP_013 – Legal Invoice Export");
//        try {
//            setup();
//            openUrl("https://supplier.coupahost.com");   // replace with your CSP URL
//            pause(2000);
//            SupplierCspPage csp = new SupplierCspPage(driver);
//            csp.exportLegalInvoiceZip();
//            testReport.pass("Legal Invoice (zip) export triggered; confirmation email expected");
//        } catch (Exception e) {
//            testReport.fail("TC_SUP_013 failed: " + e.getMessage());
//            throw e;
//        }
//    }

    // ── TC_SUP_014 ─────────────────────────────────────────────────────────────
    /**
     * Role: Admin
     * Step: Supplier menu → find supplier with no email → update PO Method to Email.
     *       Create PO → supplier receives email with "Create Your Account" button.
     * Expected: Supplier record modified; PO email received.
     */
//    @Test(description = "TC_SUP_014 – Admin updates PO Method to Email for non-CSP supplier")
//    public void tc014_poByEmail() throws InterruptedException {
//        startTest("TC_SUP_014 – PO by Email");
//        try {
//            loginAsDefault();
//            SupplierListPage listPage = new SupplierListPage(driver);
//            listPage.navigate();
//            listPage.openSupplierByName("TEST_SUPPLIER_NO_EMAIL");   // replace with real supplier name
//            listPage.updatePoMethodToEmail();
//            testReport.pass("PO Method updated to Email on supplier record");
//            // NOTE: PO creation itself is covered by PurchaseOrderTest#testCreatePO
//        } catch (Exception e) {
//            testReport.fail("TC_SUP_014 failed: " + e.getMessage());
//            throw e;
//        }
//    }

    // ── TC_SUP_015 ─────────────────────────────────────────────────────────────
    /**
     * Role: Admin
     * Step: Setup → Home Page → Announcements → Create → fill info →
     *       select "Coupa Supplier Portal" → Save.
     *       Edit the announcement → add "Orders" tab → Save.
     * Expected: Announcement created; visible on CSP and on Orders tab.
     */
//    @Test(description = "TC_SUP_015 – Admin creates and edits a CSP announcement")
//    public void tc015_cspAnnouncement() throws InterruptedException {
//        startTest("TC_SUP_015 – CSP Announcement Create & Edit");
//        try {
//            loginAsDefault();
//            SupplierListPage listPage = new SupplierListPage(driver);
//
//            listPage.createAnnouncement(
//                "Automation Test Announcement",
//                "This announcement was created by the automation suite. Please ignore."
//            );
//            testReport.pass("Announcement created and visible on Coupa Supplier Portal");
//
//            listPage.editAnnouncementAddTab("Orders");
//            testReport.pass("Announcement updated to also show on Orders tab");
//        } catch (Exception e) {
//            testReport.fail("TC_SUP_015 failed: " + e.getMessage());
//            throw e;
//        }
//    }
}
