package ug.kevinbazira.carrentalpricecomparison;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Create a new instance of the PriceComparisonDB
        PriceComparisonDB priceComparisonDB = new PriceComparisonDB();

        // Set up the db SessionFactory
        priceComparisonDB.init();

        // Instruct Spring to create and wire beans using annotations.
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Get XCarRental bean
        WebScraper xCarRental = (WebScraper) context.getBean("XCarRental");
        // Get scraped car brands from XCarRental website and add them to the db
        List<String> xCarRentalCarBrands = xCarRental.getCarBrands();
        priceComparisonDB.addCarBrands(xCarRentalCarBrands);

        //Call methods on XCarRental bean - NEXT figure out a way to add these to the DB, then you'll loop through individual brands using the AppConfig section
        // System.out.println("xCarRental.getCarBrands(): " + xCarRental.getCarBrands());
        // System.out.println("xCarRental.getCarsData(): " + xCarRental.getCarsData());





        // Run db transactions
        // priceComparisonDB.addCarBrand("Mitsubishi");
        // priceComparisonDB.addCarBrands(new String[]{"Land Cruiser", "Range Rover", "Mitsubishi"});
        // priceComparisonDB.addCarModel();
        // priceComparisonDB.addRentalService();
        // priceComparisonDB.addCarData();

        // Shut down db SessionFactory
        priceComparisonDB.shutDown();


    }
}