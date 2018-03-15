package com.zoho.project.baseTest;





import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;


import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import project.util.ExtentManager;
import project.util.Xls_Reader;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class BaseTest {
	
	public WebDriver driver;
	
	public Properties prop;
	
	public ExtentReports rep=ExtentManager.getInstance();
	public ExtentTest test;



	//Initialize
	public void init(){
		if (prop==null){
			prop=new Properties();
			try {
				FileInputStream fs=new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\projectconfig");
			prop.load(fs);
			prop.getProperty("url");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}			
		}
		}
	
	//Broswer& Webdriver
	//======Initialiaze the browser=====//
	public void browsertype(String btype) throws IOException{
		
		if(btype.equalsIgnoreCase("Chrome"))
		{
			System.setProperty("webdriver.chrome.driver","D:\\New Webdrivers\\chromedriver.exe");
			driver=new ChromeDriver();
			}
		else if(btype.equalsIgnoreCase("mozilla"))
		{
			System.setProperty("webdriver.gecko.driver","D:\\New Webdrivers\\geckodriver.exe"); 
	        driver=new FirefoxDriver();
	        driver.manage().window().maximize();
	        }
		test.log(LogStatus.INFO, "Opening browser "+btype);
		
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		driver.manage().window().maximize();	
	}
	
	//Typing data in a text field
	public void type(String locaterkey,String data){
		test.log(LogStatus.INFO, "Typing in " +locaterkey+" data." +data);
		WebElement e=null;
		
			if(locaterkey.endsWith("_id")){
			e=driver.findElement(By.id(prop.getProperty(locaterkey)));
			e.clear();
			e.sendKeys(data);
			}
		else if(locaterkey.endsWith("_xpath")){
			e=driver.findElement(By.xpath(prop.getProperty(locaterkey)));
			e.clear();
			e.sendKeys(data);
		}
	else if(locaterkey.endsWith("_name")){
		e=driver.findElement(By.name(prop.getProperty(locaterkey)));
		e.clear();
		e.sendKeys(data);
		}
	else{
		reportFail("Locator_not_found" +locaterkey );
		Assert.fail("locator not found" +locaterkey);
	}
	
	test.log(LogStatus.INFO, "successfully typed in "+ locaterkey);
	}
	public void click(String locaterkey){
		
		test.log(LogStatus.INFO, "Clicking on " +locaterkey);
		getElement(locaterkey).click();
		test.log(LogStatus.INFO, "Clicked successfully on " +locaterkey);

	}
	public WebElement getElement(String locaterkey){
		WebElement e=null;
		try {
			if(locaterkey.endsWith("_id")){
			e=driver.findElement(By.id(prop.getProperty(locaterkey)));
			}
		else if(locaterkey.endsWith("_xpath")){
			e=driver.findElement(By.xpath(prop.getProperty(locaterkey)));
		}
	else if(locaterkey.endsWith("_name")){
		e=driver.findElement(By.name(prop.getProperty(locaterkey)));
		}
	else{
		reportFail("Locator_not_found" +locaterkey );
		Assert.fail("locator not found" +locaterkey);
	}
		} catch (Exception e2) {
			// report failure
			reportFail(e2.getMessage());
			e2.printStackTrace();
			Assert.fail("failed the test" +e2.getMessage());
	}
		return e;
	}

	//fail report
	public void reportFail(String msg){
		test.log(LogStatus.FAIL, msg);
		takeScreenShot();
		Assert.fail(msg);
		
	}
	public void reportPass(String msg){
		test.log(LogStatus.PASS, msg);
		takeScreenShot();
	}
	//navigate
	public void navigate(String urlkey){
		driver.get(prop.getProperty(urlkey));
	}
	
	//ScreenShot
	public void takeScreenShot(){
		// fileName of the screenshot
		Date d=new Date();
		String screenshotFile=d.toString().replace(":", "_").replace(" ", "_")+".png";
		// store screenshot in that file
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//put screenshot file in reports
		test.log(LogStatus.INFO,"Screenshot-> "+ test.addScreenCapture(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
		
	}
	public void close(){
		driver.close();
	}
	
	
	/*--------Validation Functions--------*/
	
	public boolean isElementpresent(String locaterkey){
		List<WebElement> elementList=null;
		if(locaterkey.endsWith("_id")){
			elementList=driver.findElements(By.id(prop.getProperty(locaterkey)));
			}
		else if(locaterkey.endsWith("_xpath")){
			elementList=driver.findElements(By.xpath(prop.getProperty(locaterkey)));
		}
		else if(locaterkey.endsWith("_class")){
			elementList=driver.findElements(By.className(prop.getProperty(locaterkey)));
		}
	else if(locaterkey.endsWith("_name")){
		elementList=driver.findElements(By.name(prop.getProperty(locaterkey)));
		}
	else{
		reportFail("Locator not correct - " + locaterkey);
		Assert.fail("Locator not correct - " + locaterkey);
	}
		if(elementList.size()==0){
			return false;
		}
		else{
			return true;
	
		}
	}
		
	//VErify Text
	public boolean isTextpresent(){
		return false;
	}
	//Verify Title
	public boolean verifyTitle(){
		return false;
	}
	
	public boolean verifyText(){
		return false;
	}
	
	
}
