package businesscomponents;

import org.openqa.selenium.WebDriver;

import com.cognizant.framework.Status;

import pageObject.MailinatorPO;
import supportlibraries.Browser;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import supportlibraries.WebDriverFactory;

public class Malinator extends ReusableLibrary {

	MailinatorPO malinatorPO;
	WebDriver driver2 = null;

	public Malinator(ScriptHelper scriptHelper) {
		super(scriptHelper);
		malinatorPO = new MailinatorPO(ApplicationGlobalVariable.driver2);
	}

	public void loginPersonnelMail() {
		driver2 = WebDriverFactory.getDriver(Browser.Chrome);
		ApplicationGlobalVariable.driver2 = driver2;
		malinatorPO = new MailinatorPO(driver2);
		driver2.get("https://www.mailinator.com/");
		driver2.manage().window().maximize();
		malinatorPO.enterInputMailId(ApplicationGlobalVariable.emailId);
		malinatorPO.clickBtnGo();
		report.updateTestLog("Launch Personnel Email", "Personnel Email Id <i>"
				+ ApplicationGlobalVariable.emailId
				+ "@mailinator.com</i> is logged on successfully", Status.DONE);
	}

	public void verifySignUpMail() {
		malinatorPO = new MailinatorPO(ApplicationGlobalVariable.driver2);
		malinatorPO.displayReceivedMailRecepient();
		malinatorPO.displayReceivedMailSubject();
		report.updateTestLog("Verify Sign up mail",
				"Sign up mail is received in personnel email id", Status.PASS);
	}

	public void closePersonnelMail() {
		ApplicationGlobalVariable.driver2.close();
		ApplicationGlobalVariable.driver2.quit();
		ApplicationGlobalVariable.driver2 = null ;
		report.updateTestLog("Close Personnel Email",
				"Personnel Email id is closed successfully", Status.DONE);
	}

}
