package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

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
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.Util;
import Data.ReadData;
import junit.framework.Assert;

public class TestCases {
	
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    static PrintStream out = System.out;    // variables objetos in y out para

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		testCase4(Util.getDriver());
	}
	
	public static void testCase1(WebDriver driver) throws Exception {
		// Inicializa el web driver para peticiones sincronicas
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		// Carga la pagina web
		driver.get("http://demo.nopcommerce.com");
		
		// Busca las opciones del menu principal
		String xpath = "//ul[@class='top-menu']/li/a[@href]";
		List<WebElement> menuOptions = driver.findElements(By.xpath(xpath));
		List<String> optionKeys = new ArrayList<String>();
		
		// Guarda el identificador de cada opcion de menu para consultarlo despues de cada redireccionamiento
		for (WebElement element : menuOptions) {
			String redirectPage = element.getAttribute("href");
			redirectPage = redirectPage.substring(redirectPage.lastIndexOf('/'));
			optionKeys.add(redirectPage);
		}
		
		// Busca la opcion de menu en la pagina, la ejecuta y espera 3 segundos para el siguiente
		for (String key : optionKeys) {
			String optionXPath = "//ul[@class='top-menu']/li/a[@href='" + key + "']";
			WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(optionXPath)));
			btn.click();
		}
		
		// Busca el logo principal del sitio y ejecuta el 'click' que redirecciona a la pagina principal
		xpath = "//div[@class='header-logo']/a[@href='/']";
		WebElement pageLogo = driver.findElement(By.xpath(xpath));
		pageLogo.click();
		
		// Ejecuta codigo JavaScript para mostrar un popup
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("alert('Test finalized successfully!')");
    }
	
	public static void testCase4(WebDriver driver) throws Exception {
		// Inicializa el web driver para peticiones sincronicas
		WebDriverWait wait = new WebDriverWait(driver, 10);
		driver.get("http://demo.nopcommerce.com");
		
		// Selecciona la primera categoria del menu
    	WebElement computersCategory = driver.findElement(By.linkText("Computers"));
    	computersCategory.click();

    	// Obtiene el contenedor de categorias y luego busca por "Software"
		String xpath = "//div[@class=\"item-grid\"]";
    	WebElement categoriesContainer = driver.findElement(By.xpath(xpath));
    	categoriesContainer.findElement(By.linkText("Software")).click();
    	
    	// Obtiene el tipo de vista "Lista" y luego la activa
    	xpath = "//div[@class=\"product-viewmode\"]/a[@title=\"List\"]";
    	WebElement listView = driver.findElement(By.xpath(xpath));
    	listView.click();
    	
    	// Ordena por "Mas barato"
    	xpath = "//div[@class=\"product-sorting\"]/select[@name=\"products-orderby\"]/option[4]";
    	WebElement sortOption = driver.findElement(By.xpath(xpath));
    	sortOption.click();
    	
    	// Verifica el nombre del primer producto en lista
    	xpath = "//div[@class=\"product-list\"]//h2[@class=\"product-title\"]";
    	WebElement product = driver.findElement(By.xpath(xpath));
    	assertTrue(product.getText().equals("Sound Forge Pro 11 (recurring)"));
    	
    	// Ordena por "Mas caro"
    	xpath = "//div[@class=\"product-sorting\"]/select[@name=\"products-orderby\"]/option[5]";
    	sortOption = driver.findElement(By.xpath(xpath));
    	sortOption.click();
    	
    	// Verifica el nombre del primer producto en lista
    	xpath = "//div[@class=\"product-list\"]//h2[@class=\"product-title\"]";
    	product = driver.findElement(By.xpath(xpath));
    	assertTrue(product.getText().equals("Adobe Photoshop CS4"));
    	
		// Ejecuta codigo JavaScript para mostrar un popup
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("alert('Test 4 finalized successfully!')");
		
		// Espera a que el popup sea clickeado y cierra el browser
		wait.until(ExpectedConditions.alertIsPresent());
		wait.until(ExpectedConditions.not(ExpectedConditions.alertIsPresent()));
		
		driver.close();
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
