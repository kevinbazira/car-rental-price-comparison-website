package ug.kevinbazira.carrentalpricecomparison;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Represents a price comparison database.
 * Specifies the mapping between class objects and their respective tables in a price comparison database.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
public class PriceComparisonDB {

    // Creates new Sessions when there is need to interact with the database
    private SessionFactory sessionFactory;

    /**
     * Empty constructor
     */
    public PriceComparisonDB() {
    }

    /**
     * Set up a session factory.
     * Remember to call this method first when accessing the database.
     */
    public void init(){

        try {

            // Create a builder for the standard service registry
            StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

            // Load configuration from hibernate configuration file.
            // Here we are using a configuration file that specifies Java annotations.
            standardServiceRegistryBuilder.configure("hibernate.cfg.xml");

            // Create the registry that will be used to build the session factory
            StandardServiceRegistry registry = standardServiceRegistryBuilder.build();

            try {
                // Create the session factory - this is the goal of the init method.
                sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                // The registry would be destroyed by the SessionFactory,
                // but we had trouble building the SessionFactory, so destroy it manually
                System.err.println("Session Factory build failed.");
                e.printStackTrace();
                StandardServiceRegistryBuilder.destroy( registry );
            }

            // Ouput result
            System.out.println("Session factory built.");

        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("SessionFactory creation failed." + ex);
            ex.printStackTrace();
        }

    }

    /**
     * Close session factory and stop its threads from running.
     */
    public void shutDown(){
        sessionFactory.close();
    }

    /**
     * Run a database transaction on a database entity.
     * @dbEntity an object of a database entity e.g CarBrand, CarModel, etc.
     */
    public void runDatabaseTransaction(Object dbEntity){

        // Database entity name
        String dbEntityName = dbEntity.getClass().getSimpleName();

        // Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        // Start transaction
        session.beginTransaction();

        // Handle unique key constraint exceptions
        try{

            // Add a database entity to the db session. NB: It will not be stored until the transaction is committed.
            session.save(dbEntity);

            // Commit transaction to save it to database
            session.getTransaction().commit();

            // Print success message
            System.out.println(dbEntityName + " database entry completed successfully!");

        } catch(ConstraintViolationException ex) {
            System.err.println("Saving " + dbEntityName + " failed! :(");
            ex.printStackTrace();
        } finally {
            System.out.println("Closing " + dbEntityName + " database session");
        }

        // Close the session and release database connection
        session.close();
    }

    /**
     * Get a car brand details from the database based on the name.
     * @param brandName car brand name
     * @return carBrand
     */
    public CarBrand getCarBrand(String brandName){

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("from CarBrand where name=:name");
        query.setParameter("name", brandName);
        CarBrand carBrand = (CarBrand) query.uniqueResult();
        session.close();

        return carBrand;

    }

    /**
     * Adds a new car brand name to the database
     * @param brandName car brand name
     */
    public void addCarBrand(String brandName){

        // Create an instance of a car brand
        CarBrand carBrand = new CarBrand();

        // Set values of a car brand to be added to db tbl
        // ID column autopopulates so no need to set it
        carBrand.setName(brandName);

        // Run database transaction to add car brand to db
        runDatabaseTransaction(carBrand);

    }

    /**
     * Loops through a list of car brands and adds each brand to the database
     * @param brandNames car brand name
     */
    public void addCarBrands(List<String> brandNames){

        for(int i = 0; i < brandNames.size(); i++){
            // add a brand name to the db
            addCarBrand(brandNames.get(i));
        }

    }

    /**
     * Get a car model's details from the database based on the name.
     * @param modelName car model name
     * @return carModel
     */
    public CarModel getCarModel(String modelName){

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("from CarModel where name=:name");
        query.setParameter("name", modelName);
        CarModel carModel = (CarModel) query.uniqueResult();
        session.close();

        return carModel;

    }

    /**
     * Adds a new car model to the database
     * @param modelName car model name
     * @param carBrand car model brand
     * @param carImageURL car model image URL
     */
    public void addCarModel(String modelName, CarBrand carBrand, String carImageURL){

        // Create an instance of a car model
        CarModel carModel = new CarModel();

        // Set values of a car brand to be added to db tbl
        carModel.setName(modelName);
        carModel.setCarBrand(carBrand);
        carModel.setImageURL(carImageURL);

        // Run database transaction to add car model to db
        runDatabaseTransaction(carModel);
    }

    /**
     * Get a car rental service provider's details from the database based on the name.
     * @param rentalServiceProviderName car rental service provider name
     * @return rentalService
     */
    public RentalService getRentalService(String rentalServiceProviderName){

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query query = session.createQuery("from RentalService where name=:name");
        query.setParameter("name", rentalServiceProviderName);
        RentalService rentalService = (RentalService) query.uniqueResult();
        session.close();

        return rentalService;

    }

    /**
     * Adds a new car rental service company to the database
     * @param serviceProviderName car rentals service provider's name
     * @param serviceProviderURL car rentals service provider's website URL
     */
    public void addRentalService(String serviceProviderName, String serviceProviderURL){

        // Create an instance of a car rental service
        RentalService rentalService = new RentalService();

        // Set values of a car rental service to be added to db tbl
        rentalService.setName(serviceProviderName);
        rentalService.setWebsite(serviceProviderURL);

        // Run database transaction to add car rental service to db
        runDatabaseTransaction(rentalService);
    }

    /**
     * Adds a new car rental service company to the database
     * @param carModel car model object with name and id but id is what will be added to db
     * @param rentPerDay car rent per day
     * @param rentalService car rental service provider object with name and id but id is what will be added to db
     * @param rentURL car rent URL
     */
    public void addCarData(CarModel carModel, int rentPerDay, RentalService rentalService, String rentURL){

        // Create an instance of a car's data
        CarData carData = new CarData();

        // Set values of a car's data to be added to db tbl
        carData.setCarModel(carModel);
        carData.setRentPerDay(rentPerDay);
        carData.setRentalService(rentalService);
        carData.setRentURL(rentURL);
        carData.setDateScraped(Timestamp.from(Instant.now()));

        // Run database transaction to add a car's data to the db
        runDatabaseTransaction(carData);
    }

}
