package ug.kevinbazira.carrentalpricecomparison;

import org.springframework.context.annotation.*;

@Configuration
public class AppConfig {
    /*
    @Bean
    public Car myCar(){
        Car tmpCar = new Car();
        tmpCar.setColour("Green");
        tmpCar.setManufacturer("Ford");
        tmpCar.setOwner(myPerson());
        return tmpCar;
    }
    */
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

}
