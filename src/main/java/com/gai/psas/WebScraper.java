package com.gai.psas;


import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gai.psas.patent.model.patent.Item;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WebScraper {

	public static void main(String[] args) {
		
	    String searchQuery = "ADAPTING" ;
		String baseUrl = "http://patft.uspto.gov/" ;
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		
		try {
			String searchUrl = baseUrl + "netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&p=1&u=%2Fnetahtml%2FPTO%2Fsearch-bool.html&r=1&f=G&l=50&co1=AND&d=PTXT&s1="+ URLEncoder.encode(searchQuery, "UTF-8")+"&OS=ADAPTING&RS=" + URLEncoder.encode(searchQuery, "UTF-8");
			HtmlPage page = client.getPage(searchUrl);
			System.out.println("page :::"+page.asXml());
			
			List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//td[@class='applicantColumn']") ;
			if(items.isEmpty()){
				System.out.println("No items found !");
			}else{
				for(HtmlElement htmlItem : items){
					HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//p[@class='result-info']/a"));
					HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//a/span[@class='result-price']")) ;
					// It is possible that an item doesn't have any price, we set the price to 0.0 in this case
					String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText() ;
					Item item = new Item();
					item.setTitle(itemAnchor.asText());
					item.setUrl( baseUrl + itemAnchor.getHrefAttribute());
					item.setPrice(new BigDecimal(itemPrice.replace("$", "")));
					ObjectMapper mapper = new ObjectMapper();
					String jsonString = mapper.writeValueAsString(item) ;
					System.out.println(jsonString);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}

	}

}
