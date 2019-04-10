package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
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

		// sleep for 3 seconds
		sleep(3000);

		// maximize browser window
		driver.manage().window().maximize();

		// implicit wait
		// driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test(priority = 1)
	public void notVisibleTest() {
		// open the page http://the-internet.herokuapp.com/dynamic_loading/1
		driver.get("http://the-internet.herokuapp.com/dynamic_loading/1");

		// Find locator for startButton and click on it
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button"));
		startButton.click();

		// Then get finish element text
		WebElement finishElement = driver.findElement(By.id("finish"));

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(finishElement));

		String finishText = finishElement.getText();

		// compare actual finish element text with expected "Hello World!" using Test NG
		// Assert class
		Assert.assertTrue(finishText.contains("Hello World!"), "Finish text: " + finishText);

		// startButton.click();

	}

	@Test(priority = 2)
	public void timeoutTest() {
		// open the page http://the-internet.herokuapp.com/dynamic_loading/1
		driver.get("http://the-internet.herokuapp.com/dynamic_loading/1");

		// Find locator for startButton and click on it
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button"));
		startButton.click();

		// Then get finish element text
		WebElement finishElement = driver.findElement(By.id("finish"));

		WebDriverWait wait = new WebDriverWait(driver, 2);
		try {
			wait.until(ExpectedConditions.visibilityOf(finishElement));
		} catch (TimeoutException exception) {
			System.out.println("Exception catched: " + exception.getMessage());
			sleep(3000);
		}

		String finishText = finishElement.getText();

		// compare actual finish element text with expected "Hello World!" using Test NG
		// Assert class
		Assert.assertTrue(finishText.contains("Hello World!"), "Finish text: " + finishText);

		// startButton.click();

	}

	@Test(priority = 3)
	public void noSuchElementTest() {
		// open the page
		driver.get("http://the-internet.herokuapp.com/dynamic_loading/2");

		// Find locator for startButton and click on it
		WebElement startButton = driver.findElement(By.xpath("//div[@id='start']/button"));
		startButton.click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		Assert.assertTrue(
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("finish"), "Hello World!")),
				"Couldn't verify expected text 'Hello World!'");

//		WebElement finishElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("finish")));
//
//		String finishText = finishElement.getText();
//
//		// compare actual finish element text with expected "Hello World!" using Test NG
//		// Assert class
//		Assert.assertTrue(finishText.contains("Hello World"), "Finish text: " + finishText);
	}

	@Test
	public void staleElementTest() {
		driver.get("http://the-internet.herokuapp.com/dynamic_controls");

		WebElement checkbox = driver.findElement(By.id("checkbox"));
		WebElement removeButton = driver.findElement(By.xpath("//button[contains(text(),'Remove')]"));

		WebDriverWait wait = new WebDriverWait(driver, 10);

		removeButton.click();
//		wait.until(ExpectedConditions.invisibilityOf(checkbox));
//		Assert.assertFalse(checkbox.isDisplayed());

//		Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(checkbox)),
//				"Checkbox is still visible, but shouldn't be");

		Assert.assertTrue(wait.until(ExpectedConditions.stalenessOf(checkbox)),
				"Checkbox is still visible, but shouldn't be");
		
		WebElement addButton = driver.findElement(By.xpath("//button[contains(text(),'Add')]"));
		addButton.click();
		
		WebElement checkbox2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkbox")));
		Assert.assertTrue(checkbox2.isDisplayed(), "Checkbox is not visible, but it should be");

	}

	private void sleep(long m) {
		try {
			Thread.sleep(m);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		// Close browser
		driver.quit();
	}

}
