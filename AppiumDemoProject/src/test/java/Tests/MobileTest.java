package Tests;

import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import Extent.ExtentTestManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import library.reusablelibrary;


@Listeners(Extent.ListenerClass.class)
public class MobileTest extends library.BaseClass{

	String strText = null;
	By lstcountryselect = By.id("com.androidsample.generalstore:id/spinnerCountry");
	By lstcountryview = By.xpath("//android.widget.FrameLayout/android.widget.ListView");
	By edtName =  By.id("com.androidsample.generalstore:id/nameField");
	By rdoMale = By.id("com.androidsample.generalstore:id/radioMale");
	By btnLetsShop = By.id("com.androidsample.generalstore:id/btnLetsShop");
	
	By txtWebsite = By.xpath("/html/body/div/h1");

	By imgproduct = By.id("com.androidsample.generalstore:id/productImage");
	By txtproductname = By.id("com.androidsample.generalstore:id/productName");
	By btnAddToCart = By.id("com.androidsample.generalstore:id/productAddCart");
	By txtCartText = By.id("com.androidsample.generalstore:id/counterText");
	By btnCartImage = By.id("com.androidsample.generalstore:id/appbar_btn_cart");


	reusablelibrary lib = new reusablelibrary();


	@BeforeClass
	public void before_class() {

		try { System.out.println("Inside before_class"); 
		lib.initiate();
		
		Reporter.log("Appium Server Started");	


		} catch (Exception e) {
			System.out.println("Appium Server not Launched. Pls resolve to proceed");
			System.out.println(e.getMessage());
			Assert.fail("Appium Server not Launched. Pls resolve to proceed");

		}

	}


	@Test(priority = 0)
	public void installapk () throws IOException {	
		ExtentTest test = ExtentTestManager.getTest();
		try {

			test.info("install apk test begins ...");
			URL url = new URL("http://127.0.0.1:4723/wd/hub"); 
			String strMode = lib.prop.getProperty("appLaunchMode");
			test.info("App launch mode is = "+strMode); 
			driver = new AppiumDriver (url, reusablelibrary.appCapabilitiesSetup());
			Assert.assertEquals(isDisplayed( lstcountryselect), true);
			test.pass(lstcountryselect.toString()+" display validation");


			//		  ExtentTest test = ExtentTestManager.getTest();
			test.info("letsShop step begins ..."); 
			//		  URL url = new  URL("http://127.0.0.1:4723/wd/hub"); 
			Assert.assertEquals(isDisplayed(lstcountryselect), true);
			test.pass(lstcountryselect.toString()+" display validation");
			click(lstcountryselect); 
			Assert.assertEquals(isDisplayed(lstcountryview), true);
			test.pass(lstcountryview.toString()+" display validation");
			
			strText = lib.prop.getProperty("Country"); //"Angola"; 			
			listScroll(strText, 0); 
			By lsttargetvalue = By.xpath("//android.widget.TextView[@text='" + strText + "']");
			click(lsttargetvalue); 


		} catch(Exception e) {
			e.printStackTrace();
			test.fail(e.getCause());
		}


	}

	@Test(priority=1, dependsOnMethods = {"installapk"})
	public void letsShop() throws IOException{
		ExtentTest test = ExtentTestManager.getTest();
		try {

			URL url = new URL("http://127.0.0.1:4723/wd/hub"); 

			// Chrome session steps 

			droiddriver = new  AndroidDriver (url, reusablelibrary.browserCapabilitiesSetup());
			droiddriver.get("https://example.com");
			//Assert.assertEquals(isDisplayed(txtWebsite), true,  txtWebsite.toString()+" display validation"); 
			strText =	  droiddriver.findElement(txtWebsite).getText();
			System.out.println("strText = "+strText); 
			Assert.assertEquals(strText, "Example Domain");
			test.pass(strText+ " is captured from chrome session");
			//		  droiddriver = new AndroidDriver (url, reusablelibrary.appCapabilitiesSetup());
			droiddriver.pressKey(new KeyEvent(AndroidKey.HOME)); 
			droiddriver.pressKey(new KeyEvent(AndroidKey.APP_SWITCH)); 
			droiddriver.pressKey(new KeyEvent(AndroidKey.DPAD_UP)); 
			droiddriver.pressKey(new KeyEvent(AndroidKey.DPAD_UP)); 
			droiddriver.pressKey(new KeyEvent(AndroidKey.DPAD_LEFT)); 
			droiddriver.pressKey(new KeyEvent(AndroidKey.ENTER));		 

			// Chrome session steps

			Assert.assertEquals(isDisplayed(edtName), true);
			click(edtName); 
			type(strText, edtName); 
			driver.navigate().back();
			click(rdoMale);
			test.info("Male radio button clicked");	
			click(btnLetsShop);
			test.info("letsshop button clicked");	


		} catch (Exception e) {
			e.printStackTrace();
			test.fail(e.getCause());
		}

	}


	@Test(priority = 2, dependsOnMethods = {"letsShop"})

	public void chooseProduct() throws IOException {
		ExtentTest test = ExtentTestManager.getTest();
		try {

			Assert.assertEquals(isDisplayed(imgproduct), true);		  
			test.log(Status.PASS, "Product Images Screen is loaded");
			addToCart("com.androidsample.generalstore:id/rvProductList", lib.prop.getProperty("ProductName"));			 
			Assert.assertEquals(isDisplayed(txtCartText), true);
			strText = driver.findElement(txtCartText).getText();
			Assert.assertEquals(strText, "1");
			test.log(Status.PASS, "Product Count in cart is : "+strText);
			click(btnCartImage);
			waitforIsDisplayed(txtproductname);
			strText = driver.findElement(txtproductname).getText();
			Assert.assertEquals(strText, "PG 4");

		} catch(Exception e) {
			e.printStackTrace();
			test.fail(e.getCause());
		}

	}


	@AfterClass(alwaysRun = true)
	public void teardown() throws Exception {
		System.out.println("Inside Tear Down");
		driver.quit();
		droiddriver.quit();
		lib.stopServer();

	}

}
