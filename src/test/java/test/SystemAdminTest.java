package test;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.admin.SystemAdminPage;
import pages.purchaseorder.PurchaseOrderListPage;
import utils.DateUtils;

/**
 * System Administration test suite.
 *
 *   TC_SA_001 – Create User manually
 *   TC_SA_002 – Close PO (Soft-close + Hard-close)
 *   TC_SA_003 – Upload User List (CSV bulk load)
 *   TC_SA_004 – Deactivate User with open POs
 *   TC_SA_005 – Bulk update notification preferences
 *   TC_SA_006 – User Permission Reassignment
 *   TC_SA_007 – Create Custom Home Page Content
 *   TC_SA_008 – Create Homepage Announcements
 *   TC_SA_009 – Role Comparison
 */
public class SystemAdminTest extends BaseTest {

    private SystemAdminPage     adminPage;
    private PurchaseOrderListPage poPage;

    // ── Test data (all from config.properties) ────────────────────────────────
    private String newUserLogin;
    private String newUserFirstName;
    private String newUserLastName;
    private String newUserEmail;
    private String newUserCurrency;
    private String newUserLanguage;
    private String newUserRegion;
    private String newUserChartOfAccounts;
    private String newUserDefaultAccount;
    private String newUserDepartmentGb;
    private String newUserNextApprover;
    private String newUserRole;
    private String secondRole;
    private String thirdRole;
    private String newUserContentGroup;
    private String newScopeContentGroup;
    private String newRegionContentGroup;
    private String csvFilePath;
    private String userToDeactivate;
    private String reassignToUser;
    private String notificationAction;
    private String notificationDelivery;
    private String notificationName;
    private String notifTargetUser;
    private String permReassignFrom;
    private String permReassignTo;
    private String homePageContentGroup;
    private String homePageContentText;
    private String announcementSubject;
    private String announcementContentGroup;
    private String announcementStartAt;
    private String announcementEndAt;
    private String announcementMessage;
    private String role1Name;
    private String role2Name;

    // ── Setup ─────────────────────────────────────────────────────────────────

    @BeforeClass
    public void setUp() throws InterruptedException {
        super.initSuite();

        // Read all test data from config.properties
        newUserLogin           = config.get("sa.user.login");
        newUserFirstName       = config.get("sa.user.firstname");
        newUserLastName        = config.get("sa.user.lastname");
        newUserEmail           = config.get("sa.user.email");
        newUserCurrency        = config.get("sa.user.currency");
        newUserLanguage        = config.get("sa.user.language");
        newUserChartOfAccounts = config.get("sa.user.chart_of_accounts");
        newUserDefaultAccount  = config.get("sa.user.default_account");
        newUserDepartmentGb    = config.get("sa.user.department_gb");
        newUserNextApprover    = config.get("sa.user.next_approver");
        newUserRole            = config.get("sa.user.role");
        secondRole             = config.get("sa.user.secondRole");
        thirdRole              = config.get("sa.user.thirdRole");
        newUserContentGroup    = config.get("sa.user.content_group");
        newScopeContentGroup   = config.get("sa.user.content_group");
        newRegionContentGroup  = config.get("sa.user.content_group");
        csvFilePath            = config.get("sa.csv.filepath");
        userToDeactivate       = config.get("sa.deactivate.user");
        reassignToUser         = config.get("sa.deactivate.reassign_to");
        notificationAction     = config.get("sa.notification.action");
        notificationDelivery   = config.get("sa.notification.delivery");
        notificationName       = config.get("sa.notification.name");
        notifTargetUser        = config.get("sa.notification.target_user");
        permReassignFrom       = config.get("sa.permission.reassign_from");
        permReassignTo         = config.get("sa.permission.reassign_to");
        homePageContentGroup   = config.get("sa.homepage.content_group");
        homePageContentText    = config.get("sa.homepage.content_text");
        announcementSubject    = config.get("sa.announcement.subject");
        announcementContentGroup = config.get("sa.announcement.content_group");
        announcementStartAt    = config.get("sa.announcement.start_at");
        announcementEndAt      = config.get("sa.announcement.end_at");
        announcementMessage    = config.get("sa.announcement.message");
        role1Name              = config.get("sa.role.role1");
        role2Name              = config.get("sa.role.role2");

        // Browser setup + login
        loginAsDefault();

        adminPage = new SystemAdminPage(driver);
        poPage    = new PurchaseOrderListPage(driver);
    }

    // =========================================================================
    // TC_SA_001 – CREATE USER MANUALLY
    // =========================================================================

