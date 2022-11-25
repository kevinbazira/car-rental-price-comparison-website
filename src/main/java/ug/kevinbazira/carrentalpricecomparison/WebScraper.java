package ug.kevinbazira.carrentalpricecomparison;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a web scraper.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
public class WebScraper {

    /**
     * Traverses website products HTML elements and returns cars data list with
     * rentURL, imageURL, rentPerDay, carBrandAndModel, and serviceProvider for all cars
     * found on search page.
     * @param products web elements with products data
     * @param rentURLAnchorTagSelectors css selectors for URL to rent car
     * @param imageTagSelectors css selectors for car image
     * @param rentPerDayTagSelectors css selectors for rent per day
     * @param rentalCarServiceProvider name of car rental service provider
     * @return carsData
     */
    public static List<List<String>> getCarsData (Elements products, String[] rentURLAnchorTagSelectors,
                                                  String[] imageTagSelectors, String[] rentPerDayTagSelectors,
                                                  String rentalCarServiceProvider) {

        List<List<String>> carsData = new ArrayList<>();

        for(int i=0; i<products.size(); ++i){
            List<String> carData = new ArrayList<>();

            // Get the car rent url
            Elements rentURLAnchorTag = products.get(i).select(rentURLAnchorTagSelectors[0]);
            String rentURL = rentURLAnchorTag.attr(rentURLAnchorTagSelectors[1]);
            carData.add(rentURL);

            // Get the car image url
            Elements imageTag = products.get(i).select(imageTagSelectors[0]);
            String imageURL = imageTag.attr(imageTagSelectors[1]);
            carData.add(imageURL);

            // Get the rent cost per day
            Elements rentPerDayTag = products.get(i).select(rentPerDayTagSelectors[0]);
            String rentPerDay = rentPerDayTag.text().replaceAll("[^0-9.]", "");
            carData.add(rentPerDay);

            // Get the car brand and model
            String carBrandAndModel = rentURLAnchorTag.text();
            carData.add(carBrandAndModel);

            // Car service provider
            carData.add(rentalCarServiceProvider);

            // Add car to cars list
            carsData.add(carData);
        }
        return carsData;
    }


    /**
     * Scrapes given website and returns web elements with products data.
     * @param searchURL website search URL
     * @param brandName brand name of car that we want to scrape
     * @param productCSSClasses used as product element identifier
     * @return products
     */
    public static Elements scrapeWebsite(String searchURL, String brandName, String productCSSClasses) throws Exception{

        // Download HTML document from website
        Document htmlDoc = Jsoup.connect(searchURL + brandName).get();

        // Get all the products on the page
        Elements products = htmlDoc.select(productCSSClasses);

        return products;
    }

}
