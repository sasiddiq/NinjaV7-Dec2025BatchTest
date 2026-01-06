package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BaseClass {

	// Thread-safe WebDriver
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	// Logger instance
	protected static final Logger log = LogManager.getLogger(BaseClass.class);

	public Properties p;

	public static WebDriver getDriver() {
		return driver.get();
	}

	@BeforeClass(groups = { "sanity", "regression", "datadriven" })
	@Parameters({ "os", "browser" })
	public void openApp(String os, String br) {

		log.info("===== Test Execution Started =====");
		log.debug("OS Parameter: {}", os);
		log.debug("Browser Parameter: {}", br);

		try {
			// Load config file
			FileReader file = new FileReader(".//src//test//resources//config.properties");
			p = new Properties();
			p.load(file);
			log.debug("Config properties file loaded successfully");

			WebDriver localDriver = null;
			
			if (p.getProperty("execution_env").equalsIgnoreCase("remote")) {
				DesiredCapabilities capabilities = new DesiredCapabilities();

				// os
				if (os.equalsIgnoreCase("windows")) {
					capabilities.setPlatform(Platform.WIN11);
				} else if (os.equalsIgnoreCase("mac")) {
					capabilities.setPlatform(Platform.MAC);
				} else {
					System.out.println("No matching os");
					return;
				}

				String gridURL = "http://localhost:4444/wd/hub"; // Update if needed
				//String gridURL = "http://192.168.86.78:4444/wd/hub"; // this will also work
				

				switch (br.toLowerCase()) {
				case "chrome":
					ChromeOptions chromeOptions = new ChromeOptions();
					localDriver = new RemoteWebDriver(URI.create(gridURL).toURL(), chromeOptions.merge(capabilities));
					break;

				case "firefox":
					FirefoxOptions firefoxOptions = new FirefoxOptions();
					localDriver = new RemoteWebDriver(URI.create(gridURL).toURL(), firefoxOptions.merge(capabilities));
					break;

				case "edge":
					EdgeOptions edgeOptions = new EdgeOptions();
					localDriver = new RemoteWebDriver(URI.create(gridURL).toURL(), edgeOptions.merge(capabilities));
					break;

				default:
					log.error("No matching browser found: {}", br);
					return;
				}

			}
			
			
			
			
			
			if (p.getProperty("execution_env").equalsIgnoreCase("local")) {

			// Browser selection
			switch (br.toLowerCase()) {
			case "chrome":
				log.info("Launching Chrome browser");
				localDriver = new ChromeDriver();
				break;

			case "edge":
				log.info("Launching Edge browser");
				localDriver = new EdgeDriver();
				break;

			case "firefox":
				log.info("Launching Firefox browser");
				localDriver = new FirefoxDriver();
				break;

			default:
				log.error("Invalid browser name provided: {}", br);
				throw new RuntimeException("No matching browser found");
			}
			}

			// Assign driver to ThreadLocal
			driver.set(localDriver);
			log.debug("WebDriver instance set to ThreadLocal");

			// Browser setup
			getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
			getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
			getDriver().manage().window().maximize();

			log.info("Browser window maximized and timeouts configured");

			// Launch application
			String appURL = p.getProperty("appURL");
			log.info("Navigating to URL: {}", appURL);
			getDriver().get(appURL);

		} catch (Exception e) {
			log.error("Exception occurred while launching application", e);
			throw new RuntimeException(e);
		}
	}

	@AfterClass(groups = { "sanity", "regression", "datadriven" })
	public void closeApp() {

		log.info("Closing browser session");

		try {
			if (getDriver() != null) {
				getDriver().quit();
				log.info("Browser closed successfully");
			}
		} catch (Exception e) {
			log.error("Exception occurred while closing browser", e);
		} finally {
			driver.remove();
			log.debug("ThreadLocal WebDriver removed");
			log.info("===== Test Execution Finished =====");
		}
	}

	public String captureScreen(String tname) {

		log.debug("Capturing screenshot for test: {}", tname);
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

		try {
			File sourceFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);

			String targetFilePath = System.getProperty("user.dir") +
					"\\screenshots\\" + tname + "_" + timeStamp + ".png";

			File targetFile = new File(targetFilePath);
			sourceFile.renameTo(targetFile);

			log.info("Screenshot captured: {}", targetFilePath);
			return targetFilePath;

		} catch (Exception e) {
			log.error("Failed to capture screenshot for test: " + tname, e);
			return null;
		}
	}
}