    @Test(description = "TC_SA_001: Create a new user manually through Setup → Users → Create")
    public void tc_SA_001_createUser() {
        startTest("TC_SA_001 - Create User");

        // Step 1-2: Navigate to Users page and click Create
        adminPage.navigateToCreateUser();
        testReport.pass("Navigated to Users page and clicked Create");

        // Steps 4-11: Fill user details
        adminPage.fillUserDetails(
            newUserLogin, newUserFirstName, newUserLastName,
            newUserEmail, newUserCurrency, newUserLanguage,
            newUserChartOfAccounts, newUserDepartmentGb
        );
        testReport.pass("User details section filled");

        // Step 5: Set Active status
        adminPage.setUserStatusActive();
        testReport.pass("User status set to Active");

        // Steps 6-8: Approval settings
        adminPage.setNextApprover(newUserNextApprover);
        adminPage.fillApprovalLimits("10000", "10000");
        testReport.pass("Approval settings configured");

        // Step 12: Enable Generate Password
        adminPage.enableGeneratePassword();
        testReport.pass("Generate password checkbox enabled");

        // Step 14 : Check invoicing and purchasing license
        adminPage.checkLicense();
        
        // Steps 14-16: Role and Content Group
        adminPage.checkRole(newUserRole , secondRole , thirdRole);
        
        pause(20000);
        adminPage.configureContentGroup(newUserContentGroup);
        testReport.pass("Roles and content groups configured");

        // Step 19: Submit
        adminPage.submitCreateUser();
        String msg = adminPage.getFlashMessage();
        Assert.assertTrue(msg.toLowerCase().contains("created") || msg.toLowerCase().contains("success"),
            "Expected user creation success message, got: " + msg);
        testReport.pass("User created successfully — " + msg);

        log.info("TC_SA_001 PASSED");
    }

