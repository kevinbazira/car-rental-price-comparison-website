package ug.kevinbazira.carrentalpricecomparison.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ug.kevinbazira.carrentalpricecomparison.AppConfig;
import ug.kevinbazira.carrentalpricecomparison.WebScraper;

/**
 * Represents tests for the five web scrapers that use the WebScraper class.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Test WebScraper")
public class TestWebScraper {
    // Instruct Spring to create and wire beans using annotations.
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("Test Scraping Brands from XCarRental website")
    void testXCarRental() {
        WebScraper xCarRental = (WebScraper) context.getBean("XCarRental");
        // scrape car brands from xCarRental website
        List<String> xCarRentalCarBrands = xCarRental.getCarBrands();
        // confirm whether scraped car brands contain expected brands from this service provider.
        assertTrue(xCarRentalCarBrands.containsAll(Arrays.asList("Lamborghini", "Porsche", "Range Rover")));
    }

    @Test
    @DisplayName("Test Scraping Brands from RentalCarsUAE website")
    void testRentalCarsUAE() {
        WebScraper rentalCarsUAE = (WebScraper) context.getBean("RentalCarsUAE");
        // scrape car brands from rentalCarsUAE website
        List<String> rentalCarsUAECarBrands = rentalCarsUAE.getCarBrands();
        // confirm whether scraped car brands contain expected brands from this service provider.
        assertTrue(rentalCarsUAECarBrands.containsAll(Arrays.asList("Mitsubishi", "Renault", "Lexus")));
    }

    @Test
    @DisplayName("Test Scraping Brands from Drivus website")
    void testDrivus() {
        WebScraper drivus = (WebScraper) context.getBean("Drivus");
        // scrape car brands from Drivus website
        List<String> drivusCarBrands = drivus.getCarBrands();
        // confirm whether scraped car brands contain expected brands from this service provider.
        assertTrue(drivusCarBrands.containsAll(Arrays.asList("Nissan", "Toyota", "Hyundai")));
    }

    @Test
    @DisplayName("Test Scraping Brands from PhantomRentCar website")
    void testPhantomRentCar() {
        WebScraper phantomRentCar = (WebScraper) context.getBean("PhantomRentCar");
        // scrape car brands from xCarRental website
        List<String> phantomRentCarCarBrands = phantomRentCar.getCarBrands();
        // confirm whether scraped car brands contain expected brands from this service provider.
        assertTrue(phantomRentCarCarBrands.containsAll(Arrays.asList("GMC", "MCLAREN", "ROLLS ROYCE")));
    }

    @Test
    @DisplayName("Test Scraping Brands from QuickDrive website")
    void testQuickDrive() {
        WebScraper quickDrive = (WebScraper) context.getBean("QuickDrive");
        // scrape car brands from QuickDrive website
        List<String> quickDriveCarBrands = quickDrive.getCarBrands();
        // confirm whether scraped car brands contain expected brands from this service provider.
        assertTrue(quickDriveCarBrands.containsAll(Arrays.asList("Maserati", "Honda", "Volkswagen")));
    }

}
