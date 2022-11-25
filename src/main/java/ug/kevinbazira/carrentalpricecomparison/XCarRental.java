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

        String searchURL = "https://xcarrental.com/rent-a-car-dubai/?brand=";
        String brandName = "Lamborghini";
        String productCSSClasses = ".element.grid";
        String[] rentURLAnchorTagSelectors = {".car_link", "href"};
        String[] imageTagSelectors = {".car_image>img", "src"};
        String[] rentPerDayTagSelectors = {".single_car_price", ""};
        String rentalCarServiceProvider = "X Car Rental";

        WebScraper XCarRentalScraper = new WebScraper();

        try {
            Elements products = XCarRentalScraper.scrapeWebsite(searchURL, brandName, productCSSClasses);
            System.out.println("XCarRentalData: " + XCarRentalScraper.getCarsData(products, rentURLAnchorTagSelectors, imageTagSelectors, rentPerDayTagSelectors, rentalCarServiceProvider));
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }
}
