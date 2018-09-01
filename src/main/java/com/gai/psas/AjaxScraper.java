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

public class AjaxScraper {
	
	
	private static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
	private static DesiredCapabilities desiredCaps ;
	private static WebDriver driver ;
	
	
	public static void initPhantomJS(){
		desiredCaps = new DesiredCapabilities();
		desiredCaps.setJavascriptEnabled(true);
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
		String searchQuery = "ADAPTING" ;
		String baseUrl = "https://worldwide.espacenet.com/"+ "searchResults?ST=singleline&locale=en_EP&submitted=true&DB=&query=" + URLEncoder.encode(searchQuery, "UTF-8")+"&Submit=Search"; ;
		initPhantomJS();
		driver.get(baseUrl) ;
		int nbArticlesBefore = driver.findElements(By.xpath("//tr[@class='contentRowClass']")).size();
		for(WebElement htmlElement: driver.findElements(By.xpath("//tr[@class='contentRowClass']"))) {
			
			System.out.println("htmlElement ::"+htmlElement.findElement(By.xpath("//td[@class='inventorColumn']")).getText());
			System.out.println("htmlElement ::"+htmlElement.findElement(By.xpath("//td[@class='applicantColumn']")).getText());
			System.out.println("htmlElement ::"+htmlElement.findElement(By.xpath("//td[@class='cpcColumn']")).getText());
			System.out.println("htmlElement ::"+htmlElement.findElement(By.xpath("//div[@class='ipcColumn ']")).getText());
			System.out.println("htmlElement ::"+htmlElement.findElement(By.xpath("//td[@class='publicationInfoColumn']")).getText());
			System.out.println("htmlElement ::"+htmlElement.findElement(By.xpath("//td[@class='nowrap']")).getText());
			
		}
		System.out.println("nbArticlesBefore ::"+nbArticlesBefore);
		//driver.findElement(By.id("load-more-btn")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, 30);
		// We wait for the ajax call to fire and to load the response into the page
		Thread.sleep(800);
		int nbArticlesAfter = driver.findElements(By.xpath("//div[@class='card-stack']/div")).size();
		System.out.println(String.format("Initial articles : %s Articles after clicking : %s", nbArticlesBefore, nbArticlesAfter));
	}

}