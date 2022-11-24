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
     * Traverses XCarRental website products HTML elements and returns cars data list with
     * rentURL, imageURL, rentPerDay, carBrandAndModel, and serviceProvider for all cars
     * found on search page.
     * @param products web elements with products data
     * @return carsData
     */
    public static List<List<String>> XCarRentalData (Elements products) {
        List<List<String>> carsData = new ArrayList<>();

        for(int i=0; i<products.size(); ++i){
            List<String> carData = new ArrayList<>();

            // Get the car rent url
            Elements rentURLAnchorTag = products.get(i).select(".car_link");
            String rentURL = rentURLAnchorTag.attr("href");
            carData.add(rentURL);

            // Get the car image url
            Elements imageTag = products.get(i).select(".car_image>img");
            String imageURL = imageTag.attr("src");
            carData.add(imageURL);

            // Get the rent cost per day
            Elements rentPerDaySpanTag = products.get(i).select(".single_car_price");
            String rentPerDay = rentPerDaySpanTag.text();
            carData.add(rentPerDay);

            // Get the car brand and model
            String carBrandAndModel = rentURLAnchorTag.text();
            carData.add(carBrandAndModel);

            // Car service provider
            carData.add("X Car Rental");

            // Add car to cars list
            carsData.add(carData);
        }
        return carsData;
    }

    public static void main(String[] args) {

        String searchURL = "https://xcarrental.com/rent-a-car-dubai/?brand=";
        String brandName = "Lamborghini";
        String cssClasses = ".element.grid";

        WebScraper XCarRentalScraper = new WebScraper();

        try {
            Elements products = XCarRentalScraper.scrapeWebsite(searchURL, brandName, cssClasses);
            System.out.println("XCarRentalData: " + XCarRentalData(products));
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }
}
