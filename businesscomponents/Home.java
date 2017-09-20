package businesscomponents;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pageObject.HomePO;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Status;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class Home extends ReusableLibrary {

	HomePO homePO;

	public Home(ScriptHelper scriptHelper) {
		super(scriptHelper);
		homePO = new HomePO(driver);
	}

	public void invokeApplication() {
		driver.get(properties.getProperty("ApplicationUrl"));
		driver.manage().window().maximize();
		report.updateTestLog(
				"Launch Application",
				"Invoke the application under test : <i>"
						+ properties.getProperty("ApplicationUrl") + "</i>",
				Status.PASS);

	}

	public void signupProcess() {
		WebDriverWait wait = new WebDriverWait(driver, 20);

		homePO.clickLoginBtn();
				
		wait.until(ExpectedConditions.elementToBeClickable(By
				.id("reg_username")));

		String emailId = dataTable.getData("General_Data", "Emailid")
				+ Utility.generateRandomNumber(0, 9999);
		ApplicationGlobalVariable.emailId = emailId;
		emailId = emailId + "@mailinator.com";
		homePO.enterSignupEmailId(emailId);
		homePO.enterNewPasswd(dataTable.getData("General_Data", "Password"));
		homePO.entereEnterPasswd(dataTable.getData("General_Data", "Password"));
		homePO.clickAddAccount();
		report.updateTestLog("Sign up", "Enter sign up data : <br>"
				+ "Username - <i>" + emailId + "</i><br>Password - <i>"
				+ dataTable.getData("General_Data", "Password") + "</i>",
				Status.DONE);

		wait.until(ExpectedConditions.elementToBeClickable(By
				.xpath("//a[@class='current-open']/span")));

		/*
		 * try { Thread.sleep(3000); } catch (InterruptedException e) {
		 * e.printStackTrace(); }
		 */

		if (homePO.getTextLblUserId().replace("Hi ", "")
				.equalsIgnoreCase(ApplicationGlobalVariable.emailId)) {
			report.updateTestLog("Verify Login",
					"Succesfully singed up with username <i>"
							+ ApplicationGlobalVariable.emailId + "</i>",
					Status.PASS);
		} else {
			frameworkParameters.setStopExecution(true);
			throw new FrameworkException("Verify Login",
					"Sign up process is failed. Excepted : "
							+ ApplicationGlobalVariable.emailId + ", Actual : "
							+ homePO.getTextLblUserId().replace("Hi ", ""));
		}

	}

	public void verifyMainMenu() {
		ArrayList<String> arr = homePO.getListMainMenu();

		if (arr.contains("Home")) {
			report.updateTestLog("Home Menu",
					"<center>Home Menu is present</center>", Status.PASS);
		} else {
			report.updateTestLog("Home Menu",
					"<center>Home Menu is not present</center>", Status.FAIL);
		}

		if (arr.contains("Authors")) {
			report.updateTestLog("Authors Menu",
					"<center>Authors Menu is present</center>", Status.PASS);
		} else {
			report.updateTestLog("Authors Menu",
					"<center>Authors Menu is not present</center>", Status.FAIL);
		}

		if (arr.contains("Publishers")) {
			report.updateTestLog("Publishers Menu",
					"<center>Publishers Menu is present</center>", Status.PASS);
		} else {
			report.updateTestLog("Publishers Menu",
					"<center>Publishers Menu is not present</center>",
					Status.FAIL);
		}

		if (arr.contains("Indian Writing")) {
			report.updateTestLog("Indian Writing Menu",
					"<center>Indian Writing Menu is present</center>",
					Status.PASS);
		} else {
			report.updateTestLog("Indian Writing Menu",
					"<center>Indian Writing Menu is not present</center>",
					Status.FAIL);
		}

		if (arr.contains("Fiction")) {
			report.updateTestLog("Fiction Menu",
					"<center>Fiction Menu is present</center>", Status.PASS);
		} else {
			report.updateTestLog("Fiction Menu",
					"<center>Fiction Menu is not present</center>", Status.FAIL);
		}

		if (arr.contains("Non-Fiction")) {
			report.updateTestLog("Non-Fiction Menu",
					"<center>Non-Fiction Menu is present</center>", Status.PASS);
		} else {
			report.updateTestLog("Non-Fiction Menu",
					"<center>Non-Fiction Menu is not present</center>",
					Status.FAIL);
		}

		if (arr.contains("Children Books")) {
			report.updateTestLog("Children Books Menu",
					"<center>Children Books Menu is present</center>",
					Status.PASS);
		} else {
			report.updateTestLog("Children Books Menu",
					"<center>Children Books Menu is not present</center>",
					Status.FAIL);
		}

		if (arr.contains("Competitive Exam Books")) {
			report.updateTestLog("Competitive Exam Books Menu",
					"<center>Competitive Exam Books Menu is present</center>",
					Status.PASS);
		} else {
			report.updateTestLog(
					"Competitive Exam Books Menu",
					"<center>Competitive Exam Books Menu is not present</center>",
					Status.FAIL);
		}

	}

	public void verifyVerticalMenu() {
		ArrayList<String> arr = homePO.getListVerticalMenu();

		if (arr.contains("Arts & Photography")) {
			report.updateTestLog("Arts & Photography Menu",
					"<center>Arts & Photography Menu is present</center>",
					Status.PASS);
		} else {
			report.updateTestLog("Arts & Photography Menu",
					"<center>Arts & Photography Menu is not present</center>",
					Status.FAIL);
		}

		if (arr.contains("Biography")) {
			report.updateTestLog("Biography Menu",
					"<center>Biography Menu is present</center>", Status.PASS);
		} else {
			report.updateTestLog("Biography Menu",
					"<center>Biography Menu is not present</center>",
					Status.FAIL);
		}

		if (arr.contains("Business & Investing")) {
			report.updateTestLog("Business & Investing Menu",
					"<center>Business & Investing Menu is present</center>",
					Status.PASS);
		} else {
			report.updateTestLog(
					"Business & Investing Menu",
					"<center>Business & Investing Menu is not present</center>",
					Status.FAIL);
		}

		if (arr.contains("Children Books")) {
			report.updateTestLog("Children Books Menu",
					"<center>Children Books Menu is present</center>",
					Status.PASS);
		} else {
			report.updateTestLog("Children Books Menu",
					"<center>Children Books Menu is not present</center>",
					Status.FAIL);
		}

		if (arr.contains("College Text & Reference")) {
			report.updateTestLog(
					"College Text & Reference Menu",
					"<center>College Text & Reference Menu is present</center>",
					Status.PASS);
		} else {
			report.updateTestLog(
					"College Text & Reference Menu",
					"<center>College Text & Reference Menu is not present</center>",
					Status.FAIL);
		}

		if (arr.contains("Computers & Internet")) {
			report.updateTestLog("Computers & Internet Menu",
					"<center>Computers & Internet Menu is present</center>",
					Status.PASS);
		} else {
			report.updateTestLog(
					"Computers & Internet Menu",
					"<center>Computers & Internet Menu is not present</center>",
					Status.FAIL);
		}

		if (arr.contains("Cooking, Food & Wine")) {
			report.updateTestLog("Cooking, Food & Wine Menu",
					"<center>Cooking, Food & Wine Menu is present</center>",
					Status.PASS);
		} else {
			report.updateTestLog(
					"Cooking, Food & Wine Menu",
					"<center>Cooking, Food & Wine Menu is not present</center>",
					Status.FAIL);
		}

		if (arr.contains("Educational and Professional")) {
			report.updateTestLog(
					"Educational and Professional Menu",
					"<center>Educational and Professional Menu is present</center>",
					Status.PASS);
		} else {
			report.updateTestLog(
					"Educational and Professional Menu",
					"<center>Educational and Professional Menu is not present</center>",
					Status.FAIL);
		}

		if (arr.contains("Entertainment")) {
			report.updateTestLog("Entertainment Menu",
					"<center>Entertainment Menu is present</center>",
					Status.PASS);
		} else {
			report.updateTestLog("Entertainment Menu",
					"<center>Entertainment Menu is not present</center>",
					Status.FAIL);
		}

		if (arr.contains("Entrance Exams Preparation")) {
			report.updateTestLog(
					"Entrance Exams Preparation Menu",
					"<center>Entrance Exams Preparation Menu is present</center>",
					Status.PASS);
		} else {
			report.updateTestLog(
					"Entrance Exams Preparation Menu",
					"<center>Entrance Exams Preparation Menu is not present</center>",
					Status.FAIL);
		}
	}

	public void logout() {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(By
				.xpath("//a[@class='current-open']/span")));
		
		homePO.clickLogoutBtn();

		if (homePO.getTextLblUserId().equalsIgnoreCase("My Account")) {
			report.updateTestLog("Logout", "Logout successfully", Status.PASS);
		} else {
			frameworkParameters.setStopExecution(true);
			throw new FrameworkException("Verify Login",
					"Logout unsuccessfully");
		}

	}

}
