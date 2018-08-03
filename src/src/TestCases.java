package src;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.Util;
import Data.ReadData;

public class TestCases {
	
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    static PrintStream out = System.out;    // variables objetos in y out para

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		testCase2(Util.getDriver());
	}
	
	public static void testCase2(WebDriver driver) throws Exception {
		Boolean result = false;
        // Carga la pagina web
        driver.get("http://demo.nopcommerce.com/wishlist");

        String xpath = "//div[@class='no-data']";

        // Obtiene el mensaje 'The wish list is empty';
        String message = driver.findElement(By.xpath(xpath)).getText();

        if(message.equals("The wishlist is empty!")){
            String searchBarPath = "//input[@id='small-searchterms']";
            //ingresa el texto de busqueda y ejecuta un enter
            driver.findElement(By.xpath(searchBarPath)).sendKeys("Fahrenheit 451"+Keys.ENTER);
            String btnWishPath = "//input[@class='button-2 add-to-wishlist-button']";
            //agrega el libro al wish list
            driver.findElement(By.xpath(btnWishPath)).click();
			Thread.sleep(3000);

            String btnWishList = "//a[@class='ico-wishlist']";
            //regresa a la pagina de whishlist
            driver.findElement(By.xpath(btnWishList)).click();
			Thread.sleep(3000);


            //verifica si agrego el libro Fahrenheit 451
            String wishListItem = "//a[@class='product-name']";
            String wishItemName = driver.findElement(By.xpath(wishListItem)).getText();
            result = wishItemName.contains("Fahrenheit 451");
        }

        // Ejecuta codigo JavaScript para mostrar un popup
        JavascriptExecutor js = (JavascriptExecutor) driver;
        if(result)
            js.executeScript("alert('Test finalized successfully!')");
        else
            js.executeScript("alert('Test finalized with errors!')");
	
	}
	
	public static void testCase5(WebDriver driver) throws Exception {
    	driver.get("http://demo.nopcommerce.com/wishlist");
    	
    	String xpath = "//div[@class='no-data']";
    	String message = driver.findElement(By.xpath(xpath)).getText();
    	
    	assertEquals("The wishlist is empty!", message);
    	readItemsFromData(ReadData.readExcel(), driver);
    	driver.close();
    }
	
	public static void readItemsFromData(Sheet sheet, WebDriver driver) throws InterruptedException {
		int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
		DataFormatter formatter = new DataFormatter();
		
		//Create a loop over all the rows of excel file to read it
		for (int i = 1; i < rowCount+1; i++) {
			Row row = sheet.getRow(i);
			addItemToCart(row.getCell(0).getStringCellValue(),formatter.formatCellValue(row.getCell(1)),formatter.formatCellValue(row.getCell(2)), driver);
		}
		
	}
	
	public static void addItemToCart(String name, String quantity, String total, WebDriver driver) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		try {
			//Search for the item
			driver.findElement(By.id("small-searchterms")).sendKeys(name+Keys.ENTER);
			String btnWishPath = "//input[@class='button-2 add-to-wishlist-button']";
			
	        //Add item to wishlist
			WebElement btnAdd = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(btnWishPath)));
			btnAdd.click();
	        
	        //Wait until the expected conditions
	        String btnWishlist = "//a[@class='ico-wishlist']";
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(btnWishlist)));
			element.click();
			
	        //Wait until the expected conditions
	        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@class='qty-input']")));
	        input.sendKeys(Keys.HOME,Keys.chord(Keys.SHIFT,Keys.END),quantity+Keys.ENTER);
	        
	        //Verify if total is correct
	        String result = driver.findElement(By.xpath("//span[@class='product-subtotal']")).getText();
	        if(result.equals(total)){
	        	out.println(name+": Total correct");
	        } else {
	        	out.println(name+": Total incorrect");
	        }
	        
	        //Remove item from wishlist
	        driver.findElement(By.xpath("//input[@name='removefromcart']")).click();
	        driver.findElement(By.xpath("//input[@class='button-2 update-wishlist-button']")).click();
		} catch (TimeoutException e) {
			out.println(name+": Item Not Found");
			 String btnWishlist = "//a[@class='ico-wishlist']";
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(btnWishlist)));
			element.click();
		}
		
	}
	
	
}
