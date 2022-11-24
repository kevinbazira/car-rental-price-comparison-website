package ug.kevinbazira.carrentalpricecomparison;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Car data scraped from RentalCarsUAE website.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
public class RentalCarsUAE {

    /**
     * Traverses RentalCarsUAE website products HTML elements and returns cars data list with
     * rentURL, imageURL, rentPerDay, carBrandAndModel, and serviceProvider for all cars
     * found on search page.
     * @param products web elements with products data
     * @return carsData
     */
    public static List<List<String>> RentalCarsUAEData (Elements products) {
        List<List<String>> carsData = new ArrayList<>();

        for(int i=0; i<products.size(); ++i){
            List<String> carData = new ArrayList<>();

            //Get the car rent url
            Elements rentURLAnchorTag = products.get(i).select(".carTitle>p>a");
            String rentURL = rentURLAnchorTag.attr("href");
            carData.add(rentURL);

            //Get the car image url
            Elements imageTag = products.get(i).select(".carImageWrapper>a>img");
            String imageURL = imageTag.attr("nitro-lazy-src");
            carData.add(imageURL);

            //Get the rent cost per day
            Elements rentPerDayDivTag = products.get(i).select(".special_price :eq(1)");
            String rentPerDay = rentPerDayDivTag.text().replaceAll("[^0-9.]", "");;
            carData.add(rentPerDay);

            //Get the car brand and model
            String carBrandAndModel = rentURLAnchorTag.text();
            carData.add(carBrandAndModel);

            // Car service provider
            carData.add("Rental Cars UAE");

            // Add car to cars list
            carsData.add(carData);
        }
        return carsData;
    }

    public static void main(String[] args) {

        String searchURL = "https://rentalcarsuae.com/car-search/?category=all_cars&brandd=";
        String brandName = "Toyota";
        String cssClasses = ".carsItem";

        WebScraper RentalCarsUAEScraper = new WebScraper();

        try {
            Elements products = RentalCarsUAEScraper.scrapeWebsite(searchURL, brandName, cssClasses);
            System.out.println("RentalCarsUAEData: " + RentalCarsUAEData(products.not(".featured")));
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }
}
