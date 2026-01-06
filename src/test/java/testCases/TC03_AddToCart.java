package testCases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.CategoryPage;
import pageObjects.ProductPage;
import testBase.BaseClass;
import utilities.RetryAnalyzer;   // <-- adjust package if needed

public class TC03_AddToCart extends BaseClass {

	private static final Logger log = LogManager.getLogger(TC03_AddToCart.class);

	@Test(
		groups = { "sanity", "regression" },
		retryAnalyzer = utilities.RetryAnalyzer.class
	)
	public void testAddToCart() {

		log.info("========== TC03_AddToCart START ==========");

		try {
			// Category Page actions
			log.debug("Initializing CategoryPage");
			CategoryPage cp = new CategoryPage(getDriver());

			log.info("Clicking on Laptops & Notebooks category");
			cp.clickLaptopsAndNotebooks();

			log.info("Clicking on Show All products");
			cp.clickShowAll();

			log.debug("Waiting briefly for products to load");
			Thread.sleep(500); // ideally replace with explicit wait

			log.info("Selecting HP product");
			cp.selectHPProduct();

			// Product Page actions
			log.debug("Initializing ProductPage");
			ProductPage pp = new ProductPage(getDriver());

			log.info("Setting delivery date");
			pp.setDeliveryDate();

			log.info("Clicking Add to Cart button");
			pp.clickAddToCart();

			boolean status = pp.isSuccessMessageDisplayed();
			log.debug("Add to Cart success message displayed: {}", status);

			// Assertion with logging
			try {
				Assert.assertTrue(status, "Add to Cart Failed!");
				log.info("Product added to cart successfully");

			} catch (AssertionError ae) {
				log.error("Assertion FAILED: Product was not added to cart", ae);

				String screenshotPath = captureScreen("TC03_AddToCart");
				log.info("Screenshot captured at: {}", screenshotPath);

				// Re-throw to mark test failed and trigger RetryAnalyzer
				throw ae;
			}

		} catch (Exception e) {
			log.error("Exception occurred during Add to Cart test execution", e);

			String screenshotPath = captureScreen("TC03_AddToCart_Exception");
			log.info("Screenshot captured at: {}", screenshotPath);

			Assert.fail("Test failed due to exception: " + e.getMessage());
		} finally {
			log.info("========== TC03_AddToCart END ==========");
		}
	}
}
