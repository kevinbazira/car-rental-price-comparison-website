package ug.kevinbazira.carrentalpricecomparison;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

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

        // Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        // Start transaction
        session.beginTransaction();

        // Add a database entity to the db session. NB: It will not be stored until the transaction is committed.
        session.save(dbEntity);

        // Commit transaction to save it to database
        session.getTransaction().commit();

        // Close the session and release database connection
        session.close();

        // Print success message
        System.out.println(dbEntity.getClass().getSimpleName() + " database transaction completed successfully!");
    }

    /**
     * Adds a new car brand to the database
     */
    public void addCarBrand(){

        // Create an instance of a car brand
        CarBrand carBrand = new CarBrand();

        // Set values of a car brand to be added to db tbl
        carBrand.setId(1);
        carBrand.setName("Range Rover");

        // Run database transaction to add car brand to db
        runDatabaseTransaction(carBrand);

    }

    /**
     * Adds a new car model to the database
     */
    public void addCarModel(){

        // Create an instance of a car model
        CarModel carModel = new CarModel();

        // Set values of a car brand to be added to db tbl
        carModel.setId(1);
        carModel.setName("Sport 2024");
        carModel.setCarBrandID(3);
        carModel.setImageURL("http://website-domain.com/url-to-image.jpg");

        // Run database transaction to add car model to db
        runDatabaseTransaction(carModel);
    }

    /**
     * Adds a new car rental service company to the database
     */
    public void addRentalService(){

        // Create an instance of a car rental service
        RentalService rentalService = new RentalService();

        // Set values of a car rental service to be added to db tbl
        rentalService.setId(1);
        rentalService.setName("Drivus");
        rentalService.setWebsite("https://website-domain.com");

        // Run database transaction to add car rental service to db
        runDatabaseTransaction(rentalService);
    }

}
