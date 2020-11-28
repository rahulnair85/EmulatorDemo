package library;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.remote.MobileCapabilityType;

public class reusablelibrary {
	// Section for reusable variables
	public static Properties prop;
	//	public String apkFilepath = "D:\\APK s\\General-Store.apk";
	public int iobjTimeOut = 3;
	BaseClass bc = new BaseClass();
	
//	public static AndroidDriver<MobileElement> droidDriver;
//	public static AppiumDriver <MobileElement> appiumDriver;
	
	//public AppiumDriverLocalService service = AppiumDriverLocalService.buildDefaultService();
	//	public ChromeDriver webdriver;
	// Section ends for reusable variables

	// mobile capabilities
	

	//public ChromeOptions options = null;



	// Start Appium From command Prompt
	public void initiate() {
		try {
			
			prop = readPropertiesFile("configuration.properties");
			File src = new File(System.getProperty("user.dir")+"\\screenshots\\");
			File des = new File(System.getProperty("user.dir")+"\\test-output\\ExtentReports\\");
			FileUtils.copyDirectory(src, des);
			Runtime runtime = Runtime.getRuntime();
			if(prop.getProperty("StartEmulator")=="True") {
				//				runtime.exec("cmd.exe /c start avdmanager list avd");
				runtime.exec("cmd.exe /c start emulator -avd "+prop.getProperty("DEVICE_NAME"));
				Thread.sleep(15000);	
				runtime.exec("taskkill /F /IM cmd.exe");
				Thread.sleep(1000);
			}

				runtime.exec("cmd.exe /k start appium", null, new File(System.getenv("APPDATA")+"\\npm"));
				Thread.sleep(5000);	
				
//			  	builder = new AppiumServiceBuilder();
				//			builder.usingPort(4723);
				//			builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
				//			builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
				//			service = AppiumDriverLocalService.buildService(builder);
				//			if(!service.isRunning()) {
				//			service.start();
				//			}
				//			else {
				//				System.out.println("Appium Service already runnning");
				//			}
				//			String appiumServiceUrl = service.getUrl().toString();
				//			System.out.println("Service Status: "+service.isRunning());
				//			System.out.println("appiumServiceUrl = "+appiumServiceUrl);
			

		} catch (Exception e) {
			System.out.println("Appium Server Start Tried ...");
			System.out.println(e.getMessage());
		}


	}

	// Stop Appium From command Prompt
	public void stopServer() {
//		System.out.println("Appium Service Status: "+service.isRunning()) ;
		Runtime runtime = Runtime.getRuntime();
		try {
			//			String keysPath = System.getProperty("user.dir") + "\\SendKey.bat";
			//			runtime.exec("cmd.exe /k start "+ keysPath , null, new File(System.getenv("APPDATA")+"\\npm"));
			

			runtime.exec("taskkill /F /IM node.exe");
			runtime.exec("taskkill /F /IM cmd.exe");
			

//			System.out.println("Appium Service Status: "+service.isRunning()) ;
		} catch (Exception e) {
			System.out.println("Appium Server Stop Fails ...");
		}
	}



	// Load Properties

	public static Properties readPropertiesFile(String fileName) throws IOException {
		FileInputStream fis = null;
		Properties prop = null;
		try {
			fis = new FileInputStream(fileName);
			prop = new Properties();
			prop.load(fis);
		} catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} finally {
			fis.close();
		}
		return prop;
	}

	


	public static String  setUniqueID(){
		DateFormat dateFormat = new SimpleDateFormat("yyddmm");
		Date date = new Date();
		String dt=String.valueOf(dateFormat.format(date));
		SimpleDateFormat time = new SimpleDateFormat("HHmm");
		String tm= String.valueOf(time.format(new Date()));//time in 24 hour format
		String id= dt+tm;
		return id;   
	}

	public static String getReportPath (String path) {
		File testDirectory = new File(path);
		if (!testDirectory.exists()) {
			if (testDirectory.mkdir()) {
				System.out.println("Directory: " + path + " is created!" );

			} else {
				System.out.println("Failed to create directory: " + path);
				path = System.getProperty("user.dir");
			}
		} else {
			System.out.println("Directory already exists: " + path);
		}

		return path;
	}

	

	public static DesiredCapabilities appCapabilitiesSetup() {
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(MobileCapabilityType.PLATFORM_NAME, prop.getProperty("PLATFORM_NAME")); // Android
//		cap.setCapability(MobileCapabilityType.VERSION, prop.getProperty("VERSION")); // 10.0
		cap.setCapability(MobileCapabilityType.DEVICE_NAME, prop.getProperty("DEVICE_NAME")); // Nexus_6_API_29
		cap.setCapability(MobileCapabilityType.UDID, "emulator-5554");
		cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, prop.getProperty("AUTOMATION_NAME")); // Uiautomator2
		cap.setCapability("autoGrantPermissions", true);
		cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 40);
//		cap.setCapability(MobileCapabilityType.AUTO_WEBVIEW, false);
		
		if(prop.getProperty("apkFilepath").trim().length()>0) {
		cap.setCapability(MobileCapabilityType.APP, prop.getProperty("apkFilepath"));
		}
		else {
			File f = new File("src");
		    File fs = new File(f, "test/resources/apk/General-Store.apk");
			String absolutePath = fs.getAbsolutePath();
			System.out.println("apk path: = "+ absolutePath);
			cap.setCapability(MobileCapabilityType.APP, absolutePath);
			
		}
		String strMode = prop.getProperty("appLaunchMode"); // "normal"; // Could be fullReset or fastReset or
		
		if (strMode.equalsIgnoreCase("fullReset")) { // uninstall and install client
			System.out.println("  Driver DO FULL-RESET");
			cap.setCapability(MobileCapabilityType.FULL_RESET, true);
			cap.setCapability(MobileCapabilityType.NO_RESET, false);
		} else if (strMode.equalsIgnoreCase("fastReset")) { // clears cache and settings without reinstall
			System.out.println("  Driver DO FAST-RESET");
			cap.setCapability(MobileCapabilityType.FULL_RESET, false);
			cap.setCapability(MobileCapabilityType.NO_RESET, false);
		} else { // just start client
			System.out.println("  Driver DO NORMAL start");
			cap.setCapability(MobileCapabilityType.FULL_RESET, false);
			cap.setCapability(MobileCapabilityType.NO_RESET, true);
		}
		return cap;
		
	}
	
	public static DesiredCapabilities browserCapabilitiesSetup() {
		DesiredCapabilities bcap = new DesiredCapabilities();
		bcap.setCapability(MobileCapabilityType.PLATFORM_NAME, prop.getProperty("PLATFORM_NAME")); // Android
//		bcap.setCapability(MobileCapabilityType.VERSION, prop.getProperty("VERSION")); // 10.0
		bcap.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");	
		bcap.setCapability(MobileCapabilityType.FULL_RESET, false);
		bcap.setCapability(MobileCapabilityType.NO_RESET, true);		
		bcap.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
		bcap.setCapability("autoGrantPermissions", true);
		bcap.setCapability("androidPackage", "com.android.chrome");		
		bcap.setCapability("chromedriverExecutable","C:\\Users\\nisha\\.m2\\repository\\webdriver\\chromedriver\\win32\\74.0.3729.6\\chromedriver.exe");
		return bcap;
		
	}

	
	
	
}