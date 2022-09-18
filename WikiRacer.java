/*
* AUTHOR: Victoria Santos
* FILE: WikiRacer.java
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
* This file is responsible for driving the program and
* making the comparisons.
*/


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class WikiRacer {

	
	/**
	 * This is the main function of the class responsible
	 * for calling findWikiLadder given command line args.
	 * 
	 * @param args as a String[]
	 * 
	 * @return none
	 * 
	 */
	public static void main(String[] args) {
		List<String> ladder = findWikiLadder(args[0], args[1]);
		System.out.println(ladder);
	}

	
	/**
	 * Responsible for driving the main work of the program.
	 * Creates the priority queue, adds the start page, and
	 * begins the work of testing different possible ladders
	 * to the end page.
	 * 
	 * @param start, the link for the starting page as a String
	 * @param end, the link for the ending page as a String
	 * 
	 * @return currLadder, the final ladder from start to end,
	 * or an empty List<String> if no ladder is found
	 */
	private static List<String> findWikiLadder(String start, String end) {
		MaxPQ priorityQueue = new MaxPQ();
		List<String> firstLadder = new ArrayList<String>();
		firstLadder.add(start);
		priorityQueue.enqueue(firstLadder, 1000);
		Set<String> endLinks = WikiScraper.findWikiLinks(end);
		
		while (!priorityQueue.isEmpty()) {
			List<String> currLadder = priorityQueue.dequeue();
			Set<String> currLinks = WikiScraper.findWikiLinks(currLadder.get(currLadder.size() - 1));	
			if (currLinks.contains(end)) {
				currLadder.add(end);
				return currLadder;
			}
			
			currLinks.parallelStream().forEach(link -> {
				  WikiScraper.findWikiLinks(link); });			
			for (String subLink : currLinks) {
				if (!currLadder.contains(subLink)) {
					List<String> nextLadder = new ArrayList<String>(currLadder);
					nextLadder.add(subLink);
					
					Set<String> linksInCommon = WikiScraper.findWikiLinks(subLink);
					linksInCommon.retainAll(endLinks);
					int numSharedLinks = linksInCommon.size();
					priorityQueue.enqueue(nextLadder, numSharedLinks);
				}
			}
		}	
		List<String> noLadder = new ArrayList<String>();
		return noLadder;
	}

	
}
