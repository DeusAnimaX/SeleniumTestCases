package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Util {
	
	public static String getMacChromeDriver() {
		String url = "/Users/sofia/Documents/workspace/WebDriverTest/chromedriver";
		return url;
	}
	
	public static WebDriver getDriver() {
		System.setProperty("webdriver.chrome.driver", Util.getMacChromeDriver());
		WebDriver driver = new ChromeDriver();
		return driver;
	}
	
	public static String getFilePath() {
		return "/Users/sofia/Downloads";
	}
	
	public static String getFileName() {
		return "Parametros.xlsx";
	}
	
	public static String getSheetName() {
		return "Hoja1";
	}

}
