package businesscomponents;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.cognizant.framework.Status;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class Home2 extends ReusableLibrary {

	public Home2(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}

	public void verifyMainLogo() {

		if (driver.findElement(By.xpath("//img[@title = 'Buy Books India']"))
				.isDisplayed()) {
			report.updateTestLog("Verify logo",
					"Logo is verified successfully", Status.PASS);
		} else {
			report.updateTestLog("Verify logo",
					"Logo is not verified successfully", Status.FAIL);
		}

		driver.findElement(By.xpath("//img[@title = 'Buy Books India']"))
				.click();
		String mainlogourl = driver.getCurrentUrl();
		if (mainlogourl.equalsIgnoreCase("http://www.buybooksindia.com/")) {
			report.updateTestLog("Verify main logo",
					"Main logo click is navigating to correct URL", Status.PASS);
		} else {
			report.updateTestLog("Verify main logo",
					"Main logo click is not navigating to correct URL",
					Status.FAIL);
		}
	}

	public void verifyDefaultSearchOption() {

		if (driver.findElement(By.id("select2-searchby-container")).getText()
				.equalsIgnoreCase("Book")) {
			report.updateTestLog("Default search value",
					"Default value is Book as expected", Status.PASS);
		} else {
			report.updateTestLog("Default search value",
					"Default value is not Book", Status.FAIL);
		}

	}

	public void verifySearchOptions() {

		Select select = new Select(driver.findElement(By.id("searchby")));
		List<WebElement> options = select.getOptions();

		for (WebElement e : options) {
			if (e.getText().equalsIgnoreCase("Book")
					|| e.getText().equalsIgnoreCase("Author")
					|| e.getText().equalsIgnoreCase("Subject")) {
				report.updateTestLog("Search Option", "Search option contain "
						+ e.getText(), Status.PASS);
			} else {
				report.updateTestLog(
						"Search Option",
						"Search option " + e.getText() + " is not as expected ",
						Status.FAIL);

			}
		}

	}

}
