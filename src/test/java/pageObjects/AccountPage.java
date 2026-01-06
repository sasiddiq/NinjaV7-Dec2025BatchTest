package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AccountPage {
	
	WebDriver driver;
	//constructor

	public AccountPage(WebDriver driver) 
	{
		this.driver=driver;
		//PageFactory.initElements(driver, this);
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//h1[normalize-space()='My Account']")
	WebElement confirmationText_MyAccount;
	
	@FindBy(xpath = "//li[@class='list-inline-item']//i[@class='fa-solid fa-caret-down']")
	WebElement dropDown_MyAccount;

	@FindBy(xpath = "//a[@class='dropdown-item'][normalize-space()='Logout']")
	WebElement link_Logout;


	public WebElement getMyAccountConfirmation() {
		return confirmationText_MyAccount;
	}
	
	public void clickMyAccountDropDown() {
		dropDown_MyAccount.click();
	}

	public void clickLogout() {
		link_Logout.click();
	}
	

}
