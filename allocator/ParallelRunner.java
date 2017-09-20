package allocator;

import java.util.Date;

import supportlibraries.DriverScript;
import supportlibraries.SeleniumReport;
import supportlibraries.SeleniumTestParameters;

import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Util;


/**
 * Class to facilitate parallel execution of test scripts
 * @author Cognizant
 */
public class ParallelRunner implements Runnable
{
	private final SeleniumTestParameters testParameters;
	private final SeleniumReport summaryReport;
	
	
	/**
	 * Constructor to initialize the details of the test case to be executed
	 * @param testParameters The {@link SeleniumTestParameters} object (passed from the {@link Allocator})
	 * @param summaryReport The {@link SeleniumReport} object (passed from the {@link Allocator})
	 */
	public ParallelRunner(SeleniumTestParameters testParameters, SeleniumReport summaryReport)
	{
		super();
		
		this.testParameters = testParameters;
		this.summaryReport = summaryReport;
	}
	
	@Override
	public void run()
	{
		Date startTime = Util.getCurrentTime();
		String testStatus = invokeTestScript();
		Date endTime = Util.getCurrentTime();
		String executionTime = Util.getTimeDifference(startTime, endTime);
		summaryReport.updateResultSummary(testParameters.getCurrentScenario(),
									testParameters.getCurrentTestcase(),
									testParameters.getCurrentTestDescription(),
									executionTime, testStatus);
	}
	
	private String invokeTestScript()
	{
		String testStatus;
		FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
		
		if(frameworkParameters.getStopExecution()) {
			testStatus = "Aborted";
		} else {
			DriverScript driverScript = new DriverScript(this.testParameters);
			driverScript.setTestExecutedInUnitTestFramework(false);
			driverScript.driveTestExecution();
			testStatus = driverScript.getTestStatus();
		}
		
		return testStatus;
	}
}