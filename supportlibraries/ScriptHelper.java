package supportlibraries;

import org.openqa.selenium.WebDriver;

import com.cognizant.framework.CraftDataTable;


/**
 * Wrapper class for common framework objects, to be used across the entire test case and dependent libraries
 * @author Cognizant
 */
public class ScriptHelper
{
	private final CraftDataTable dataTable;
	private final SeleniumReport report;
	private final WebDriver driver;
	
	/**
	 * Constructor to initialize all the objects wrapped by the {@link ScriptHelper} class
	 * @param dataTable The {@link CraftDataTable} object
	 * @param report The {@link SeleniumReport} object
	 * @param driver The {@link WebDriver} object
	 */
	public ScriptHelper(CraftDataTable dataTable, SeleniumReport report, WebDriver driver)
	{
		this.dataTable = dataTable;
		this.report = report;
		this.driver = driver;
	}
	
	/**
	 * Function to get the {@link CraftDataTable} object
	 * @return The {@link CraftDataTable} object
	 */
	public CraftDataTable getDataTable()
	{
		return dataTable;
	}
	
	/**
	 * Function to get the {@link SeleniumReport} object
	 * @return The {@link SeleniumReport} object
	 */
	public SeleniumReport getReport()
	{
		return report;
	}
	
	/**
	 * Function to get the {@link WebDriver} object
	 * @return The {@link WebDriver} object
	 */
	public WebDriver getDriver()
	{
		return driver;
	}
}