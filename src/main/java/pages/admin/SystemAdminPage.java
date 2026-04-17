package pages.admin;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.BasePage;

/**
 * Page Object covering all System Administration scenarios:
 *
 * TC_SA_001 – Create User manually TC_SA_003 – Upload User List (CSV bulk load)
 * TC_SA_004 – Deactivate User with open POs TC_SA_005 – Bulk update
 * notification preferences TC_SA_006 – User Permission Reassignment TC_SA_007 –
 * Create Custom Home Page Content TC_SA_008 – Create Homepage Announcements
 * TC_SA_009 – Role Comparison
 *
 * TC_SA_002 (Close PO) is handled by PurchaseOrderListPage.
 */
public class SystemAdminPage extends BasePage {

	private static final int DEFAULT_WAIT = 15;
	private static final int LONG_WAIT = 30;

	// ── Common navigation ─────────────────────────────────────────────────────
	private final By setupMenu = By.linkText("Setup");
	private final By usersMenu = By.linkText("Users");
	private final By searchUserField = By.id("sf_user");
	private final By searchButton = By.id("sfBtn_user");
	private final By spinner = By.cssSelector(".loading, .spinner, .overlay");

	public SystemAdminPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT));
	}

	// =========================================================================
	// TC_SA_001 – CREATE USER MANUALLY
	// =========================================================================

	// ── User Details ──────────────────────────────────────────────────────────
	private final By createBtn = By.linkText("Create");
	private final By loginField = By.id("user_login");
	private final By firstNameField = By.id("user_firstname");
	private final By lastNameField = By.id("user_lastname");
	private final By ssoIdentifier = By.id("user_sso_identifier");
	private final By emailField = By.id("user_email");
	private final By mentionName = By.id("user_mention_name");
	private final By currencyDropdown = By.id("user_default_currency_id");
	private final By languageDropdown = By.id("locale_language_id");
//    private final By regionDropdown       = By.id("user_region_id");
	private final By chartOfAccountsField = By.id("user_default_account_type_id");
	private final By departmentGbField = By.cssSelector("input[id*='user_custom_field'], input[name*='custom_field']");

	// ── User Status ───────────────────────────────────────────────────────────
	private final By activeRadio = By.id("user_new_status_active");
	private final By inactiveRadio = By.cssSelector("input[id='user_inactive'], input[value='inactive']");

	// ── Approval settings ─────────────────────────────────────────────────────
	private final By nextApproverField = By.cssSelector("input[id*='user_parent'], input[name*='user[parent]']");
	private final By reqApprovalLimit = By.id("user_requisition_approval_limit_attributes_amount");
	private final By invApprovalLimit = By.id("user_invoice_approval_limit_attributes_amount");

	// ── Password ──────────────────────────────────────────────────────────────
	private final By generatePasswordCheckbox = By.id("user_reset_password");

	// ── License ──────────────────────────────────────────────────────────────
	private final By purchasingLicenseCheckbox = By.id("user_purchasing_user");
	private final By invoicingLicenseCheckbox = By.id("user_invoicing_user");

	// ── Roles ─────────────────────────────────────────────────────────────────
	private final By rolesSection = By.id("roles_section");
	private final By contentGroupRadio = By.id("business_group_security_type_everyone_and_selected");
