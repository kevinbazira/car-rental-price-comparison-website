package ug.kevinbazira.carrentalpricecomparison;

public class Main {
    public static void main(String[] args) {

        // Create a new instance of the PriceComparisonDB
        PriceComparisonDB priceComparisonDB = new PriceComparisonDB();

        // Set up the db SessionFactory
        priceComparisonDB.init();

        // Run db transactions
        // priceComparisonDB.addCarBrand();
        // priceComparisonDB.addCarModel();
        // priceComparisonDB.addRentalService();
        priceComparisonDB.addCarData();
        /*DONE*/ //priceComparisonDB.addCereal();//Add data
        /*DONE*/ //priceComparisonDB.updateCereal();//Updates data
        /*DONE*/ //priceComparisonDB.searchCereals();//Search for data
        /*DONE*/ //priceComparisonDB.deleteCerealSafe();//Delete data

        // Shut down db SessionFactory
        priceComparisonDB.shutDown();

    }
}