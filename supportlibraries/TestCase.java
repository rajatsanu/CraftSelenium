package supportlibraries;

import java.io.FileNotFoundException;
import java.util.Date;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Util;

import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;


/**
 * Abstract base class for all the test cases to be automated
 * @author Cognizant
 */
public abstract class TestCase extends ResultSummaryManager
{
	/**
	 * The {@link SeleniumTestParameters} object to be used to specify the test parameters
	 */
	protected SeleniumTestParameters testParameters;
	/**
	 * The {@link DriverScript} object to be used to execute the required test case
	 */
	protected DriverScript driverScript;
	
	private Date startTime, endTime;
	
	
	@BeforeSuite
	public void suiteSetup(ITestContext testContext)
	{
		setRelativePath();
		initializeTestBatch();
		
		int nThreads;
		if (testContext.getSuite().getParallel().equalsIgnoreCase("false")) {
			nThreads = 1;
		} else {
			nThreads = testContext.getCurrentXmlTest().getThreadCount();
		}
		initializeSummaryReport(testContext.getSuite().getName(), nThreads);
		
		try {
			setupErrorLog();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while setting up the Error log!");
		}
	}
	
	@BeforeMethod
	public void testMethodSetup()
	{
		if(frameworkParameters.getStopExecution()) {
			suiteTearDown();
			
			throw new SkipException("Aborting all subsequent tests!");
		} else {
			startTime = Util.getCurrentTime();
			
			String currentScenario =
					capitalizeFirstLetter(this.getClass().getPackage().getName().substring(12));
			String currentTestcase = this.getClass().getSimpleName();
			testParameters = new SeleniumTestParameters(currentScenario, currentTestcase);
		}
	}
	
	private String capitalizeFirstLetter(String myString)
	{
		StringBuilder stringBuilder = new StringBuilder(myString);
		stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
		return stringBuilder.toString();
	}
	
	@AfterMethod
	public void testMethodTearDown()
	{
		String testStatus = driverScript.getTestStatus();
		endTime = Util.getCurrentTime();
		String executionTime = Util.getTimeDifference(startTime, endTime);
		summaryReport.updateResultSummary(testParameters.getCurrentScenario(),
									testParameters.getCurrentTestcase(),
									testParameters.getCurrentTestDescription(),
									executionTime, testStatus);
	}
	
	@AfterSuite
	public void suiteTearDown()
	{
		wrapUp();
		//launchResultSummary();
	}
}