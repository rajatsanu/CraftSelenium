package supportlibraries;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Report;
import com.cognizant.framework.ReportSettings;
import com.cognizant.framework.ReportTheme;


/**
 * Class to extend the reporting features of the framework
 * @author Cognizant
 */
public class SeleniumReport extends Report
{
	private WebDriver driver;
	/**
	 * Function to set the {@link WebDriver} object
	 * @param driver The {@link WebDriver} object
	 */
	public void setDriver(WebDriver driver)
	{
		this.driver = driver;
	}
	
	
	/**
	 * Constructor to initialize the Report
	 * @param reportSettings The {@link ReportSettings} object
	 * @param reportTheme The {@link ReportTheme} object
	 */
	public SeleniumReport(ReportSettings reportSettings, ReportTheme reportTheme)
	{
		super(reportSettings, reportTheme);
	}
	
	@Override
	protected void takeScreenshot(String screenshotPath)
	{
		if (driver == null) {
			throw new FrameworkException("Report.driver is not initialized!");
		}
		
		if (driver.getClass().getSimpleName().equals("HtmlUnitDriver") || 
			driver.getClass().getGenericSuperclass().toString().equals("class org.openqa.selenium.htmlunit.HtmlUnitDriver")) {
			return;	// Screenshots not supported in headless mode
		}
		
		File scrFile;
		if (driver.getClass().getSimpleName().equals("RemoteWebDriver")) {
			Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
			if (capabilities.getBrowserName().equals("htmlunit")) {
				return;	// Screenshots not supported in headless mode
			}
			WebDriver augmentedDriver = new Augmenter().augment(driver);
	        scrFile = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
		} else {
			scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		}
		
		try {
			FileUtils.copyFile(scrFile, new File(screenshotPath), true);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while writing screenshot to file");
		}
	}
}