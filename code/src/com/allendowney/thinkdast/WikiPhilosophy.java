package com.allendowney.thinkdast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class WikiPhilosophy {

    final static List<String> visited = new ArrayList<String>();
    final static WikiFetcher wf = new WikiFetcher();

    /**
     * Tests a conjecture about Wikipedia and Philosophy.
     *
     * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
     *
     * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String destination = "https://en.wikipedia.org/wiki/Philosophy";
        String source = "https://en.wikipedia.org/wiki/Benzene";

        testConjecture(destination, source);
    }

    /**
     * Starts from given URL and follows first link until it finds the destination or exceeds the limit.
     *
     * @param destination
     * @param source
     * @throws IOException
     */
    public static void testConjecture(String destination, String source) throws IOException {
        // TODO: FILL THIS IN!
        String url = source;
        boolean flag = true;
        while(!visited.contains(url)&!url.equals(destination)){
            visited.add(url);
            Element elt = getFirstValidLink(url);
            Document doc = Jsoup.connect(url).get();  // Connect to the URL and parse the HTML
            String header = doc.getElementById("firstHeading").text();
            if (elt == null) {
                flag = false;
                System.err.println("Got to a page with no valid links.");
                return;
            }

            System.out.println("**" + header + "**");
            url = elt.attr("abs:href");
        }
        if (flag){
            Document doc = Jsoup.connect(url).get();  // Connect to the URL and parse the HTML
            String header = doc.getElementById("firstHeading").text();
            System.out.println("**" + header + "**");
            System.out.println("Eureka!");
        }
    }

    private static Element getFirstValidLink(String url) throws IOException {

        Elements paragraphs = wf.fetchWikipedia(url);
        WikiParser wp = new WikiParser(paragraphs);
        Element elt = wp.findFirstLink();
        return elt;

    }

}
