package allocator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openqa.selenium.Platform;

import supportlibraries.Browser;
import supportlibraries.ResultSummaryManager;
import supportlibraries.SeleniumTestParameters;
import businesscomponents.ApplicationGlobalVariable;

import com.cognizant.framework.ExcelDataAccess;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.IterationOptions;


/**
 * Class to manage the batch execution of test scripts within the framework
 * @author Cognizant
 */
public class Allocator extends ResultSummaryManager
{
	private List<SeleniumTestParameters> testInstancesToRun;
	
	
	public static void main(String[] args)
	{
		Allocator allocator = new Allocator();
		allocator.driveBatchExecution();
	}
	
	private void driveBatchExecution()
	{
		setRelativePath();
		initializeTestBatch();
		
		int nThreads = Integer.parseInt(properties.getProperty("NumberOfThreads"));
		initializeSummaryReport(properties.getProperty("RunConfiguration"), nThreads);
		
		try {
			setupErrorLog();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while setting up the Error log!");
		}
		
		executeTestBatch();
		
		wrapUp();
		launchResultSummary();
	}
	
	@Override
	protected void initializeTestBatch()
	{
		super.initializeTestBatch();
		
		testInstancesToRun = getRunInfo(properties.getProperty("RunConfiguration"));
	}
	
	/**
	 * @param sheetName
	 * @return
	 */
	/**
	 * @param sheetName
	 * @return
	 */
	private List<SeleniumTestParameters> getRunInfo(String sheetName)
	{
		ExcelDataAccess runManagerAccess = new ExcelDataAccess(frameworkParameters.getRelativePath(), "Run Manager");			
		runManagerAccess.setDatasheetName(sheetName);
		
		int nTestInstances = runManagerAccess.getLastRowNum();
		List<SeleniumTestParameters> testInstancesToRun = new ArrayList<SeleniumTestParameters>();
		
		for (int currentTestInstance = 1; currentTestInstance <= nTestInstances; currentTestInstance++) {
			String executeFlag = runManagerAccess.getValue(currentTestInstance, "Execute");
			
			if (executeFlag.equalsIgnoreCase("Yes")) {
				String currentScenario = runManagerAccess.getValue(currentTestInstance, "TestScenario");
				String currentTestcase = runManagerAccess.getValue(currentTestInstance, "TestCase");
				SeleniumTestParameters testParameters =
						new SeleniumTestParameters(currentScenario, currentTestcase);
				
				testParameters.setCurrentTestDescription(runManagerAccess.getValue(currentTestInstance, "Description"));
				
				String iterationMode = runManagerAccess.getValue(currentTestInstance, "IterationMode");
				if (!iterationMode.equals("")) {
					testParameters.setIterationMode(IterationOptions.valueOf(iterationMode));
				} else {
					testParameters.setIterationMode(IterationOptions.RunAllIterations);
				}
				
				String startIteration = runManagerAccess.getValue(currentTestInstance, "StartIteration");
				if (!startIteration.equals("")) {
					testParameters.setStartIteration(Integer.parseInt(startIteration));
				}
				String endIteration = runManagerAccess.getValue(currentTestInstance, "EndIteration");
				if (!endIteration.equals("")) {
					testParameters.setEndIteration(Integer.parseInt(endIteration));
				}
				
				String browser = runManagerAccess.getValue(currentTestInstance, "Browser");
				ApplicationGlobalVariable.browser = browser ;
				if (!browser.equals("")) {
					testParameters.setBrowser(Browser.valueOf(browser));
				} else {
					testParameters.setBrowser(Browser.valueOf(properties.getProperty("DefaultBrowser")));
				}
				String browserVersion = runManagerAccess.getValue(currentTestInstance, "BrowserVersion");
				if (!browserVersion.equals("")) {
					testParameters.setBrowserVersion(browserVersion);
				}
				String platform = runManagerAccess.getValue(currentTestInstance, "Platform");
				if (!platform.equals("")) {
					testParameters.setPlatform(Platform.valueOf(platform));
				} else {
					testParameters.setPlatform(Platform.valueOf(properties.getProperty("DefaultPlatform")));
				}
				
				testInstancesToRun.add(testParameters);
			}
		}
		
		return testInstancesToRun;
	}
	
	private void executeTestBatch()
	{
		int nThreads = Integer.parseInt(properties.getProperty("NumberOfThreads"));
		ExecutorService parallelExecutor = Executors.newFixedThreadPool(nThreads);
		
		for (int currentTestInstance = 0; currentTestInstance < testInstancesToRun.size() ; currentTestInstance++ ) {
			ParallelRunner testRunner =	new ParallelRunner(testInstancesToRun.get(currentTestInstance), summaryReport);
			parallelExecutor.execute(testRunner);
			
			if(frameworkParameters.getStopExecution()) {
				break;
			}
		}
		
		parallelExecutor.shutdown();
		while(!parallelExecutor.isTerminated()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}