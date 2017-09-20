package pageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MailinatorPO {
	
	WebDriver driver ;
	
	public MailinatorPO(WebDriver driver){
		this.driver = driver ;
		PageFactory.initElements(driver, this);
	}

	
	
	
	@FindBy(id="inboxfield")
	private WebElement inputMailId;
		
	@FindBy(xpath="//button[@class='btn btn-dark']")
	private WebElement btnGo;
	
	
	@FindBy(xpath="//div[contains(text(),'info@buybooksindia.com')]")
	private WebElement receivedMailRecepient;
	
	@FindBy(xpath="//div[contains(text(),'Registration Confirmation - Buy Books India')]")
	private WebElement receivedMailSubject;

	
	public void enterInputMailId(String emailId){
		inputMailId.sendKeys(emailId);
	}
	
	public void clickBtnGo(){
		btnGo.click();
	}
	
	public void displayReceivedMailRecepient(){
		receivedMailRecepient.isDisplayed();
	}
	
	public void displayReceivedMailSubject(){
		receivedMailSubject.isDisplayed();
	}
	
	
}
