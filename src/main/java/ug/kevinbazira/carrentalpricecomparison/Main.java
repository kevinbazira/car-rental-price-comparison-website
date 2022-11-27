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

        // Shut down db SessionFactory
        priceComparisonDB.shutDown();

    }
}