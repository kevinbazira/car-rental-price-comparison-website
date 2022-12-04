package ug.kevinbazira.carrentalpricecomparison;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
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
    public static void etl(WebScraper webScraper, PriceComparisonDB priceComparisonDB){
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

        /*** Get XCarRental bean
        WebScraper xCarRental = (WebScraper) context.getBean("XCarRental");
        // Extract/scrape data from the XCarRental Website, transform it, and load/add it to the db.
        etl(xCarRental, priceComparisonDB);

        // Get RentalCarsUAE bean
        WebScraper rentalCarsUAE = (WebScraper) context.getBean("RentalCarsUAE");
        // Extract/scrape data from the RentalCarsUAE Website, transform it, and load/add it to the db.
        etl(rentalCarsUAE, priceComparisonDB);

        // Get Drivus bean
        WebScraper drivus = (WebScraper) context.getBean("Drivus");
        // Extract/scrape data from the Drivus Website, transform it, and load/add it to the db.
        etl(drivus, priceComparisonDB);

        // Get PhantomRentCar bean
        WebScraper phantomRentCar = (WebScraper) context.getBean("PhantomRentCar");
        // Extract/scrape data from the PhantomRentCar Website, transform it, and load/add it to the db.
        etl(phantomRentCar, priceComparisonDB);

        // Get QuickDrive bean
        WebScraper quickDrive = (WebScraper) context.getBean("QuickDrive");
        // Extract/scrape data from the QuickDrive Website, transform it, and load/add it to the db.
        etl(quickDrive, priceComparisonDB);
        */

        // List<String> quickDriveCarBrands = quickDrive.getCarBrands(); // PASSED!!!
        // List<List<String>> quickDriveCarsData = quickDrive.getCarsData("Rolls-Royce");
        // System.out.println("quickDriveCarsData: " + quickDriveCarsData);


        // List<String> phantomRentCarCarBrands = phantomRentCar.getCarBrands(); // PASSED!!!
        // List<List<String>> phantomRentCarCarsData = phantomRentCar.getCarsData("Range Rover");
        // System.out.println("phantomRentCarCarsData: " + phantomRentCarCarsData);

        /*String brandName = "KIA";
        // List<String> drivusCarBrands = drivus.getCarBrands();
        List<List<String>> drivusCarsData = drivus.getCarsData(String.valueOf(drivus.getBrandNameIDs().get(brandName)));
        for(int i = 0; i < drivusCarsData.size(); i++){
            List<String> carData = drivusCarsData.get(i);
            // Get model image URL
            String carModelImageURL = carData.get(1);
            // Get brand and model name
            String brandAndModelName = carData.get(3);
            // Extract model name from brand and model name
            String modelName = drivus.extractModelName(brandAndModelName, brandName);
            // Only add model name to db if it's connected to the brand
            if(!modelName.isEmpty()){
                // Get model's car brand object from db. Its ID is to be used as a foreign key.
                CarBrand modelCarBrandObj = priceComparisonDB.getCarBrand(brandName);
                // Add car model details to the db
                System.out.println("BrandID: " + modelCarBrandObj.getId());
            }
        }*/
        // Get scraped car hire details from XCarRental website
        // List<List<String>> drivusCarsData = drivus.getCarsData(String.valueOf(drivus.getBrandNameIDs().get(brandName)));
        // System.out.println(drivusCarBrands);



        /*** Add car rental service provider to the db
        priceComparisonDB.addRentalService(xCarRental.getRentalCarServiceProvider(), xCarRental.getBrandsSearchURL());
        // Get scraped car brands from XCarRental website and add them to the db
        List<String> xCarRentalCarBrands = xCarRental.getCarBrands();
        // Add car brands to db
        priceComparisonDB.addCarBrands(xCarRentalCarBrands);
        // Loop through each car brand, get scraped car data and
        // use it to add the car model + car hire details to the db.
        for(int i = 0; i < xCarRentalCarBrands.size(); i++){
            String brandName = xCarRentalCarBrands.get(i);
            // Get scraped car hire details from XCarRental website
            List<List<String>> xCarRentalCarsData = xCarRental.getCarsData(brandName);
            // Add car model details to database
            addCarModelDetailsToDB(brandName, xCarRentalCarsData, xCarRental, priceComparisonDB);
            // Add car hire details to database
            addCarHireDetailsToDB(brandName, xCarRentalCarsData, xCarRental, priceComparisonDB);
        }
        */


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