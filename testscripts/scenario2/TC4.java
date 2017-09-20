package testscripts.scenario2;

import org.testng.annotations.Test;

import com.cognizant.framework.IterationOptions;

import supportlibraries.DriverScript;
import supportlibraries.TestCase;


/**
 * Test for book flight tickets and verify booking
 * @author Cognizant
 */
public class TC4 extends TestCase
{
	@Test
	public void runTC4()
	{
		testParameters.setCurrentTestDescription("Test for book flight tickets and verify booking");
		testParameters.setIterationMode(IterationOptions.RunOneIterationOnly);
		//testParameters.setBrowser(Browser.InternetExplorer);
		
		driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();
	}
}