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
     * Traverses website brands list of HTML elements and returns car brand names list.
     * @param brandsHTMLElements web elements with car brands data
     * @return carBrands
     */
    public static List<String> getCarBrands (Elements brandsHTMLElements) {

        List<String> carBrands = new ArrayList<>();

        for(int i=0; i<brandsHTMLElements.size(); ++i){
            // Get a car brand
            String carBrandName = brandsHTMLElements.get(i).text();
            carBrands.add(carBrandName);
        }
        return carBrands;
    }

    /**
     * Traverses website products listing HTML elements and returns cars data list with
     * rentURL, imageURL, rentPerDay, carBrandAndModel, and serviceProvider for all cars
     * found on search page.
     * @param carsHTMLElements web elements with cars data
     * @param rentURLAnchorTagSelectors css selectors for URL to rent car
     * @param imageTagSelectors css selectors for car image
     * @param rentPerDayTagSelectors css selectors for rent per day
     * @param rentalCarServiceProvider name of car rental service provider
     * @return carsData
     */
    public static List<List<String>> getCarsData (Elements carsHTMLElements, String[] rentURLAnchorTagSelectors,
                                                  String[] imageTagSelectors, String[] rentPerDayTagSelectors,
                                                  String rentalCarServiceProvider) {

        List<List<String>> carsData = new ArrayList<>();

        for(int i=0; i<carsHTMLElements.size(); ++i){
            List<String> carData = new ArrayList<>();

            // Get the car rent url
            Elements rentURLAnchorTag = carsHTMLElements.get(i).select(rentURLAnchorTagSelectors[0]);
            String rentURL = rentURLAnchorTag.attr(rentURLAnchorTagSelectors[1]);
            carData.add(rentURL);

            // Get the car image url
            Elements imageTag = carsHTMLElements.get(i).select(imageTagSelectors[0]);
            String imageURL = imageTag.attr(imageTagSelectors[1]);
            carData.add(imageURL);

            // Get the rent cost per day
            Elements rentPerDayTag = carsHTMLElements.get(i).select(rentPerDayTagSelectors[0]);
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
     * Scrapes given website and returns web elements.
     * In our case it returns an html element with either car brands or cars list.
     * @param searchURL website search URL
     * @param elementCSSClasses used as car brand or cars list html elements identifier
     * @return scraped html elements
     */
    public static Elements scrapeWebsite(String searchURL, String elementCSSClasses) throws Exception{

        // Download HTML document from website
        Document htmlDoc = Jsoup.connect(searchURL).get();

        // Get all the elements on the page
        Elements htmlElements = htmlDoc.select(elementCSSClasses);

        return htmlElements;
    }

}
