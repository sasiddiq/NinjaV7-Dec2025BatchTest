package testCases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountPage;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import testBase.BaseClass;
import utilities.DataProviders;
import utilities.RetryAnalyzer;   // <-- adjust package if needed

public class TC02_Login extends BaseClass {

	private static final Logger log = LogManager.getLogger(TC02_Login.class);

	@Test(
		groups = { "sanity", "regression", "datadriven" },
		dataProvider = "LoginData",
		dataProviderClass = DataProviders.class,
		retryAnalyzer = utilities.RetryAnalyzer.class
	)
	void testLogin(String email, String pwd) {

		log.info("========== TC02_Login START ==========");
		log.debug("Test data received -> Email: [{}], Password: [PROTECTED]", email);

		try {
			// Home Page actions
			log.debug("Initializing HomePage");
			HomePage hp = new HomePage(getDriver());

			log.info("Clicking on My Account");
			hp.clickMyAccount();

			log.info("Navigating to Login page");
			hp.goToLogin();

			// Login Page actions
			log.debug("Initializing LoginPage");
			LoginPage lp = new LoginPage(getDriver());

			log.debug("Entering email");
			lp.setEmail(email);

			log.debug("Entering password");
			lp.setPwd(pwd);

			log.info("Clicking Login button");
			lp.clickLogin();

			// Account Page validation
			log.debug("Initializing AccountPage");
			AccountPage ap = new AccountPage(getDriver());

			boolean status = ap.getMyAccountConfirmation().isDisplayed();
			log.debug("My Account confirmation displayed: {}", status);

			// Assertion block with logging
			try {
				Assert.assertTrue(status, "Login failed - My Account not displayed");
				log.info("Login successful for user: {}", email);

				// Logout steps
				log.info("Performing logout");
				ap.clickMyAccountDropDown();
				ap.clickLogout();
				log.info("Logout successful");

			} catch (AssertionError ae) {
				log.error("Assertion FAILED for login with email: {}", email, ae);

				String screenshotPath = captureScreen("TC02_Login_" + email.replace("@", "_"));
				log.info("Screenshot captured at: {}", screenshotPath);

				// Re-throw to mark test failed and trigger RetryAnalyzer
				throw ae;
			}

		} catch (Exception e) {
			log.error("Exception occurred during login test execution", e);

			String screenshotPath = captureScreen("TC02_Login_Exception");
			log.info("Screenshot captured at: {}", screenshotPath);

			Assert.fail("Test failed due to exception: " + e.getMessage());
		} finally {
			log.info("========== TC02_Login END ==========");
		}
	}
}
