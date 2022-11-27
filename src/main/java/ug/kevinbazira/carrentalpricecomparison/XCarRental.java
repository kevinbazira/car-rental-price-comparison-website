package ug.kevinbazira.carrentalpricecomparison;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Car data scraped from XCarRental website.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
public class XCarRental {

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
    /*public static List<String> getCarBrands (Elements products, String[] rentURLAnchorTagSelectors,
                                                  String[] imageTagSelectors, String[] rentPerDayTagSelectors,
                                                  String rentalCarServiceProvider) {

        List<String> carBrands = new ArrayList<>();

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
            carBrands.add(carData);
        }
        return carBrands;
    }*/
    public static void main(String[] args) {

        String brandName = "Lamborghini";
        String searchURL = "https://xcarrental.com/rent-a-car-dubai/?brand=" + brandName;
        String carElementCSSClasses = ".element.grid";
        String[] rentURLAnchorTagSelectors = {".car_link", "href"};
        String[] imageTagSelectors = {".car_image>img", "src"};
        String[] rentPerDayTagSelectors = {".single_car_price", ""};
        String rentalCarServiceProvider = "X Car Rental";

        WebScraper XCarRentalScraper = new WebScraper();

        try {
            Elements products = XCarRentalScraper.scrapeWebsite(searchURL, carElementCSSClasses);
            System.out.println("XCarRentalData: " + XCarRentalScraper.getCarsData(products, rentURLAnchorTagSelectors, imageTagSelectors, rentPerDayTagSelectors, rentalCarServiceProvider));
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }
}
