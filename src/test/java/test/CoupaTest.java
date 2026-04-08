package test;

import org.testng.annotations.Test;

import base.BasePage;
import pages.E2E.EndToEnd2.LoginPage;

public class CoupaTest extends BasePage {

    @Test
    public void testCoupaLogin() throws InterruptedException {

        setup();

        driver.get("https://accorhotels-test.coupahost.com/sessions/new");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("TEMP-AELHA", "Clavier@123");

        Thread.sleep(5000);

        tearDown();
    }
}
