package ug.kevinbazira.carrentalpricecomparison;

import org.jsoup.select.Elements;

/**
 * Car data scraped from XCarRental website.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
public class XCarRental {

    public static void main(String[] args) {

        String brandName = "Lamborghini";
        String carsSearchURL = "https://xcarrental.com/rent-a-car-dubai/?brand=" + brandName;
        String carElementCSSClasses = ".element.grid";
        String[] rentURLAnchorTagSelectors = {".car_link", "href"};
        String[] imageTagSelectors = {".car_image>img", "src"};
        String[] rentPerDayTagSelectors = {".single_car_price", ""};
        String rentalCarServiceProvider = "X Car Rental";

        WebScraper XCarRentalScraper = new WebScraper();

        /*try {
            Elements carsHTMLElements = XCarRentalScraper.scrapeWebsite(carsSearchURL, carElementCSSClasses);
            System.out.println("XCarRental Cars Data: " + XCarRentalScraper.getCarsData(carsHTMLElements, rentURLAnchorTagSelectors, imageTagSelectors, rentPerDayTagSelectors, rentalCarServiceProvider));
        } catch(Exception ex) {
            ex.printStackTrace();
        }*/

        String brandsSearchURL = "https://xcarrental.com/rent-a-car-dubai/";
        String brandsElementCSSClasses = "select#brand>option";

        try {
            Elements brandsHTMLElements = XCarRentalScraper.scrapeWebsite(brandsSearchURL, brandsElementCSSClasses);
            // Remember to remove "Any Brand" from the brands list
            System.out.println("XCarRental Brands Data: " + XCarRentalScraper.getCarBrands(brandsHTMLElements));
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }
}
