package library;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.touch.offset.PointOption;


public class BaseClass {
	
	protected static AppiumDriver driver;
	protected AndroidDriver droiddriver;
	protected AppiumServiceBuilder builder;
	protected AppiumDriverLocalService service;
	
	
	
	
	public static String screenShot() throws IOException { 
		String screenShotPath = capture(driver, "Snap_"+reusablelibrary.setUniqueID());
		return screenShotPath; }
	
//	public AppiumDriver Base(AppiumDriver driver) { return this.driver = driver; }
	
	public AndroidDriver Base(AndroidDriver droiddriver) { return this.droiddriver = droiddriver; }
	
	public AppiumServiceBuilder Base(AppiumServiceBuilder builder) { return this.builder = builder; }
	
	public AppiumDriverLocalService Base(AppiumDriverLocalService service) { return this.service = service; }
	
	public void visit(String url) { driver.get(url); }
	
	
	public MobileElement find (By locator) throws Exception {
		
//		 if(droiddriver!=null) {
//			return (MobileElement) droiddriver.findElement(locator);
//		}

		return (MobileElement) driver.findElement(locator); 
		
	}
	
	public void click(By locator) throws Exception {find(locator).click();}
	
	public void type(String strInputText, By locator) throws Exception {
		find(locator).setValue(strInputText); }
	
	public void listScroll(String strInputText, int objIndex)  {
	driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance("+ objIndex+"))"
			+ ".scrollIntoView(new UiSelector().textMatches(\"" + strInputText + "\").instance(0));"));
	}
	
	public void addToCart(String resourceId, String strInputText)  {
//		driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance("+ objIndex+"))"
//				+ ".scrollIntoView(new UiSelector().textMatches(\"" + strInputText + "\").instance(0));"));
		try {
		MobileElement me = (MobileElement) driver.findElement(MobileBy.AndroidUIAutomator(
				"new UiScrollable(new UiSelector().scrollable(true).resourceId(\""+ resourceId +"\"))"
	+ ".getChildByText("+ "new UiSelector().className(\"android.widget.TextView\"), \""+ strInputText +"\")"));
//				+ ".scrollIntoView(new UiSelector().textMatches(\"" + strInputText + "\").instance(0));"));	
		
		MobileElement parent = (MobileElement) driver.findElement(By.id(resourceId));
		me = parent.findElement(By.xpath("//android.widget.RelativeLayout[2]/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.TextView[2]"));
		me.click();
		
		
		}
		catch(Exception e) {
			
		}
	}
	
	public void swiptToBottom()
	{
		  TouchAction swipe = new TouchAction(driver)
	              .press(PointOption.point(78,420))	              
	              .moveTo(PointOption.point(540,1900))
	              .release()
	              .perform();
		
		}
	
	public boolean isDisplayed(By locator) throws Exception {
		
		try {
			boolean flag = false;
			flag = waitforIsDisplayed(locator, 10);
			flag = find(locator).isDisplayed();
			return flag;
		}
		catch(org.openqa.selenium.NoSuchElementException e) { // org.openqa.selenium.NoSuchElementException
			return false;
		}
	}
	
	public boolean waitforIsDisplayed(By locator, Integer... timeout) {
		try {
			
			waitFor (ExpectedConditions.presenceOfAllElementsLocatedBy(locator),
					(timeout.length >0 ? timeout[0] : null));
			waitFor (ExpectedConditions.visibilityOfAllElementsLocatedBy(locator),
					(timeout.length >0 ? timeout[0] : null));		 
			
						
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	private void waitFor (org.openqa.selenium.support.ui.ExpectedCondition<List<WebElement>> condition, Integer timeout) {
		timeout = timeout != null ? timeout : 5;
//		if(droiddriver!=null) {
//			WebDriverWait wait = new WebDriverWait(droiddriver, timeout);
//			wait.until(condition);
//		}
//		else {
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			wait.until(condition);
//		}
		
	}
	
	
	
	public void switchToContext(String context ) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		HashMap<String,String> params = new HashMap();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}
	public String getCurrentContextHandle() {         
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		String context =  (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
		return context;
	}
	public List<String> getContextHandles(AndroidDriver driver) {         
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		List<String> contexts =  (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
		return contexts;
	}
	
	public static String capture (AppiumDriver driver,String screenShotName) throws IOException {
		String dest;
		dest = System.getProperty("user.dir")+"\\test-output\\ErrorScreenshots\\"+screenShotName+".png";
//		if(prop.getProperty("ReportPath").trim().length() > 0) {
//			dest = prop.getProperty("ReportPath") + "\\ErrorScreenshots\\"+screenShotName+".png";
//		}
//		else {
//			
//		}
		System.out.println("Snap dest:="+dest);
		System.out.println("Snap driver is null ? :="+driver.getSessionId());
		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			File destination = new File(dest);
			FileUtils.copyFile(source, destination); 
		}
		catch( Exception e) {
			System.out.println("object null or closed and no snap to capture");			
			//dest = System.getProperty("user.dir")+"\\Screenshots\\DriverError.PNG";
		}

		return dest;
	}
	
	
}
