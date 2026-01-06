package testCases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AffiliatePage;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import testBase.BaseClass;
import utilities.RetryAnalyzer;   // adjust package if needed

public class TC06_AddAffiliate extends BaseClass {

	private static final Logger log = LogManager.getLogger(TC06_AddAffiliate.class);

	@Test(groups = { "regression" }, retryAnalyzer = utilities.RetryAnalyzer.class)
	void testAddAffiliate() {

		log.info("========== TC06_AddAffiliate START ==========");

		try {
			// Home Page → Login navigation
			log.debug("Initializing HomePage");
			HomePage hp = new HomePage(getDriver());

			log.info("Clicking My Account");
			hp.clickMyAccount();

			log.info("Navigating to Login page");
			hp.goToLogin();

			// Login
			log.debug("Initializing LoginPage");
			LoginPage lp = new LoginPage(getDriver());

			log.info("Entering login credentials (password hidden)");
			lp.setEmail("sid@cloudberry.services");
			lp.setPwd("Test123");
			lp.clickLogin();
			log.info("Login submitted successfully");

			// Affiliate Page actions
			log.debug("Initializing AffiliatePage");
			AffiliatePage ap = new AffiliatePage(getDriver());

			log.info("Navigating to Affiliate form");
			ap.navigateToAffiliateForm();

			log.debug("Filling Affiliate details");
			ap.fillAffiliateDetails(
					"CloudBerry",
					"cloudberry.services",
					"123456",
					"Shadab Siddiqui"
			);

			boolean status = ap.isAffiliateAdded();
			log.debug("Affiliate added status: {}", status);

			// Assertion with logging + screenshot
			try {
				Assert.assertTrue(status, "Affiliate details not added successfully.");
				log.info("Affiliate added successfully");
			} catch (AssertionError ae) {
				log.error("Assertion FAILED: Affiliate was not added", ae);

				String screenshotPath = captureScreen("TC06_AddAffiliate");
				log.info("Screenshot captured at: {}", screenshotPath);

				// Re-throw to mark test failed and trigger RetryAnalyzer
				throw ae;
			}

		} catch (Exception e) {
			log.error("Exception occurred during Add Affiliate test execution", e);

			String screenshotPath = captureScreen("TC06_AddAffiliate_Exception");
			log.info("Screenshot captured at: {}", screenshotPath);

			Assert.fail("Test failed due to exception: " + e.getMessage());
		} finally {
			log.info("========== TC06_AddAffiliate END ==========");
		}
	}
}
