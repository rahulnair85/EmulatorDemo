package Extent;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import library.BaseClass;
import library.reusablelibrary;

public class ListenerClass  implements ITestListener  {	

	library.reusablelibrary lib = new library.reusablelibrary();
	BaseClass bc = new BaseClass();
	
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		System.out.println(("*** Running test method " + result.getMethod().getMethodName() + "..."));
		ExtentTestManager.startTest(result.getMethod().getMethodName());
	}

	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		System.out.println("*** Executed " + result.getMethod().getMethodName() + " test successfully...");
		ExtentTestManager.getTest().log(Status.PASS, "Test passed");
	}

	public void onTestFailure (ITestResult result) {
		// TODO Auto-generated method stub
		int iLengthofError;	
		try {
		
		System.out.println("*** Test execution " + result.getMethod().getMethodName() + " failed...");
		ExtentTestManager.getTest().log(Status.FAIL, "Test Failed");

		System.out.println(result.getName()+"_"+reusablelibrary.setUniqueID());


		ExtentTestManager.getTest().log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " Test case FAILED due to below issues:", ExtentColor.RED));
		iLengthofError = result.getThrowable().toString().trim().length();
		if(iLengthofError>100) {
			iLengthofError = iLengthofError/3;			
		}
		ExtentTestManager.getTest().fail(result.getThrowable().toString().substring(0, iLengthofError));
		ExtentTestManager.getTest().log(Status.FAIL, "Snapshot : <a href='" + bc.screenShot() + "' target=\"_blank\">error</a>");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		System.out.println("*** Test " + result.getMethod().getMethodName() + " skipped...");
		ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped");
		
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		System.out.println("*** Test failed but within percentage % " + result.getMethod().getMethodName());
	}

	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		System.out.println("*** Test Suite " + context.getName() + " started ***");
	}

	public void onFinish(ITestContext context) {
		System.out.println(("*** Test Suite " + context.getName() + " ending ***"));
		ExtentTestManager.endTest();
		ExtentManager.getInstance().flush();

	}



}
