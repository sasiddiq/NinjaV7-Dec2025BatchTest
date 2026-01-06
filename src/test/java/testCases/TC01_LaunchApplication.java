package testCases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import testBase.BaseClass;
import utilities.RetryAnalyzer; // <-- make sure this matches your package

public class TC01_LaunchApplication extends BaseClass {

	private static final Logger log = LogManager.getLogger(TC01_LaunchApplication.class);

	@Test(groups = { "sanity", "regression" }, retryAnalyzer = utilities.RetryAnalyzer.class)
	void testLaunchApplication() {

		log.info("========== TC01_LaunchApplication START ==========");

		try {
			log.debug("Initializing HomePage object using ThreadLocal WebDriver");
			HomePage hp = new HomePage(getDriver()); // keeping since you had it; useful for page validation
			log.debug("HomePage object created: {}", hp.getClass().getSimpleName());

			log.info("Fetching page title");
			String actualTitle = getDriver().getTitle();
			log.debug("Actual Title: [{}]", actualTitle);

			String expectedTitle = "Your store of fun";
			log.debug("Expected Title: [{}]", expectedTitle);

			// Assertion with try-catch logging
			try {
				Assert.assertEquals(actualTitle, expectedTitle, "Page title mismatch!");
				log.info("Assertion PASSED: Title matched successfully");
			} catch (AssertionError ae) {
				log.error("Assertion FAILED: Expected [{}] but found [{}]", expectedTitle, actualTitle, ae);

				// Optional: capture screenshot if you want (uses your BaseClass method)
				String screenshotPath = captureScreen("TC01_LaunchApplication");
				log.info("Screenshot captured at: {}", screenshotPath);

				// Re-throw so TestNG marks test as failed (and RetryAnalyzer can retry)
				throw ae;
			}

		} catch (Exception e) {
			log.error("Exception occurred in testLaunchApplication()", e);

			// Optional: screenshot on unexpected exception too
			String screenshotPath = captureScreen("TC01_LaunchApplication_Exception");
			log.info("Screenshot captured at: {}", screenshotPath);

			// Fail the test explicitly (RetryAnalyzer will retry)
			Assert.fail("Test failed due to exception: " + e.getMessage());
		} finally {
			log.info("========== TC01_LaunchApplication END ==========");
		}
	}
}
