package ug.kevinbazira.carrentalpricecomparison;

import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * Car data scraped from Drivus website.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
public class Drivus {

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
        String brandName = "Hyundai";
        String brandID = String.valueOf(brandNameIDs.get(brandName));
        String searchURL = "https://www.drivus.ae/catalog/?" +
                "catalog_filter%5Bfrom%5D=145&catalog_filter%5Bto%5D=19999&" +
                "catalog_filter%5Bperiod%5D=monthly&catalog_filter%5Bdate_from%5D=&" +
                "catalog_filter%5Bdate_to%5D=&catalog_filter%5Bage%5D=1&" +
                "catalog_filter%5Bvendors%5D%5B%5D=" + brandID;
        String carElementCSSClasses = ".c-product-panel.c-product-panel--row";
        String[] rentURLAnchorTagSelectors = {".c-product-panel__title>a", "href"};
        String[] imageTagSelectors = {".c-product-panel__img>a>img", "data-src"};
        String[] rentPerDayTagSelectors = {".c-product-panel__prices>ul :eq(2) .c-product-panel__price", ""};
        String rentalCarServiceProvider = "DrivusData";

        WebScraper DrivusScraper = new WebScraper();

        try {
            Elements carsHTMLElements = DrivusScraper.scrapeWebsite(searchURL, carElementCSSClasses);
            System.out.println("DrivusData: " + DrivusScraper.getCarsData(carsHTMLElements.not(".featured"), rentURLAnchorTagSelectors, imageTagSelectors, rentPerDayTagSelectors, rentalCarServiceProvider));
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }
}

