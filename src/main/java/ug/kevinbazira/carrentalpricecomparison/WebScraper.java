package ug.kevinbazira.carrentalpricecomparison;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Represents a web scraper.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
public class WebScraper {

    /**
     * Scrapes given website and returns web elements with products data.
     * @param searchURL website search URL
     * @param brandName brand name of car that we want to scrape
     * @param cssClasses used as product element identifier
     * @return products
     */
    public static Elements scrapeWebsite(String searchURL, String brandName, String cssClasses) throws Exception{

        //Download HTML document from website
        Document htmlDoc = Jsoup.connect(searchURL + brandName).get();

        //Get all the products on the page
        Elements products = htmlDoc.select(cssClasses);

        return products;
    }

}
