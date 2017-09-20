package testscripts.scenario1;

import org.testng.annotations.Test;

import com.cognizant.framework.IterationOptions;

import supportlibraries.Browser;
import supportlibraries.DriverScript;
import supportlibraries.TestCase;


/**
 * Test for login with valid user credentials
 * @author Cognizant
 */
public class TC1 extends TestCase
{
	@Test
	public void runTC1()
	{
		testParameters.setCurrentTestDescription("Test for sign up in Book But India Portal");
		testParameters.setIterationMode(IterationOptions.RunOneIterationOnly);
		testParameters.setBrowser(Browser.Chrome);
		
		driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();
	}
}