    // =========================================================================
    // TC_SA_002 – CLOSE PO FOR AN ORDER "NOT RECEIVED"
    // =========================================================================
//
//    @Test(description = "TC_SA_002: Soft-close then hard-close a PO that was not received")
//    public void tc_SA_002_closePO() {
//        startTest("TC_SA_002 - Close PO");
//
//        // Step 1: Login already done in @BeforeClass
//
//        // Step 2: Navigate to Orders
//        poPage.navigate();
//        testReport.pass("Navigated to Purchase Orders page");
//
//        // Steps 3-4: Locate and soft-close a PO
//        poPage.softClosePO();
//        testReport.pass("Soft-close confirmation dialog accepted");
//
//        // Step 5: Hard-close the PO
//        poPage.hardClosePO();
//        testReport.pass("PO closed successfully");
//
//        log.info("TC_SA_002 PASSED");
//    }
//
//    // =========================================================================
//    // TC_SA_003 – UPLOAD USER LIST (CSV BULK LOAD)
//    // =========================================================================
//
//    @Test(description = "TC_SA_003: Upload a CSV file to bulk-create users")
//    public void tc_SA_003_uploadUserList() {
//        startTest("TC_SA_003 - Upload User List");
//
//        // Navigate to Bulk Load Users page
//        adminPage.navigateToBulkUserLoad();
//        testReport.pass("Navigated to Bulk Load Users page");
//
//        // Download the CSV template
//        adminPage.downloadCsvTemplate();
//        testReport.pass("CSV template downloaded");
//
//        // Upload the pre-filled CSV file
//        adminPage.uploadCsvFile(csvFilePath);
//        testReport.pass("CSV file uploaded and Verify Data page displayed");
//
//        // Finish the upload
//        adminPage.finishUpload();
//        testReport.pass("Upload completed successfully");
//
//        // Verify the user exists in the system
//        adminPage.searchAndVerifyUser(newUserLogin);
//        testReport.pass("User found in system after bulk upload: " + newUserLogin);
//
//        log.info("TC_SA_003 PASSED");
//    }
//
//    // =========================================================================
//    // TC_SA_004 – DEACTIVATE USER WITH OPEN POs
//    // =========================================================================
//
//    @Test(description = "TC_SA_004: Deactivate a user who has open POs and reassign their work")
//    public void tc_SA_004_deactivateUser() {
//        startTest("TC_SA_004 - Deactivate User");
//
//        // Step 1: Open user profile
//        adminPage.openUserProfile(userToDeactivate);
//        testReport.pass("User profile opened: " + userToDeactivate);
//
//        // Step 2: Click Edit
//        adminPage.clickEditUser();
//        testReport.pass("User profile in edit mode");
//
//        // Step 3: Set status to Inactive (triggers reassignment popup)
//        adminPage.setUserStatusInactive();
//        testReport.pass("Inactive status selected — reassignment popup appeared");
//
//        // Steps 4-7: Perform reassignment
//        adminPage.performUserReassignment(reassignToUser);
//        testReport.pass("Reassignment tasks selected and Apply clicked");
//
//        // Step 8: Wait for Done
//        adminPage.waitForReassignmentDone(userToDeactivate);
//        testReport.pass("Reassignment completed — user is now Inactive");
//
//        log.info("TC_SA_004 PASSED");
//    }
//
//    // =========================================================================
//    // TC_SA_005 – BULK UPDATE NOTIFICATION PREFERENCES
//    // =========================================================================
//
//    @Test(description = "TC_SA_005: Bulk enable/disable notification preferences for users")
//    public void tc_SA_005_notificationPreferences() {
//        startTest("TC_SA_005 - Bulk Notification Preferences");
//
//        // Navigate to Notifications page
//        adminPage.navigateToNotifications();
//        testReport.pass("Navigated to Notifications page");
//
//        // Select action and delivery method
//        adminPage.configureNotificationAction(notificationAction, notificationDelivery);
//        testReport.pass("Action '" + notificationAction + "' with delivery '" + notificationDelivery + "' selected");
//
//        // Select notification type
//        adminPage.selectNotification(notificationName);
//        testReport.pass("Notification selected: " + notificationName);
//
//        // Add target user
//        adminPage.addNotificationUser(notifTargetUser);
//        testReport.pass("Target user added: " + notifTargetUser);
//
//        // Apply
//        adminPage.applyNotificationSettings();
//        testReport.pass("Notification preferences applied — confirmation message displayed");
//
//        log.info("TC_SA_005 PASSED");
//    }
//
//    // =========================================================================
//    // TC_SA_006 – USER PERMISSION REASSIGNMENT
//    // =========================================================================
//
//    @Test(description = "TC_SA_006: Reassign user permissions and roles from one user to another")
//    public void tc_SA_006_permissionReassignment() {
//        startTest("TC_SA_006 - User Permission Reassignment");
//
//        // Navigate to User Reassignments → Create
//        adminPage.navigateToUserReassignment();
//        testReport.pass("Navigated to User Reassignment Create page");
//
//        // Fill fields and apply
//        adminPage.createPermissionReassignment(permReassignFrom, permReassignTo, false);
//        testReport.pass("Reassignment created — Remove from: " + permReassignFrom +
//                        ", Assign to: " + permReassignTo);
//
//        // Wait for Done
//        adminPage.waitForPermissionReassignmentDone();
//        testReport.pass("Reassignment job completed — status shows Done");
//
//        log.info("TC_SA_006 PASSED");
//    }
//
//    // =========================================================================
//    // TC_SA_007 – CREATE CUSTOM HOME PAGE CONTENT
//    // =========================================================================
//
//    @Test(description = "TC_SA_007: Create a custom Home Page content block for a specific content group")
//    public void tc_SA_007_homePageContent() {
//        startTest("TC_SA_007 - Create Custom Home Page Content");
//
//        // Navigate to Home Page Content → Create
//        adminPage.navigateToHomePageContent();
//        testReport.pass("Navigated to Home Page Content Create page");
//
//        // Fill and publish
//        adminPage.createHomePageContent(homePageContentGroup, homePageContentText);
//        testReport.pass("Custom home page content created and published for group: " + homePageContentGroup);
//
//        log.info("TC_SA_007 PASSED");
//    }
//
//    // =========================================================================
//    // TC_SA_008 – CREATE HOMEPAGE ANNOUNCEMENTS
//    // =========================================================================
//
//    @Test(description = "TC_SA_008: Create a Homepage announcement with mandatory fields and verify on Home page")
//    public void tc_SA_008_createAnnouncement() {
//        startTest("TC_SA_008 - Create Homepage Announcement");
//
//        // Navigate to Announcements → Create
//        adminPage.navigateToAnnouncements();
//        testReport.pass("Navigated to Announcements Create page");
//
//        // Fill all fields and save
//        adminPage.createAnnouncement(
//            announcementSubject,
//            announcementContentGroup,
//            announcementStartAt,
//            announcementEndAt,
//            announcementMessage,
//            true
//        );
//
//        String msg = adminPage.getAnnouncementSuccessMessage();
//        Assert.assertTrue(msg.toLowerCase().contains("created") || msg.toLowerCase().contains("success"),
//            "Expected announcement success message, got: " + msg);
//        testReport.pass("Announcement created — " + msg);
//
//        log.info("TC_SA_008 PASSED");
//    }
//
//    // =========================================================================
//    // TC_SA_009 – ROLE COMPARISON
//    // =========================================================================
//
//    @Test(description = "TC_SA_009: Compare two roles and verify permission display filters")
//    public void tc_SA_009_roleComparison() {
//        startTest("TC_SA_009 - Role Comparison");
//
//        // Navigate to Roles page
//        adminPage.navigateToRoles();
//        testReport.pass("Navigated to Roles page");
//
//        // Open role 1
//        adminPage.openRole(role1Name);
//        testReport.pass("Opened role: " + role1Name);
//
//        // Compare to role 2
//        adminPage.compareRoleTo(role2Name);
//        testReport.pass("Comparing " + role1Name + " vs " + role2Name);
//
//        // Verify the 3 display filters
//        adminPage.toggleShowRole1Permissions();
//        testReport.pass("'Show permissions only in Role 1' filter applied (red/bold)");
//
//        adminPage.toggleShowRole2Permissions();
//        testReport.pass("'Show permissions only in Role 2' filter applied (green/italic)");
//
//        adminPage.toggleShowBothRoles();
//        testReport.pass("'Show permissions in both roles' filter applied");
//
//        adminPage.toggleShowBothRoles();
//        testReport.pass("Filter deselected — hidden permissions shown again");
//
//        // Compare to original role
//        adminPage.compareToOriginalRole();
//        testReport.pass("Custom role compared to original role in Actions column");
//
//        log.info("TC_SA_009 PASSED");
//    }
}
