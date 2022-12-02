package ug.kevinbazira.carrentalpricecomparison;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * TODO
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
public class Main {

    /**
     * Add car hire details to the database.
     * @param brandName car brand name
     * @param carsData list of cars and their car hire details
     * @param webScraper web scraper object of given website
     * @param priceComparisonDB object used manipulate data in price comparison database
     */
    public static void addCarHireDetailsToDB(String brandName, List<List<String>> carsData,WebScraper webScraper, PriceComparisonDB priceComparisonDB){
        // Loop through each car's hire details and add the car model to the db
        for(int i = 0; i < carsData.size(); i++){
            List<String> carData = carsData.get(i);
            // Get car model object from db. Its ID is to be used as a foreign key.
            String brandAndModelName = carData.get(3);
            String carModelName = webScraper.extractModelName(brandAndModelName, brandName);
            CarModel carModel = priceComparisonDB.getCarModel(carModelName);
            // Get rent per day
            int rentPerDay = Integer.parseInt(carData.get(2));
            // Get rental service provider details
            String rentalServiceName = carData.get(4);
            // Get rental service object from db. Its ID is to be used as a foreign key.
            RentalService rentalService = priceComparisonDB.getRentalService(rentalServiceName);
            System.out.println("rentalService: " + rentalService.getId());
            // Get rent URL
            String rentURL = carData.get(0);
            // Add car model details to the db
            priceComparisonDB.addCarData(carModel, rentPerDay, rentalService, rentURL);
        }
    }

    /**
     * Add car model details to the database.
     * @param brandName car brand name
     * @param carsData list of cars and their car hire details
     * @param webScraper web scraper object of given website
     * @param priceComparisonDB object used manipulate data in price comparison database
     */
    public static void addCarModelDetailsToDB(String brandName, List<List<String>> carsData,WebScraper webScraper, PriceComparisonDB priceComparisonDB){
        // Loop through each car's hire details and add the car model to the db
        for(int i = 0; i < carsData.size(); i++){
            List<String> carData = carsData.get(i);
            // Get model image URL
            String carModelImageURL = carData.get(1);
            // Get brand and model name
            String brandAndModelName = carData.get(3);
            // Extract model name from brand and model name
            String modelName = webScraper.extractModelName(brandAndModelName, brandName);
            // Only add model name to db if it's connected to the brand
            if(!modelName.isEmpty()){
                // Get model's car brand object from db. Its ID is to be used as a foreign key.
                CarBrand modelCarBrandObj = priceComparisonDB.getCarBrand(brandName);
                // Add car model details to the db
                priceComparisonDB.addCarModel(modelName, modelCarBrandObj, carModelImageURL);
            }
        }
    }
    public static void main(String[] args) {

        // Start program timer
        long programStartTime = System.nanoTime();

        // Create a new instance of the PriceComparisonDB
        PriceComparisonDB priceComparisonDB = new PriceComparisonDB();

        // Set up the db SessionFactory
        priceComparisonDB.init();

        // Instruct Spring to create and wire beans using annotations.
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Get XCarRental bean
        WebScraper xCarRental = (WebScraper) context.getBean("XCarRental");
        // Add car rental service provider to the db
        priceComparisonDB.addRentalService(xCarRental.getRentalCarServiceProvider(), xCarRental.getBrandsSearchURL());
        // Get scraped car brands from XCarRental website and add them to the db
        List<String> xCarRentalCarBrands = xCarRental.getCarBrands();
        // Add car brands to db
        priceComparisonDB.addCarBrands(xCarRentalCarBrands);
        /*
        Loop through each car brand, get scraped car data and
        use it to add the car model + car hire details to the db.
         */
        for(int i = 0; i < xCarRentalCarBrands.size(); i++){
            String brandName = xCarRentalCarBrands.get(i);
            // Get scraped car hire details from XCarRental website
            List<List<String>> xCarRentalCarsData = xCarRental.getCarsData(brandName);
            // Add car model details to database
            addCarModelDetailsToDB(brandName, xCarRentalCarsData, xCarRental, priceComparisonDB);
            // Add car hire details to database
            addCarHireDetailsToDB(brandName, xCarRentalCarsData, xCarRental, priceComparisonDB);
        }



        /*// Add car model details to database
        String brandName = "Lamborghini"; // Lamborghini, Audi
        List<List<String>> xCarRentalCarsData = xCarRental.getCarsData(brandName);
        addCarModelDetailsToDB(brandName, xCarRentalCarsData, xCarRental, priceComparisonDB);
        */

       /* // Add car hire details to database
        String brandName = "Lamborghini";
        List<List<String>> xCarRentalCarsData = xCarRental.getCarsData(brandName);
        addCarHireDetailsToDB(brandName, xCarRentalCarsData, xCarRental, priceComparisonDB);
        */

        // FINALLY WORKED ... phew!!!
        /*CarBrand tmpCarBrand = priceComparisonDB.getCarBrand("Lamborghini");
        System.out.println("tmpCarBrand ID: " + tmpCarBrand.getId());
        */


        // >>> car model === scraped car name - brand name
        // >>> get brand names from db and loop through each to get car details
        // get brandAndModel text > split it to brandName and modelName > get brandName (skipped for now. loop required.) > add modelName + brandNameID to DB

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

        // End program timer
        long programRunTime = System.nanoTime() - programStartTime;
        System.out.println("This program run for: " + programRunTime + " nanoseconds.");


    }
}