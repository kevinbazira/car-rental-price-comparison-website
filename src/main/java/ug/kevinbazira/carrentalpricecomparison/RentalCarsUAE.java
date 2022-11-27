package ug.kevinbazira.carrentalpricecomparison;

import org.jsoup.select.Elements;

/**
 * Car data scraped from RentalCarsUAE website.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
public class RentalCarsUAE {

    public static void main(String[] args) {

        String brandName = "Toyota";
        String searchURL = "https://rentalcarsuae.com/car-search/?category=all_cars&brandd=" + brandName;
        String carElementCSSClasses = ".carsItem";
        String[] rentURLAnchorTagSelectors = {".carTitle>p>a", "href"};
        String[] imageTagSelectors = {".carImageWrapper>a>img", "nitro-lazy-src"};
        String[] rentPerDayTagSelectors = {".special_price :eq(1)", ""};
        String rentalCarServiceProvider = "Rental Cars UAE";

        WebScraper RentalCarsUAEScraper = new WebScraper();

        try {
            Elements products = RentalCarsUAEScraper.scrapeWebsite(searchURL, carElementCSSClasses);
            System.out.println("RentalCarsUAEData: " + RentalCarsUAEScraper.getCarsData(products, rentURLAnchorTagSelectors, imageTagSelectors, rentPerDayTagSelectors, rentalCarServiceProvider));
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }
}
