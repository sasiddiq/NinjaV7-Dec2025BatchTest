package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ConfirmationPage extends BasePage {

    public ConfirmationPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//h1[normalize-space()='Your order has been placed!']")
    WebElement orderSuccessHeader;

    public boolean isOrderPlaced() {
        return orderSuccessHeader.isDisplayed();
    }
}
