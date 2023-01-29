package com.herokuapp.theinternet;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ExceptionsTests {
	private WebDriver driver;

	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	private void setUp(@Optional("chrome") String browser) {
//		Create driver
		switch (browser) {
		case "chrome":
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			driver = new ChromeDriver();
			break;

		case "firefox":
			System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
			driver = new FirefoxDriver();
			break;

		default:
			System.out.println("Do not know how to start " + browser + ", starting chrome instead");
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			driver = new ChromeDriver();
			break;
		}
		
		// Move browser window to the left monitor
		driver.manage().window().setPosition(new Point(-1000,200));

		// maximize browser window
		driver.manage().window().maximize();
	}
	
	@Test
	public void noSuchElementExceptionTest( ) {
		//Test case 1: NoSuchElementException
		//Open page
		driver.get("https://practicetestautomation.com/practice-test-exceptions/");
		
		//Click Add button
		WebElement addButtonElement = driver.findElement(By.id("add_btn"));
		addButtonElement.click();
		
		// Explicit wait
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement row2Input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='row2']/input")));
		
		//Verify Row 2 input field is displayed
		Assert.assertTrue(row2Input.isDisplayed(), "Row 2 is not displayed");
	}
	
	@Test
	public void elementNotInteractableExceptionTest( ) {
		// Test case 2: ElementNotInteractableException
		// Open page
		driver.get("https://practicetestautomation.com/practice-test-exceptions/");
		
		// Click Add button
		WebElement addButtonElement = driver.findElement(By.id("add_btn"));
		addButtonElement.click();
		
		// Wait for the second row to load
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement row2Input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='row2']/input")));
		
		// Type text into the second input field
		row2Input.sendKeys("Sushi");
		
		// Push Save button using locator By.name(“Save”)
		WebElement saveButton = driver.findElement(By.xpath("//div[@id='row2']/button[@name='Save']"));
		saveButton.click();
		
		// Verify text saved
		WebElement confirmationMessage = driver.findElement(By.id("confirmation"));
		String messageText = confirmationMessage.getText();
		Assert.assertEquals(messageText, "Row 2 was saved", "Confirmation message text is not expected");
	}
	
	@Test
	public void invalidElementStateExceptionTest( ) {
		// Test case 3: InvalidElementStateException
		// Open page
		driver.get("https://practicetestautomation.com/practice-test-exceptions/");
		
		
		// Clear input field
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement row1Input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='row1']/input")));
		WebElement editButton = driver.findElement(By.id("edit_btn"));
		editButton.click();
		
		wait.until(ExpectedConditions.elementToBeClickable(row1Input));
		row1Input.clear();
				
		// Type text into the input field
		row1Input.sendKeys("Sushi");
		
		WebElement saveButton = driver.findElement(By.id("save_btn"));
		saveButton.click();
		
		// Verify text changed
		String value = row1Input.getAttribute("value");
		Assert.assertEquals(value, "Sushi", "Input 1 field value is not expected");
		
		// Verify text saved
		WebElement confirmationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmation")));
		String messageText = confirmationMessage.getText();
		Assert.assertEquals(messageText, "Row 1 was saved", "Confirmation message text is not expected");
	}
	
	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		// Close browser
		driver.quit();
	}
}
