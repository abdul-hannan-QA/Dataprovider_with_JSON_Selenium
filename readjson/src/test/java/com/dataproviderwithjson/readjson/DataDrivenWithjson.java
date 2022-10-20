package com.dataproviderwithjson.readjson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DataDrivenWithjson {


	public static WebDriver 	driver;


	@BeforeSuite
	public static WebDriver  setup() {



		WebDriverManager.chromedriver().setup();

		//headless browser
		ChromeOptions option=new ChromeOptions();
		option.addArguments("window-size=1400,800");
		option.addArguments("headless");

		driver = new ChromeDriver();
		return driver;


	}

	@Test(dataProvider="dp")
	public void login(String data) {
         String[] users=data.split(",");
		driver.get("https://admin-demo.nopcommerce.com/login");

		WebElement txtEmail=driver.findElement(By.id("Email"));
		txtEmail.clear();
		txtEmail.sendKeys(users[0]);


		WebElement txtPassword=driver.findElement(By.id("Password"));
		txtPassword.clear();
		txtPassword.sendKeys(users[1]);

		driver.findElement(By.xpath("//button[@type='submit']")).click(); //Login  button

		String exp_title="Dashboard / nopCommerce administration";
		String act_title=driver.getTitle();
		Assert.assertEquals(exp_title,act_title);



	}


	@DataProvider(name="dp")
	public String[] readjsondata() throws IOException, Exception{


		JSONParser parser=new JSONParser();
		FileReader reader= new FileReader(".\\jsonfiles\\testdata.json");

		Object obj=parser.parse(reader);

		JSONObject userloginobject=(JSONObject)obj;

		JSONArray userloginsarray=(JSONArray)userloginobject.get("userlogins");
		String arr[]=new String[userloginsarray.size()];

		for (int i = 0; i < userloginsarray.size(); i++) {

			JSONObject users =(JSONObject)userloginsarray.get(i);

			String user=(String)users.get("username");
			String pwd=(String)users.get("password");

			arr[i]=user+","+ pwd;
		}
		return arr;
	}













}
