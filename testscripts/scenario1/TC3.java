package testscripts.scenario1;

import org.testng.annotations.Test;

import supportlibraries.DriverScript;
import supportlibraries.TestCase;


/**
 * Test for register new user and login using the registered user
 * @author Cognizant
 */
public class TC3 extends TestCase
{
	@Test
	public void runTC3()
	{
		testParameters.setCurrentTestDescription("Test for register new user and login using the registered user");
		
		driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();
	}
}