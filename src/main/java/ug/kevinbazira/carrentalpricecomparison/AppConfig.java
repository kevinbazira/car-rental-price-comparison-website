package ug.kevinbazira.carrentalpricecomparison;

import org.springframework.context.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    /**
     * Dependency injection for properties that will be used to scrape
     * cars for hire from the XCarRental website.
     */
    @Bean
    public WebScraper XCarRental(){

        WebScraper tmpXCarRental = new WebScraper();

        // configure properties to scrape rental car brands
        tmpXCarRental.setBrandsSearchURL("https://xcarrental.com/rent-a-car-dubai/");
        tmpXCarRental.setBrandsElementCSSClasses("select#brand>option");
        tmpXCarRental.setNonBrandName("Any Brand");

        // configure properties to scrape car hire details
        tmpXCarRental.setCarsSearchURL("https://xcarrental.com/rent-a-car-dubai/?brand=");
        tmpXCarRental.setCarElementCSSClasses(".element.grid");
        tmpXCarRental.setRentURLAnchorTagSelectors(new String[]{".car_link", "href"});
        tmpXCarRental.setImageTagSelectors(new String[]{".car_image>img", "src"});
        tmpXCarRental.setRentPerDayTagSelectors(new String[]{".single_car_price", ""});
        tmpXCarRental.setRentalCarServiceProvider("X Car Rental");

        return tmpXCarRental;

    }

    /**
     * Dependency injection for properties that will be used to scrape
     * cars for hire from the RentalCarsUAE website.
     */
    @Bean
    public WebScraper RentalCarsUAE(){

        WebScraper tmpRentalCarsUAE = new WebScraper();

        // configure properties to scrape rental car brands
        tmpRentalCarsUAE.setBrandsSearchURL("https://rentalcarsuae.com/car-search/?category=all_cars");
        tmpRentalCarsUAE.setBrandsElementCSSClasses("select[name=\"brandd\"]>option");
        tmpRentalCarsUAE.setNonBrandName("All Brands");

        // configure properties to scrape car hire details
        tmpRentalCarsUAE.setCarsSearchURL("https://rentalcarsuae.com/car-search/?category=all_cars&brandd=");
        tmpRentalCarsUAE.setCarElementCSSClasses(".carsItem");
        tmpRentalCarsUAE.setRentURLAnchorTagSelectors(new String[]{".carTitle>p>a", "href"});
        tmpRentalCarsUAE.setImageTagSelectors(new String[]{".carImageWrapper>a>img", "src"});
        tmpRentalCarsUAE.setRentPerDayTagSelectors(new String[]{".special_price :eq(1)", ""});
        tmpRentalCarsUAE.setRentalCarServiceProvider("Rental Cars UAE");

        return tmpRentalCarsUAE;

    }

    /**
     * Dependency injection for properties that will be used to scrape
     * cars for hire from the Drivus website.
     */
    @Bean
    public WebScraper Drivus(){

        WebScraper tmpDrivus = new WebScraper();

        // configure properties to scrape rental car brands
        tmpDrivus.setBrandsSearchURL("https://www.drivus.ae/catalog/");
        tmpDrivus.setBrandsElementCSSClasses(".c-form-filter__brands .c-checkbox-brand__label");
        tmpDrivus.setNonBrandName("MG");
        Map<String, Integer> brandNameIDs = new HashMap<>();
        brandNameIDs.put("KIA", 2);
        brandNameIDs.put("Nissan", 4);
        brandNameIDs.put("Toyota", 5);
        brandNameIDs.put("Hyundai", 6);
        brandNameIDs.put("Land Rover", 14);
        brandNameIDs.put("Mitsubishi", 15);
        brandNameIDs.put("Renault", 16);
        tmpDrivus.setBrandNameIDs(brandNameIDs);

        // configure properties to scrape car hire details
        tmpDrivus.setCarsSearchURL("https://www.drivus.ae/catalog/?" +
                                    "catalog_filter%5Bfrom%5D=145&catalog_filter%5Bto%5D=19999&" +
                                    "catalog_filter%5Bperiod%5D=monthly&catalog_filter%5Bdate_from%5D=&" +
                                    "catalog_filter%5Bdate_to%5D=&catalog_filter%5Bage%5D=1&" +
                                    "catalog_filter%5Bvendors%5D%5B%5D=");
        tmpDrivus.setCarElementCSSClasses(".c-product-panel.c-product-panel--row");
        tmpDrivus.setRentURLAnchorTagSelectors(new String[]{".c-product-panel__title>a", "href"});
        tmpDrivus.setImageTagSelectors(new String[]{".c-product-panel__img>a>img", "data-src"});
        tmpDrivus.setRentPerDayTagSelectors(new String[]{".c-product-panel__prices>ul :eq(2) .c-product-panel__price", ""});
        tmpDrivus.setRentalCarServiceProvider("Drivus");

        return tmpDrivus;

    }

}