//			.xpath("//label[contains(.,'Basic content plus selected content groups')]/input");
	private final By allGroupsSelect = By.id("all_business_groups");
	private final By selectedGroupsBtn = By.cssSelector("button[id*='select_business_group'], input[value='Select']");

	// ── Approval groups ───────────────────────────────────────────────────────
	private final By approvalGroupField = By.cssSelector("input[id*='approval_group']");

	// ── Billing Account Security ──────────────────────────────────────────────
	private final By billingAccSecurityRadio = By.cssSelector("input[id*='billing_account_security']");

	// ── Save / Create ─────────────────────────────────────────────────────────
	private final By saveUserBtn = By
			.cssSelector("input[type='submit'][value='Create'], input[type='submit'][value='Save']");

	// ── Success flash ─────────────────────────────────────────────────────────
	private final By flashNotice = By.cssSelector(".flash-notice, #notice, .alert-success");

	/** Navigate to Setup → Users → Create */
	public void navigateToCreateUser() {
		wait.until(ExpectedConditions.elementToBeClickable(setupMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(usersMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(createBtn)).click();
		log.info("Navigated to Create User page");
	}

	/** Fill the User Details section. */
	public void fillUserDetails(String login, String firstName, String lastName, String email, String currency,
			String language, String chartOfAccounts, String departmentGb) {
		type(loginField, login);
		type(firstNameField, firstName);
		type(lastNameField, lastName);
		type(ssoIdentifier, email);
		type(mentionName, login);
		type(emailField, email);

		new Select(driver.findElement(currencyDropdown)).selectByVisibleText(currency);
		new Select(driver.findElement(languageDropdown)).selectByVisibleText(language);

//		typeAutocomplete(chartOfAccountsField, chartOfAccounts);
		selectChartOfAccount(chartOfAccounts);

		if (!departmentGb.isEmpty())
			type(departmentGbField, departmentGb);
		log.info("User details filled for login: {}", login);
	}

	/** Click the Active radio button. */
	public void setUserStatusActive() {
		WebElement radio = wait.until(ExpectedConditions.presenceOfElementLocated(activeRadio));
		if (!radio.isSelected())
			radio.click();
		log.info("Status set to Active");
	}

	/** Set the Next Approver field. */
	public void setNextApprover(String approverName) {
		typeAutocomplete(nextApproverField, approverName);
		log.info("Next approver set: {}", approverName);
	}

	/** Fill requisition and invoice approval limits. */
	public void fillApprovalLimits(String reqLimit, String invLimit) {
		type(reqApprovalLimit, reqLimit);
		type(invApprovalLimit, invLimit);
		log.info("Approval limits set — Req: {}, Inv: {}", reqLimit, invLimit);
	}

	/** Ensure the Generate Password checkbox is ticked. */
	public void enableGeneratePassword() {
		WebElement cb = wait.until(ExpectedConditions.presenceOfElementLocated(generatePasswordCheckbox));
		if (!cb.isSelected())
			cb.click();
		log.info("Generate password checkbox enabled");
	}
	
	public void checkLicense() {
		WebElement purchasingCheckbox = wait.until(ExpectedConditions.presenceOfElementLocated(purchasingLicenseCheckbox));
		if (!purchasingCheckbox.isSelected())
			purchasingCheckbox.click();
		WebElement invoicingCheckbox = wait.until(ExpectedConditions.presenceOfElementLocated(invoicingLicenseCheckbox));
		if (!invoicingCheckbox.isSelected())
			invoicingCheckbox.click();
		log.info("Purchasing & Invoicing License is selected");
	}

	/** Check one or more role checkboxes by visible label text. */
	public void checkRole(String roleLabel, String secondRole , String thirdRole) {
//		List<WebElement> checkboxes = driver.findElements(By.xpath(
//			    "//li[.//a[text()='AH User'] or .//a[text()='AH Accounts Payable'] or .//a[text()='AH Controller']]//input[@type='checkbox']"
//			));
		String xpath = String.format(
		        "//li[.//a[text()='%s'] or .//a[text()='%s'] or .//a[text()='%s']]//input[@type='checkbox']",
		        roleLabel, secondRole, thirdRole
		    );

		    List<WebElement> checkboxes = driver.findElements(By.xpath(xpath));
			for (WebElement checkbox : checkboxes) {
			    if (!checkbox.isSelected()) {
			        checkbox.click();
			    }
			}
//		By roleCheckbox = By.xpath("//div[@id='roles_section' or contains(@class,'roles')]"
//				+ "//label[normalize-space()='" + roleLabel + "']/input");
//		WebElement cb = wait.until(ExpectedConditions.presenceOfElementLocated(roleCheckbox));
//		if (!cb.isSelected())
//			cb.click();
		log.info("Role checked: {}", roleLabel);
	}

	/** Select 'Basic content plus selected content groups' and add a group. */
	public void configureContentGroup(String groupName) {
		wait.until(ExpectedConditions.elementToBeClickable(contentGroupRadio)).click();
		pause(2000);
		WebElement allGroups = wait.until(ExpectedConditions.visibilityOfElementLocated(allGroupsSelect));
		for (WebElement opt : allGroups.findElements(By.tagName("option"))) {
			
			if (opt.getText().trim().equalsIgnoreCase(groupName)) {
				opt.click();
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(selectedGroupsBtn)).click();
		log.info("Content group added: {}", groupName);
	}

	/** Type in the Approval Groups field and confirm. */
	public void addApprovalGroup(String groupName) {
		typeAutocomplete(approvalGroupField, groupName);
		log.info("Approval group added: {}", groupName);
	}

	/** Click Create to save the new user. */
	public void submitCreateUser() {
		wait.until(ExpectedConditions.elementToBeClickable(saveUserBtn)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
		log.info("Create user submitted");
	}

	/** Returns the flash notice text after user creation. */
	public String getFlashMessage() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(flashNotice)).getText().trim();
	}

	// =========================================================================
	// TC_SA_003 – UPLOAD USER LIST (CSV Bulk Load)
	// =========================================================================

	private final By loadFromFileBtn = By
			.cssSelector("a[href*='bulk_load'], a.load-from-file, input[value*='Load from file']");
	private final By downloadCsvLink = By.xpath("//a[contains(.,'CSV plain') or contains(.,'Download')]");
	private final By chooseFileInput = By.cssSelector("input[type='file']");
	private final By startUploadBtn = By.cssSelector("input[value='Start Upload'], button[id*='start_upload']");
	private final By finishUploadBtn = By.cssSelector("input[value='Finish Upload'], button[id*='finish_upload']");
	private final By uploadSuccessMsg = By.cssSelector(".flash-notice, #upload_status");

	/** Navigate to Setup → Users then click Load from file. */
	public void navigateToBulkUserLoad() {
		wait.until(ExpectedConditions.elementToBeClickable(setupMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(usersMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(loadFromFileBtn)).click();
		log.info("Navigated to Bulk Load Users page");
	}

	/** Click the Download CSV plain link. */
	public void downloadCsvTemplate() {
		wait.until(ExpectedConditions.elementToBeClickable(downloadCsvLink)).click();
		pause(2000);
		log.info("CSV template download initiated");
	}

	/** Upload a CSV file and trigger the start of upload. */
	public void uploadCsvFile(String absoluteFilePath) {
		wait.until(ExpectedConditions.presenceOfElementLocated(chooseFileInput)).sendKeys(absoluteFilePath);
		pause(1000);
		wait.until(ExpectedConditions.elementToBeClickable(startUploadBtn)).click();
		log.info("CSV file uploaded: {}", absoluteFilePath);
	}

	/** Click Finish Upload and wait for confirmation. */
	public void finishUpload() {
		wait.until(ExpectedConditions.elementToBeClickable(finishUploadBtn)).click();
		WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(LONG_WAIT));
		longWait.until(ExpectedConditions.visibilityOfElementLocated(uploadSuccessMsg));
		log.info("Upload finished");
	}

	/** After upload, search for the user and verify they exist. */
	public void searchAndVerifyUser(String login) {
		wait.until(ExpectedConditions.elementToBeClickable(setupMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(usersMenu)).click();
		WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(searchUserField));
		field.clear();
		field.sendKeys(login);
		wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(login)));
		log.info("User found after bulk upload: {}", login);
	}

	// =========================================================================
	// TC_SA_004 – DEACTIVATE USER WITH OPEN POs
	// =========================================================================

	private final By editUserBtn = By.cssSelector("a.edit-user, input[value='Edit'], a[href*='/edit']");
	private final By reassignUserField = By.cssSelector("input[id*='reassign'], input[id*='select_user_to_assign']");
	private final By selectChangesCheckboxes = By.cssSelector("input[id*='transfer_'], input.transfer-cb");
	private final By deactivateOnceCheckbox = By.id("deactivate_user_checkbox");
	private final By applyReassignBtn = By.cssSelector("input[value='Apply'], button[id*='apply']");
	private final By userStatusInactiveConfirm = By.cssSelector(".flash-notice, span[class*='inactive']");

	/** Search for a user and open their profile. */
	public void openUserProfile(String login) {
		wait.until(ExpectedConditions.elementToBeClickable(setupMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(usersMenu)).click();
		WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(searchUserField));
		field.clear();
		field.sendKeys(login);
		wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText(login))).click();
		log.info("Opened user profile: {}", login);
	}

	/** Click Edit on the user profile page. */
	public void clickEditUser() {
		wait.until(ExpectedConditions.elementToBeClickable(editUserBtn)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
		log.info("Clicked Edit on user profile");
	}

	/** Set the user status to Inactive (triggers reassignment popup). */
	public void setUserStatusInactive() {
		WebElement radio = wait.until(ExpectedConditions.presenceOfElementLocated(inactiveRadio));
		if (!radio.isSelected())
			radio.click();
		pause(1500);

		// Handle the alert/popup confirming Inactive
		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
			log.info("Alert accepted for inactive status");
		} catch (Exception ignored) {
			// Not a JS alert — Coupa sometimes uses an inline popup
			By okBtn = By.xpath("//button[normalize-space()='OK'] | //input[@value='OK']");
			try {
				wait.until(ExpectedConditions.elementToBeClickable(okBtn)).click();
			} catch (Exception e) {
				log.info("No alert/OK popup for inactive — continuing");
			}
		}
		log.info("User status set to Inactive");
	}

	/**
	 * On the reassignment page: select the replacement user and tick all transfer
	 * options.
	 */
	public void performUserReassignment(String replacementUser) {
		typeAutocomplete(reassignUserField, replacementUser);
		pause(2000);

		// Tick all "Select Changes to Make" checkboxes
		for (WebElement cb : driver.findElements(selectChangesCheckboxes)) {
			if (!cb.isSelected()) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", cb);
			}
		}

		// Make sure "Deactivate once updates are complete" is checked
		try {
			WebElement deactivateCb = driver.findElement(deactivateOnceCheckbox);
			if (!deactivateCb.isSelected())
				deactivateCb.click();
		} catch (Exception e) {
			log.warn("Deactivate checkbox not found — may already be selected by default");
		}

		wait.until(ExpectedConditions.elementToBeClickable(applyReassignBtn)).click();
		log.info("User reassignment applied to: {}", replacementUser);
	}

	/** Wait until the reassignment shows Done and verify the user is Inactive. */
	public void waitForReassignmentDone(String login) {
		WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(LONG_WAIT));
		longWait.until(ExpectedConditions
				.textToBePresentInElementLocated(By.cssSelector("#reassignment_status, .progress-status"), "Done"));
		log.info("Reassignment completed for: {}", login);
	}

	// =========================================================================
	// TC_SA_005 – BULK UPDATE NOTIFICATION PREFERENCES
	// =========================================================================

	private final By notificationsMenu = By.linkText("Notifications");
	private final By platformMenu = By.linkText("Platform");
	private final By actionDropdown = By.id("bulk_action");
	private final By deliveryMethodDropdown = By.cssSelector("select[id*='delivery_method']");
	private final By availableNotifications = By
			.cssSelector("select[id*='available_notifications'], #available_notifications");
	private final By selectNotifBtn = By.cssSelector("button[id*='select_notification'], input[value='Select']");
	private final By notifUserSearch = By.cssSelector("input[id*='user_search'], input[placeholder*='Search']");
	private final By applyNotifBtn = By.cssSelector("input[value='Apply'], button.apply-btn");
	private final By notifSuccessMsg = By.cssSelector(".flash-notice, .success-message");

	/** Navigate to Setup → Platform → Notifications. */
	public void navigateToNotifications() {
		wait.until(ExpectedConditions.elementToBeClickable(setupMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(platformMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(notificationsMenu)).click();
		log.info("Navigated to Notifications page");
	}

	/** Select action (Enable/Disable) and delivery method (Online/Email). */
	public void configureNotificationAction(String action, String deliveryMethod) {
		new Select(wait.until(ExpectedConditions.elementToBeClickable(actionDropdown))).selectByVisibleText(action);
		new Select(wait.until(ExpectedConditions.elementToBeClickable(deliveryMethodDropdown)))
				.selectByVisibleText(deliveryMethod);
		log.info("Notification action: {}, method: {}", action, deliveryMethod);
	}

	/** Select a notification from the Available list and move it to Selected. */
	public void selectNotification(String notificationName) {
		WebElement listEl = wait.until(ExpectedConditions.visibilityOfElementLocated(availableNotifications));
		for (WebElement opt : listEl.findElements(By.tagName("option"))) {
			if (opt.getText().trim().equalsIgnoreCase(notificationName)) {
				opt.click();
				break;
			}
		}
		wait.until(ExpectedConditions.elementToBeClickable(selectNotifBtn)).click();
		log.info("Notification selected: {}", notificationName);
	}

	/** Search for a user in the Select Users section. */
	public void addNotificationUser(String username) {
		typeAutocomplete(notifUserSearch, username);
		log.info("Notification user added: {}", username);
	}

	/** Click Apply and verify the success message. */
	public void applyNotificationSettings() {
		wait.until(ExpectedConditions.elementToBeClickable(applyNotifBtn)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(notifSuccessMsg));
		log.info("Notification settings applied");
	}

	// =========================================================================
	// TC_SA_006 – USER PERMISSION REASSIGNMENT
	// =========================================================================

	private final By userReassignmentsMenu = By.linkText("User Reassignments");
	private final By createReassignBtn = By.cssSelector("a.new-reassignment, a[href*='/user_reassignments/new']");
	private final By removeFromField = By.cssSelector("input[id*='remove_user'], input[placeholder*='remove']");
	private final By assignToField = By.cssSelector("input[id*='assign_user'], input[placeholder*='assign']");
	private final By section2Checkboxes = By.cssSelector("input[id*='option_'], input.reassign-option-cb");
	private final By deactivateAfterCb = By.cssSelector("input[id*='deactivate_after'], input.deactivate-cb");
	private final By applyReassignPermBtn = By.cssSelector("input[value='Apply'], button[class*='apply']");
	private final By reassignStatusIndicator = By.cssSelector("#reassignment_status, .status-indicator");

	/** Navigate to Setup → Platform → User Reassignments → Create. */
	public void navigateToUserReassignment() {
		wait.until(ExpectedConditions.elementToBeClickable(setupMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(platformMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(userReassignmentsMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(createReassignBtn)).click();
		log.info("Navigated to User Reassignment Create page");
	}

	/** Fill Remove from / Assign to fields, check Section 2 options, and apply. */
	public void createPermissionReassignment(String removeFrom, String assignTo, boolean deactivateAfter) {
		typeAutocomplete(removeFromField, removeFrom);
		pause(2000);
		typeAutocomplete(assignToField, assignTo);
		pause(2000);

		// Tick all Section 2 options
		for (WebElement cb : driver.findElements(section2Checkboxes)) {
			if (!cb.isSelected()) {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", cb);
			}
		}

		// Deactivate after reassignment
		try {
			WebElement deactivateCb = driver.findElement(deactivateAfterCb);
			if (deactivateAfter && !deactivateCb.isSelected()) {
				deactivateCb.click();
			} else if (!deactivateAfter && deactivateCb.isSelected()) {
				deactivateCb.click();
			}
		} catch (Exception e) {
			log.warn("Deactivate after checkbox not found");
		}

		wait.until(ExpectedConditions.elementToBeClickable(applyReassignPermBtn)).click();
		log.info("Permission reassignment applied — from: {}, to: {}", removeFrom, assignTo);
	}

	/** Wait for the reassignment job to show "Done". */
	public void waitForPermissionReassignmentDone() {
		WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(LONG_WAIT));
		longWait.until(ExpectedConditions.textToBePresentInElementLocated(reassignStatusIndicator, "Done"));
		log.info("Permission reassignment completed");
	}

	// =========================================================================
	// TC_SA_007 – CREATE CUSTOM HOME PAGE CONTENT
	// =========================================================================

	private final By homePageContentMenu = By.linkText("Home Page Content");
	private final By createContentBtn = By.cssSelector("a[href*='home_page_content/new'], .new-content-btn");
	private final By availableToRadio = By.xpath("//label[contains(.,'Only members of these content groups')]/input");
	private final By contentGroupSearch = By
			.cssSelector("input[id*='content_group_search'], input[placeholder*='content group']");
	private final By textAreaContent = By.cssSelector("textarea[id*='text'], #home_page_content_body");
	private final By publishBtn = By.cssSelector("input[value='Publish'], button[id*='publish']");

	/** Navigate to Setup → Platform → Home Page Content → Create. */
	public void navigateToHomePageContent() {
		wait.until(ExpectedConditions.elementToBeClickable(setupMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(platformMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(homePageContentMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(createContentBtn)).click();
		log.info("Navigated to Home Page Content Create");
	}

	/** Fill and publish a custom home page content block. */
	public void createHomePageContent(String contentGroupName, String bodyText) {
		wait.until(ExpectedConditions.elementToBeClickable(availableToRadio)).click();
		pause(1000);
		typeAutocomplete(contentGroupSearch, contentGroupName);
		pause(1000);
		type(textAreaContent, bodyText);
		wait.until(ExpectedConditions.elementToBeClickable(publishBtn)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(flashNotice));
		log.info("Home page content created and published for group: {}", contentGroupName);
	}

	// =========================================================================
	// TC_SA_008 – CREATE HOMEPAGE ANNOUNCEMENTS
	// =========================================================================

	private final By homePageMenu = By.linkText("Home Page");
	private final By announcementsLink = By.linkText("Announcements");
	private final By announcementCreateBtn = By.cssSelector("a[href*='announcements/new'], .new-announcement-btn");
	private final By subjectField = By.id("announcement_subject");
	private final By announcContentGroup = By.cssSelector("input[id*='content_group']");
	private final By startAtField = By.id("announcement_start_at");
	private final By endAtField = By.id("announcement_end_at");
	private final By messageArea = By.cssSelector("textarea[id*='message'], #announcement_body");
	private final By importantCb = By.id("announcement_important");
	private final By saveAnnouncBtn = By.cssSelector("input[type='submit'][value='Save'], button[type='submit']");
	private final By announcSuccessMsg = By.cssSelector(".flash-notice");

	/** Navigate to Setup → Home Page → Announcements → Create. */
	public void navigateToAnnouncements() {
		wait.until(ExpectedConditions.elementToBeClickable(setupMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(homePageMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(announcementsLink)).click();
		wait.until(ExpectedConditions.elementToBeClickable(announcementCreateBtn)).click();
		log.info("Navigated to Announcements Create page");
	}

	/**
	 * Fill all mandatory fields and save the announcement.
	 *
	 * @param subject       Subject text
	 * @param contentGroup  Content group name
	 * @param startAt       Start date (e.g. "04/15/2026")
	 * @param endAt         End date
	 * @param message       Announcement body text
	 * @param markImportant Whether to tick "Important Announcement"
	 */
	public void createAnnouncement(String subject, String contentGroup, String startAt, String endAt, String message,
			boolean markImportant) {
		type(subjectField, subject);
		typeAutocomplete(announcContentGroup, contentGroup);
		pause(500);

		type(startAtField, startAt);
		type(endAtField, endAt);
		type(messageArea, message);

		if (markImportant) {
			WebElement cb = driver.findElement(importantCb);
			if (!cb.isSelected())
				cb.click();
		}

		wait.until(ExpectedConditions.elementToBeClickable(saveAnnouncBtn)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(announcSuccessMsg));
		log.info("Announcement created: {}", subject);
	}

	/** Verify announcement is visible and rotating on the home page. */
	public String getAnnouncementSuccessMessage() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(announcSuccessMsg)).getText().trim();
	}

	// =========================================================================
	// TC_SA_009 – ROLE COMPARISON
	// =========================================================================

	private final By companySetupMenu = By.linkText("Company Setup");
	private final By rolesMenu = By.linkText("Roles");
	private final By compareToField = By.cssSelector("input[id*='compare_to'], input[placeholder*='Compare']");
	private final By compareBtn = By.cssSelector("input[value='Compare'], button[id*='compare']");
	private final By showRole1OnlyBtn = By.cssSelector("button[id*='show_role1'], input[id*='show_role1']");
	private final By showRole2OnlyBtn = By.cssSelector("button[id*='show_role2'], input[id*='show_role2']");
	private final By showBothRolesBtn = By.cssSelector("button[id*='show_both'], input[id*='show_both']");
	private final By compareToOriginalBtn = By
			.cssSelector("a[title*='Compare to original'], img[alt*='Compare to original']");

	/** Navigate to Setup → Company Setup → Roles. */
	public void navigateToRoles() {
		wait.until(ExpectedConditions.elementToBeClickable(setupMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(companySetupMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(rolesMenu)).click();
		log.info("Navigated to Roles page");
	}

	/** Open a role by its name link. */
	public void openRole(String roleName) {
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText(roleName))).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
		log.info("Opened role: {}", roleName);
	}

	/** Set the Compare to field and click Compare. */
	public void compareRoleTo(String otherRoleName) {
		typeAutocomplete(compareToField, otherRoleName);
		pause(1000);
		wait.until(ExpectedConditions.elementToBeClickable(compareBtn)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
		log.info("Comparing role to: {}", otherRoleName);
	}

	/** Toggle the "Show permissions only in Role 1" filter. */
	public void toggleShowRole1Permissions() {
		wait.until(ExpectedConditions.elementToBeClickable(showRole1OnlyBtn)).click();
		log.info("Show Role 1 only permissions toggled");
	}

	/** Toggle the "Show permissions only in Role 2" filter. */
	public void toggleShowRole2Permissions() {
		wait.until(ExpectedConditions.elementToBeClickable(showRole2OnlyBtn)).click();
		log.info("Show Role 2 only permissions toggled");
	}

	/** Toggle "Show permissions present in both roles". */
	public void toggleShowBothRoles() {
		wait.until(ExpectedConditions.elementToBeClickable(showBothRolesBtn)).click();
		log.info("Show permissions in both roles toggled");
	}

	/** Click "Compare to original role" in the Actions column. */
	public void compareToOriginalRole() {
		wait.until(ExpectedConditions.elementToBeClickable(compareToOriginalBtn)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
		log.info("Compare to original role clicked");
	}

	// =========================================================================
	// PRIVATE HELPERS
	// =========================================================================

	/**
	 * Types into an autocomplete field and waits for the dropdown to appear, then
	 * selects the first matching option.
	 */
	private void typeAutocomplete(By locator, String value) {
		type(locator, value);
		pause(1500);
		By firstOption = By.cssSelector("ul.ComboBox__results li:first-child, "
				+ "ul.ui-autocomplete li.ui-menu-item:first-child a, " + "ul[id$='_chosen_results'] li:first-child");
		try {
			wait.until(ExpectedConditions.elementToBeClickable(firstOption)).click();
		} catch (Exception e) {
			log.warn("No autocomplete dropdown found for value '{}' — value may already be entered", value);
		}
	}
	
	public void selectChartOfAccount(String chartOfAcc) {
		WebElement dropdown = driver.findElement(chartOfAccountsField);

		Select select = new Select(dropdown);
		select.selectByVisibleText(chartOfAcc);
		log.info("Chart of account selected succeffully !!");
	}
	
}
