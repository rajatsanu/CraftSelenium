package pageObject;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePO {
	
	
	public HomePO(WebDriver driver){
		PageFactory.initElements(driver, this);
	}

	
	@FindBy(xpath="//a[@title='Login']")
	private WebElement loginBtn;
	
	@FindBy(id="reg_username")
	private WebElement signUpEmailId;
	
	@FindBy(id="reg_pwd")
	private WebElement newPasswd;
	
	@FindBy(id="reg_conpwd")
	private WebElement reEnterPasswd;
	
	@FindBy(id="addCustomer_0")
	private WebElement btnAddAccount;
	
	@FindBy(xpath="//a[@class='current-open']/span")
	private WebElement lblUserId;
	
	@FindBy(xpath="//ul[@class='nav navbar-nav']/li")
	private List<WebElement> listMainMenu;
	
	@FindBy(xpath="//ul[@class='vertical-menu-list']/li")
	private List<WebElement> listVerticalMenu;
	
	@FindBy(xpath="//a[@title='Logout']")
	private WebElement btnLogout;
	
	
	
	
	public void clickLoginBtn(){
		loginBtn.click();
	}
	
	public void enterSignupEmailId(String emailId){
		signUpEmailId.sendKeys(emailId);
	}
	
	public void enterNewPasswd(String passwd){
		newPasswd.sendKeys(passwd);
	}
	
	public void entereEnterPasswd(String passwd){
		reEnterPasswd.sendKeys(passwd);
	}
	
	public void clickAddAccount(){
		btnAddAccount.click();
	}
	
	public String getTextLblUserId(){
		return lblUserId.getText();
	}
	
	public ArrayList<String> getListMainMenu(){
		ArrayList<String> arr = new ArrayList<String>();
		for(WebElement element : listMainMenu){
			arr.add(element.getText());
		}
		return arr ;
	}
	
	public ArrayList<String> getListVerticalMenu(){
		ArrayList<String> arr = new ArrayList<String>();
		for(WebElement element : listVerticalMenu){
			arr.add(element.getText());
		}
		return arr ;
	}
	
	public void clickLogoutBtn(){
		btnLogout.click();
	}
}
