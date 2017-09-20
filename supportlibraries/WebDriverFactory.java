package supportlibraries;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.WebClient;

import org.openqa.selenium.ie.InternetExplorerDriver;

import com.opera.core.systems.OperaDriver;

import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.remote.*;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Settings;


/**
 * Factory class for creating the {@link WebDriver} object as required
 * @author Cognizant
 */
public class WebDriverFactory
{
	private static Properties properties;
	
	private WebDriverFactory()
	{
		// To prevent external instantiation of this class
	}
	
	
	/**
	 * Function to return the appropriate {@link WebDriver} object based on the parameters passed
	 * @param browser The {@link Browser} to be used for the test execution
	 * @return The corresponding {@link WebDriver} object
	 */
	public static WebDriver getDriver(Browser browser)
	{
		WebDriver driver = null;
		properties = Settings.getInstance();
		boolean proxyRequired =
				Boolean.parseBoolean(properties.getProperty("ProxyRequired"));
		
		switch(browser) {
		case Chrome:
			// Takes the system proxy settings automatically
			
			System.setProperty("webdriver.chrome.driver",
									properties.getProperty("ChromeDriverPath"));
			driver = new ChromeDriver();
			break;
			
		case Firefox:
			// Takes the system proxy settings automatically
			
			driver = new FirefoxDriver();
			break;
			
		case HtmlUnit:
			// Does not take the system proxy settings automatically!
			
			driver = new HtmlUnitDriver();
			
			if (proxyRequired) {
				boolean proxyAuthenticationRequired =
						Boolean.parseBoolean(properties.getProperty("ProxyAuthenticationRequired"));
				if(proxyAuthenticationRequired) {
					// NTLM authentication for proxy supported
					
					driver = new HtmlUnitDriver() {
					@Override
					protected WebClient modifyWebClient(WebClient client) {
						DefaultCredentialsProvider credentialsProvider = new DefaultCredentialsProvider();
						credentialsProvider.addNTLMCredentials(properties.getProperty("Username"),
																properties.getProperty("Password"),
																properties.getProperty("ProxyHost"),
																Integer.parseInt(properties.getProperty("ProxyPort")),
																"", properties.getProperty("Domain"));
						client.setCredentialsProvider(credentialsProvider);
						return client;
						}
					};
				}
				
				((HtmlUnitDriver) driver).setProxy(properties.getProperty("ProxyHost"),
											Integer.parseInt(properties.getProperty("ProxyPort")));
			}
			
			break;
			
		case InternetExplorer:
			// Takes the system proxy settings automatically
			
			System.setProperty("webdriver.ie.driver",
									properties.getProperty("InternetExplorerDriverPath"));
			driver = new InternetExplorerDriver();
			break;
			
		case Opera:
			// Does not take the system proxy settings automatically!
			// NTLM authentication for proxy NOT supported
			
			if (proxyRequired) {
				DesiredCapabilities desiredCapabilities = getProxyCapabilities();
				driver = new OperaDriver(desiredCapabilities);
			} else {
				driver = new OperaDriver();
			}
			
			break;
			
		case Safari:
			// Takes the system proxy settings automatically
			
			driver = new SafariDriver();
			break;
			
		default:
			throw new FrameworkException("Unhandled browser!");
		}
		
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		
		return driver;
	}
	
	private static DesiredCapabilities getProxyCapabilities()
	{
		Proxy proxy = new Proxy();
		proxy.setProxyType(ProxyType.MANUAL);
		
		properties = Settings.getInstance();
		String proxyUrl = properties.getProperty("ProxyHost") + ":" +
									properties.getProperty("ProxyPort");
		
		proxy.setHttpProxy(proxyUrl);
		proxy.setFtpProxy(proxyUrl);
		proxy.setSslProxy(proxyUrl);
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability(CapabilityType.PROXY, proxy);
		
		return desiredCapabilities;
	}
	
	/**
	 * Function to return the appropriate {@link WebDriver} object based on the parameters passed
	 * @param browser The {@link Browser} to be used for the test execution
	 * @param remoteUrl The URL of the remote machine to be used for the test execution
	 * @return The corresponding {@link WebDriver} object
	 */
	public static WebDriver getDriver(Browser browser, String remoteUrl)
	{
		return getDriver(browser, null, null, remoteUrl);
	}
	
	/**
	 * Function to return the appropriate {@link WebDriver} object based on the parameters passed
	 * @param browser The {@link Browser} to be used for the test execution
	 * @param browserVersion The browser version to be used for the test execution
	 * @param platform The {@link Platform} to be used for the test execution
	 * @param remoteUrl The URL of the remote machine to be used for the test execution
	 * @return The corresponding {@link WebDriver} object
	 */
	public static WebDriver getDriver(Browser browser, String browserVersion,
												Platform platform, String remoteUrl)
	{
		// For running RemoteWebDriver tests in Chrome and IE:
		// The ChromeDriver and IEDriver executables needs to be in the PATH of the remote machine
		// To set the executable path manually, use:
		// java -Dwebdriver.chrome.driver=/path/to/driver -jar selenium-server-standalone.jar
		// java -Dwebdriver.ie.driver=/path/to/driver -jar selenium-server-standalone.jar
		
		properties = Settings.getInstance();
		boolean proxyRequired =
				Boolean.parseBoolean(properties.getProperty("ProxyRequired"));
		
		DesiredCapabilities desiredCapabilities = null;
		if ((browser.equals(Browser.HtmlUnit) || browser.equals(Browser.Opera))
																&& proxyRequired) {
			desiredCapabilities = getProxyCapabilities();
		} else {
			desiredCapabilities = new DesiredCapabilities();
		}
		
		desiredCapabilities.setBrowserName(browser.getValue());
		
		if (browserVersion != null) {
			desiredCapabilities.setVersion(browserVersion);
		}
		if (platform != null) {
			desiredCapabilities.setPlatform(platform);
		}
		
		desiredCapabilities.setJavascriptEnabled(true);	// Pre-requisite for remote execution
		
		URL url;
		try {
			url = new URL(remoteUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new FrameworkException("The specified remote URL is malformed");
		}
		
		return new RemoteWebDriver(url, desiredCapabilities);
	}
}