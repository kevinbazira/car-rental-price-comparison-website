package ug.kevinbazira.carrentalpricecomparison;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * Main class for this web scraper program.
 * It's mainly :D used to glue everything together from: scraping the websites; to transforming data; and loading it into the database.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
public class Main {

    /**
     * Extract/Scrape Transform Load (ETL).
     * This method glues everything together:
     * 1. Calls the scraper that extracts car hire details from a website.
     * 2. Calls methods that transform scraped data into details (like car brand, model, service provider, rent per day, etc) that can be added to the db.
     * 3. Calls the load methods that add transformed car hire details to their respective database entities.
     * @param webScraper web scraper object of given website
     * @param priceComparisonDB object used to manipulate data in price comparison database
     */
    public static void extractTransformLoad(WebScraper webScraper, PriceComparisonDB priceComparisonDB){
        // Add car rental service provider to the db
        priceComparisonDB.addRentalService(webScraper.getRentalCarServiceProvider(), webScraper.getBrandsSearchURL());
        // Get scraped car brands from given website and add them to the db
        List<String> rentalCarBrands = webScraper.getCarBrands();
        // Add car brands to db
        priceComparisonDB.addCarBrands(rentalCarBrands);
        /*
        Loop through each car brand, get scraped car data and
        use it to add the car model + car hire details to the db.
         */
        for(int i = 0; i < rentalCarBrands.size(); i++){
            // Get brand name.
            String brandName = rentalCarBrands.get(i);
            // Get scraped car hire details from a given website. NB: The Drivus website displays brand names but uses brand IDs for search. The Quick Drive website requires hyphens in brands for search.
            String searchBrandName = "";
            if(webScraper.getRentalCarServiceProvider().equals("Drivus")){
                searchBrandName = String.valueOf(webScraper.getBrandNameIDs().get(brandName));
            } else if (webScraper.getRentalCarServiceProvider().equals("Quick Drive")){
                searchBrandName = brandName.replace(" ", "-");
            } else {
                searchBrandName = brandName;
            }
            List<List<String>> rentalCarsData = webScraper.getCarsData(searchBrandName);
            // Add car model details to database
            addCarModelDetailsToDB(brandName, rentalCarsData, webScraper, priceComparisonDB);
            // Add car hire details to database
            addCarHireDetailsToDB(brandName, rentalCarsData, webScraper, priceComparisonDB);
        }

    }

    /**
     * Add car hire details to the database.
     * @param brandName car brand name
     * @param carsData list of cars and their car hire details
     * @param webScraper web scraper object of given website
     * @param priceComparisonDB object used to manipulate data in price comparison database
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
     * @param priceComparisonDB object used to manipulate data in price comparison database
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

    /**
     * Main method that is running ETLs on the following websites:
     * 1. XCarRental
     * 2. RentalCarsUAE
     * 3. Drivus
     * 4. PhantomRentCar
     * 5. QuickDrive
     * @param args
     */
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
        // Extract/scrape data from the XCarRental Website, transform it, and load/add it to the db.
         extractTransformLoad(xCarRental, priceComparisonDB);

        // Get RentalCarsUAE bean
        WebScraper rentalCarsUAE = (WebScraper) context.getBean("RentalCarsUAE");
        // Extract/scrape data from the RentalCarsUAE Website, transform it, and load/add it to the db.
         extractTransformLoad(rentalCarsUAE, priceComparisonDB);

        // Get Drivus bean
        WebScraper drivus = (WebScraper) context.getBean("Drivus");
        // Extract/scrape data from the Drivus Website, transform it, and load/add it to the db.
         extractTransformLoad(drivus, priceComparisonDB);

        // Get PhantomRentCar bean
        WebScraper phantomRentCar = (WebScraper) context.getBean("PhantomRentCar");
        // Extract/scrape data from the PhantomRentCar Website, transform it, and load/add it to the db.
         extractTransformLoad(phantomRentCar, priceComparisonDB);

        // Get QuickDrive bean
        WebScraper quickDrive = (WebScraper) context.getBean("QuickDrive");
        // Extract/scrape data from the QuickDrive Website, transform it, and load/add it to the db.
         extractTransformLoad(quickDrive, priceComparisonDB);

        // Shut down the db SessionFactory
        priceComparisonDB.shutDown();

        // End program timer
        long programRunTime = System.nanoTime() - programStartTime;
        System.out.println("This program run for: " + programRunTime + " nanoseconds.");

    }
}