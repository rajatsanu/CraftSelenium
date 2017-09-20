package allocator;

import java.io.File;
import java.util.Properties;

import org.openqa.selenium.Platform;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

import supportlibraries.*;

import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.IterationOptions;
import com.cognizant.framework.Settings;
import com.cognizant.framework.TimeStamp;
import com.cognizant.framework.Util;


public class QcTestRunner
{
	private static final FrameworkParameters frameworkParameters =
												FrameworkParameters.getInstance();
	private static SeleniumTestParameters testParameters;
	private static String testStatus = "Failed";	// Guilty until proven innocent!	
	
	private QcTestRunner()
	{
		// To prevent external instantiation of this class
	}
	
    public static void main(String[] args)
	{
		if(args.length < 4) {
			System.out.println("\nError: Insufficient parameters!" +
								"\nUsage: java allocator.QcTestRunner " +
								"<testset-name> <time-stamp>" +
								"<scenario-name> <test-name> <test-description*> " +
								"<iteration-mode*> <start-iteration*> <end-iteration*> " +
								"<browser*> <browser-version*> <platform*> " +
								"\n\n * - Optional (can be skipped by specifying empty quotes)");
			return;
		}
		
		updateStatusInRegistry();	// Initialize the test result
		setRelativePath();
		TimeStamp.setPath(args[0] + Util.getFileSeparator() + args[1]);
		initializeTestParameters(args);
		driveExecutionFromQc();
		updateStatusInRegistry();
	}
    
    private static void updateStatusInRegistry()
	{
		Advapi32Util.registryCreateKey(WinReg.HKEY_CURRENT_USER,
										"SOFTWARE\\Cognizant\\Framework\\QC Integration");
		
        Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER,
        		"SOFTWARE\\Cognizant\\Framework\\QC Integration", "TestStatus", testStatus);
	}
    
    private static void setRelativePath()
	{
		String relativePath = new File(System.getProperty("user.dir")).getAbsolutePath();
		if(relativePath.contains("allocator")) {
			relativePath = new File(System.getProperty("user.dir")).getParent();
		}
		frameworkParameters.setRelativePath(relativePath);
	}
    
    private static void initializeTestParameters(String[] args)
	{
    	Properties properties = Settings.getInstance();
    	
    	testParameters = new SeleniumTestParameters(args[2], args[3]);
		
		if (args.length >= 5 && !args[4].equalsIgnoreCase("SKIP")) {
			testParameters.setCurrentTestDescription(args[4]);
		}
		
		if (args.length >= 6 && !args[5].equalsIgnoreCase("SKIP")) {
			testParameters.setIterationMode(IterationOptions.valueOf(args[5]));
		} else {
			testParameters.setIterationMode(IterationOptions.RunAllIterations);
		}
		
		if (args.length >= 7 && !args[6].equalsIgnoreCase("SKIP")) {
			testParameters.setStartIteration(Integer.parseInt(args[6]));
		}
		if (args.length >= 8 && !args[7].equalsIgnoreCase("SKIP")) {
			testParameters.setEndIteration(Integer.parseInt(args[7]));
		}
		
		if (args.length >= 9 && !args[8].equalsIgnoreCase("SKIP")) {
			testParameters.setBrowser(Browser.valueOf(args[8]));
		} else {
			testParameters.setBrowser(Browser.valueOf(properties.getProperty("DefaultBrowser")));
		}
		if (args.length >= 10 && !args[9].equalsIgnoreCase("SKIP")) {
			testParameters.setBrowserVersion(args[9]);
		}
		if (args.length >= 11 && !args[10].equalsIgnoreCase("SKIP")) {
			testParameters.setPlatform(Platform.valueOf(args[10]));
		} else {
			testParameters.setPlatform(Platform.valueOf(properties.getProperty("DefaultPlatform")));
		}
	}
    
    private static void driveExecutionFromQc()
    {
    	DriverScript driverScript = new DriverScript(testParameters);
		driverScript.setTestExecutedInUnitTestFramework(false);
		driverScript.setLinkScreenshotsToTestLog(false);
		driverScript.driveTestExecution();
		testStatus = driverScript.getTestStatus();
    }
}