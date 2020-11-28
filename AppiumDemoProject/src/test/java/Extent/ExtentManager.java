package Extent;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import library.reusablelibrary;

public class ExtentManager {
    private static ExtentReports extent;
    private static String reportFileName = "RunReport_"+reusablelibrary.setUniqueID()+".html";
	private static String reportFilepath = System.getProperty("user.dir") + "\\test-output\\ExtentReports\\";
	public static String reportFileLocation =  reportFilepath+ reportFileName;
 
    public static ExtentReports getInstance()  {
        if (extent == null)
            createInstance();
        return extent;
    }
 
    //Create an extent report instance
    public static ExtentReports createInstance()  {

       System.out.println("reportFileLocation="+reportFileLocation);
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportFileLocation);
        htmlReporter.loadXMLConfig(System.getProperty("user.dir")+"/html-config.xml");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        //Set environment details
		extent.setSystemInfo("OS", "Windows");
		extent.setSystemInfo("AUT", "Demo");
 
        return extent;
    }
}