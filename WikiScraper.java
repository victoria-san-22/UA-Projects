/*
* AUTHOR: Victoria Santos
* FILE: WikiScraper.java
* ASSIGNMENT: PA10 WikiRacer
* COURSE: CSc 210 001; Spring 2022
* PURPOSE: This program is meant to simulate the online game
* WikiRacer, which challenges users to make it from one
* Wikipedia page to another solely through the current
* page's hrefs. I operates in conjunction with two
* other files, MaxPQ and WikiScraper. Altogether, the
* program works by fetching and scraping HTMLs, managing 
* a max priority queue of "ladders", and making informed
* choices about which link might lead to the end page.
* This file is responsible for fetching and scraping the
* HTML, returning URLs, and returning the set of links for
* a given page.
*/


import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WikiScraper {
	
	
	private static Map<String, Set<String>> memo = new ConcurrentHashMap<>();
	
	
	/**
	 * Responsible for returning the given link's set of
	 * page links. The function is memoized, creating and
	 * storing Sets for link names as they're found (via
	 * fetching and scraping), and returning the stored 
	 * sets when a repeat linkname is passed. 
	 * 
	 * @param link, a link name as a String
	 * 
	 * @return the link's link set
	 */
	public static Set<String> findWikiLinks(String link) {
		if (memo.containsKey(link)) {
			return memo.get(link);
		}
		else {
			String html = fetchHTML(link);
			Set<String> links = scrapeHTML(html);
			memo.put(link, links);
			return links;
		}
	}
	
	
	/**
	 * Responsible for fetching the actual html 
	 * code of the given link, using the getURL
	 * function and other methods.
	 * 
	 * @param link, a link name as a String
	 * 
	 * @return page HTML
	 */
	private static String fetchHTML(String link) {
		StringBuffer buffer = null;
		try {
			URL url = new URL(getURL(link));
			InputStream is = url.openStream();
			int ptr = 0;
			buffer = new StringBuffer();
			while ((ptr = is.read()) != -1) {
			    buffer.append((char)ptr);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		return buffer.toString();
	}
	
	
	/**
	 * Responsible for forming the web page url
	 * of the given link
	 * 
	 * @param link, a link name as a String
	 * 
	 * @return wikipedia url with link
	 */
	private static String getURL(String link) {
		return "https://en.wikipedia.org/wiki/" + link;
	}
	
	
	/**
	 * Responsible for taking in a string of the HTML 
	 * source for a webPage. Returns a Set<String> 
	 * containing all of the valid wiki link
	 * titles found in the HTML source.
	 * 
	 * @param html, the html of a link as a String
	 * 
	 * @return hrefs, the set of Strings of links
	 * 
	 */
	private static Set<String> scrapeHTML(String html) {
		Set<String> hrefs = new HashSet<String>();
		
		while (html.contains("href")) {
			html = html.substring(html.indexOf("<a href="));
			int endIndex = html.indexOf("</a>") + 4; // 4 = length of "</a>"
			String currHref = html.substring(0, endIndex);
			
			if (currHref.contains("<a href=\"/wiki/")) {
				currHref = currHref.substring(currHref.indexOf("/wiki/") + 6); // 4 = length of "/wiki/"
				int linkEndIndex = currHref.indexOf("\"");
				currHref = currHref.substring(0, linkEndIndex);
				
				if (!currHref.contains(":") && !currHref.contains("#")) {
					hrefs.add(currHref);
				}
			}		
			html = html.substring(endIndex);
		}	
		return hrefs;
	}
	
}
