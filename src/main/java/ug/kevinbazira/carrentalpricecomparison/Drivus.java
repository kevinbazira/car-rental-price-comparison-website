package ug.kevinbazira.carrentalpricecomparison;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Car data scraped from Drivus website.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
public class Drivus {

    /**
     * Traverses Drivus website products HTML elements and returns cars data list with
     * rentURL, imageURL, rentPerDay, carBrandAndModel, and serviceProvider for all cars
     * found on search page.
     * @param products web elements with products data
     * @return carsData
     */
    public static List<List<String>> DrivusData (Elements products) {
        List<List<String>> carsData = new ArrayList<>();

        for(int i=0; i<products.size(); ++i){
            List<String> carData = new ArrayList<>();

            //Get the car rent url
            Elements rentURLAnchorTag = products.get(i).select(".c-product-panel__title>a");
            String rentURL = rentURLAnchorTag.attr("href");
            carData.add(rentURL);

            //Get the car image url
            Elements imageTag = products.get(i).select(".c-product-panel__img>a>img");
            String imageURL = imageTag.attr("data-src");
            carData.add(imageURL);

            //Get the rent cost per day
            Elements rentPerDayDivTag = products.get(i).select(".c-product-panel__prices>ul :eq(2) .c-product-panel__price");
            String rentPerDay = rentPerDayDivTag.text().replaceAll("[^0-9.]", "");
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

        Map<String, Integer> brandNameIDs = new HashMap<>();
        brandNameIDs.put("KIA", 2);
        brandNameIDs.put("Nissan", 4);
        brandNameIDs.put("Toyota", 5);
        brandNameIDs.put("Hyundai", 6);
        brandNameIDs.put("Land Rover", 14);
        brandNameIDs.put("Mitsubishi", 15);
        brandNameIDs.put("Renault", 16);
        brandNameIDs.put("MG", 18);
        String searchURL = "https://www.drivus.ae/catalog/?" +
                "catalog_filter%5Bfrom%5D=145&catalog_filter%5Bto%5D=19999&" +
                "catalog_filter%5Bperiod%5D=monthly&catalog_filter%5Bdate_from%5D=&" +
                "catalog_filter%5Bdate_to%5D=&catalog_filter%5Bage%5D=1&" +
                "catalog_filter%5Bvendors%5D%5B%5D=";
        String brandName = "Hyundai";
        String brandID = String.valueOf(brandNameIDs.get(brandName));
        String cssClasses = ".c-product-panel.c-product-panel--row";

        WebScraper DrivusScraper = new WebScraper();

        try {
            Elements products = DrivusScraper.scrapeWebsite(searchURL, brandID, cssClasses);
            System.out.println("DrivusData: " + DrivusData(products.not(".featured")));
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }
}

