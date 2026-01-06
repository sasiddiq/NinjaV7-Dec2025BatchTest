package testCases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.CategoryPage;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.ProductPage;
import testBase.BaseClass;
import utilities.RetryAnalyzer; // <-- adjust package if needed

public class TC05_AddToWishList extends BaseClass {

	private static final Logger log = LogManager.getLogger(TC05_AddToWishList.class);

	@Test(groups = { "regression" }, retryAnalyzer = utilities.RetryAnalyzer.class)
	void testAddToWishList() {

		log.info("========== TC05_AddToWishList START ==========");

		try {
			// Home Page -> Login Navigation
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
			log.info("Login submitted");

			// Category Page -> Select Product
			log.debug("Initializing CategoryPage");
			CategoryPage cp = new CategoryPage(getDriver());

			log.info("Navigating to Laptops & Notebooks");
			cp.clickLaptopsAndNotebooks();

			log.info("Clicking Show All");
			cp.clickShowAll();

			log.debug("Waiting briefly for products to load");
			Thread.sleep(500); // ideally replace with explicit wait

			log.info("Selecting HP product");
			cp.selectHPProduct();

			// Product Page -> Add to Wishlist
			log.debug("Initializing ProductPage");
			ProductPage pp = new ProductPage(getDriver());

			log.info("Adding product to wishlist");
			pp.addToWishlist();

			boolean status = pp.isSuccessMessageDisplayed();
			log.debug("Wishlist success message displayed: {}", status);

			// Assertion with logging + screenshot
			try {
				Assert.assertTrue(status, "Wishlist message not shown.");
				log.info("Wishlist updated successfully");
			} catch (AssertionError ae) {
				log.error("Assertion FAILED: Wishlist message was not displayed", ae);

				String screenshotPath = captureScreen("TC05_AddToWishList");
				log.info("Screenshot captured at: {}", screenshotPath);

				// rethrow so TestNG marks failed and RetryAnalyzer triggers
				throw ae;
			}

		} catch (Exception e) {
			log.error("Exception occurred during AddToWishList execution", e);

			String screenshotPath = captureScreen("TC05_AddToWishList_Exception");
			log.info("Screenshot captured at: {}", screenshotPath);

			Assert.fail("Test failed due to exception: " + e.getMessage());
		} finally {
			log.info("========== TC05_AddToWishList END ==========");
		}
	}
}
