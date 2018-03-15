package com.zoho.projectbase.Testcases;

import java.io.IOException;
import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import project.util.Xls_Reader;
import project.util.dataUtil;

import com.relevantcodes.extentreports.LogStatus;
import com.zoho.project.baseTest.BaseTest;



public class loginTest extends BaseTest {
	
	String testCaseName="LoginTest";
	
	Xls_Reader xls;
	SoftAssert softAssert=new SoftAssert();
	
	
@Test(dataProvider="getdata")
	public void doLogin(Hashtable<String, String>data) throws IOException, InterruptedException{
	test=rep.startTest("LoginTest");
	test.log(LogStatus.INFO, "Starting Login Test");
	if(!dataUtil.isRunnable(testCaseName, xls) ||  data.get("Runmode").equals("N")){
		test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
		throw new SkipException("Skipping the test as runmode is N");
	}
	test.log(LogStatus.INFO, data.toString());
	
	browsertype(data.get("Browser"));
	takeScreenShot();
	test.log(LogStatus.INFO, "Broswer opened");
	navigate("url");
	takeScreenShot();
	type("Login_id", data.get("Username"));
	type("Loginpwd_id",data.get("Password"));
	click("sign_xpath");
	String actualresult="Y";
			if(actualresult.equalsIgnoreCase("ExpectedResult")){
			reportPass("Test Passed");
			}
			else{
				reportFail("Test Failed");
			}
	Thread.sleep(5000);
	close();

	
	takeScreenShot();
	close();
	}
	
@DataProvider
public Object[][] getdata(){
	super.init();
xls=new Xls_Reader(prop.getProperty("xlspath"));

	return dataUtil.getdata(xls,testCaseName);
		
}

@AfterMethod
public void quit(){
	rep.endTest(test);
	rep.flush();

}


}
