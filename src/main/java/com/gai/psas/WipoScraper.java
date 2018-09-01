package com.gai.psas;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.management.DescriptorRead;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

// PhantomJS

public class WipoScraper {
	
	
	private static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
	private static DesiredCapabilities desiredCaps ;
	private static WebDriver driver ;
	
	
	public static void initPhantomJS(){
		desiredCaps = new DesiredCapabilities();
		desiredCaps.setJavascriptEnabled(false);
		desiredCaps.setCapability("takesScreenshot", false);
		desiredCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "src/main/resources/phantomjs/bin/phantomjs");
		desiredCaps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "User-Agent", USER_AGENT);

		ArrayList<String> cliArgsCap = new ArrayList();
		cliArgsCap.add("--web-security=false");
		cliArgsCap.add("--ssl-protocol=any");
		cliArgsCap.add("--ignore-ssl-errors=true");
		cliArgsCap.add("--webdriver-loglevel=ERROR");

		desiredCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
		driver = new PhantomJSDriver(desiredCaps);
		driver.manage().window().setSize(new Dimension(1920, 1080));
	}
	
	
	
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException {
		System.setProperty("phantomjs.page.settings.userAgent", USER_AGENT);
		String searchQuery = "FP:(ADAPTING)" ;
		String baseUrl = "https://patentscope.wipo.int/search/en/result.jsf";
		initPhantomJS();
		driver.get(baseUrl) ;
		driver.findElement(By.id("resultListFormTop:refineSearchField")).sendKeys(searchQuery);
		driver.findElement(By.id("resultListFormTop:commandRefineSearchField")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 30);
		// We wait for the ajax call to fire and to load the response into the page
		Thread.sleep(800);
		int nbArticlesAfter = driver.findElements(By.xpath("//tr[@class='rf-dt-r']")).size();
		for(WebElement htmlElement: driver.findElements(By.xpath("//tr[@class='rf-dt-r']"))) {
			System.out.println("htmlElement ::"+htmlElement.getText());
		}
		System.out.println(String.format("Articles after clicking : %s", nbArticlesAfter));
	}

}