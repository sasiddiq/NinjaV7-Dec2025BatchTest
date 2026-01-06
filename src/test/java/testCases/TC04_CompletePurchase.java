package testCases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.CategoryPage;
import pageObjects.CheckoutPage;
import pageObjects.ConfirmationPage;
import pageObjects.LoginPage;
import pageObjects.ProductPage;
import testBase.BaseClass;
import utilities.RetryAnalyzer;   // adjust package if needed

public class TC04_CompletePurchase extends BaseClass {

	private static final Logger log = LogManager.getLogger(TC04_CompletePurchase.class);

	@Test(
		groups = { "sanity", "regression" },
		retryAnalyzer = utilities.RetryAnalyzer.class
	)
	public void testCompletePurchase() {

		log.info("========== TC04_CompletePurchase START ==========");

		try {
			// Category Page actions
			log.debug("Initializing CategoryPage");
			CategoryPage cp = new CategoryPage(getDriver());

			log.info("Navigating to Laptops & Notebooks");
			cp.clickLaptopsAndNotebooks();

			log.info("Clicking Show All products");
			cp.clickShowAll();

			log.debug("Waiting for products to load");
			Thread.sleep(500); // ideally replace with explicit wait

			log.info("Selecting HP product");
			cp.selectHPProduct();

			// Product Page actions
			log.debug("Initializing ProductPage");
			ProductPage pp = new ProductPage(getDriver());

			log.info("Setting delivery date");
			pp.setDeliveryDate();

			log.info("Adding product to cart");
			pp.clickAddToCart();

			log.info("Proceeding to checkout");
			pp.clickCheckout();

			// Checkout Page actions
			log.debug("Initializing CheckoutPage");
			CheckoutPage cop = new CheckoutPage(getDriver());

			log.info("Clicking Login on Checkout page");
			cop.clickLogin();

			// Login Page actions
			log.debug("Initializing LoginPage");
			LoginPage lp = new LoginPage(getDriver());

			log.debug("Entering login credentials (password hidden)");
			lp.setEmail("sid@cloudberry.services");
			lp.setPwd("Test123");
			lp.clickLogin();

			log.info("Completing checkout steps");
			cop.completeCheckout();

			// Confirmation Page validation
			log.debug("Initializing ConfirmationPage");
			ConfirmationPage confirmationPage = new ConfirmationPage(getDriver());

			boolean orderPlaced = confirmationPage.isOrderPlaced();
			log.debug("Order placed status: {}", orderPlaced);

			// Assertion with logging
			try {
				Assert.assertTrue(orderPlaced, "Order placement failed!");
				log.info("Order placed successfully");

			} catch (AssertionError ae) {
				log.error("Assertion FAILED: Order was not placed successfully", ae);

				String screenshotPath = captureScreen("TC04_CompletePurchase");
				log.info("Screenshot captured at: {}", screenshotPath);

				// Re-throw to mark failure and trigger RetryAnalyzer
				throw ae;
			}

		} catch (Exception e) {
			log.error("Exception occurred during complete purchase flow", e);

			String screenshotPath = captureScreen("TC04_CompletePurchase_Exception");
			log.info("Screenshot captured at: {}", screenshotPath);

			Assert.fail("Test failed due to exception: " + e.getMessage());
		} finally {
			log.info("========== TC04_CompletePurchase END ==========");
		}
	}
}
