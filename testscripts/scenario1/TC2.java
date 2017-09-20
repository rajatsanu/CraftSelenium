package testscripts.scenario1;

import org.testng.annotations.Test;

import supportlibraries.Browser;
import supportlibraries.DriverScript;
import supportlibraries.TestCase;


/**
 * Test for login with invalid user credentials
 * @author Cognizant
 */
public class TC2 extends TestCase
{
	@Test
	public void runTC2()
	{
		testParameters.setCurrentTestDescription("Verify main menu option of portal");
		testParameters.setBrowser(Browser.Chrome);
		
		driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();
	}
}