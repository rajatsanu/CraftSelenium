package businesscomponents;

import java.util.Random;
import java.util.Set;

import org.openqa.selenium.WebDriver;

public class Utility {

	
	public static int generateRandomNumber(int lowerLimit, int higherLimit){
		Random r = new Random();
		int Low = lowerLimit;
		int High = higherLimit;
		return r.nextInt(High-Low) + Low;
	}
	
	public static void jumpInNewWindow(WebDriver driver){
		
		String currentWindow = driver.getWindowHandle();
		Set<String> allWindowHandles = driver.getWindowHandles();

		for(int i=0; i<allWindowHandles.size(); i++){
			if(!allWindowHandles.toArray()[i].equals(currentWindow)){
				driver.switchTo().window((String) allWindowHandles.toArray()[i]);
			}
		}
		
	}
	
	
	
	
}
