package ug.kevinbazira.carrentalpricecomparison;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a web scraper.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
public class WebScraper {

    // properties used to scrape rental car brands
    private String brandsSearchURL;
    private String brandsElementCSSClasses;
    private String nonBrandName;
    private Map<String, Integer> brandNameIDs;

    // properties used to scrape car hire details
    private String carsSearchURL;
    private String carElementCSSClasses;
    private String[] rentURLAnchorTagSelectors;
    private String[] imageTagSelectors;
    private String[] rentPerDayTagSelectors;

    private String[] carBrandAndModelSelectors;
    private String rentalCarServiceProvider;

    /**
     * Empty constructor
     */
    public WebScraper() {
    }

    public String getBrandsSearchURL() {
        return brandsSearchURL;
    }

    public void setBrandsSearchURL(String brandsSearchURL) {
        this.brandsSearchURL = brandsSearchURL;
    }

    public String getBrandsElementCSSClasses() {
        return brandsElementCSSClasses;
    }

    public void setBrandsElementCSSClasses(String brandsElementCSSClasses) {
        this.brandsElementCSSClasses = brandsElementCSSClasses;
    }

    public String getNonBrandName() {
        return nonBrandName;
    }

    public void setNonBrandName(String nonBrandName) {
        this.nonBrandName = nonBrandName;
    }

    public Map<String, Integer> getBrandNameIDs() {
        return brandNameIDs;
    }

    public void setBrandNameIDs(Map<String, Integer> brandNameIDs) {
        this.brandNameIDs = brandNameIDs;
    }

    public String getCarsSearchURL() {
        return carsSearchURL;
    }

    public void setCarsSearchURL(String carsSearchURL) {
        this.carsSearchURL = carsSearchURL;
    }

    public String getCarElementCSSClasses() {
        return carElementCSSClasses;
    }

    public void setCarElementCSSClasses(String carElementCSSClasses) {
        this.carElementCSSClasses = carElementCSSClasses;
    }

    public String[] getRentURLAnchorTagSelectors() {
        return rentURLAnchorTagSelectors;
    }

    public void setRentURLAnchorTagSelectors(String[] rentURLAnchorTagSelectors) {
        this.rentURLAnchorTagSelectors = rentURLAnchorTagSelectors;
    }

    public String[] getImageTagSelectors() {
        return imageTagSelectors;
    }

    public void setImageTagSelectors(String[] imageTagSelectors) {
        this.imageTagSelectors = imageTagSelectors;
    }

    public String[] getRentPerDayTagSelectors() {
        return rentPerDayTagSelectors;
    }

    public void setRentPerDayTagSelectors(String[] rentPerDayTagSelectors) {
        this.rentPerDayTagSelectors = rentPerDayTagSelectors;
    }

    public String[] getCarBrandAndModelSelectors() {
        return carBrandAndModelSelectors;
    }

    public void setCarBrandAndModelSelectors(String[] carBrandAndModelSelectors) {
        this.carBrandAndModelSelectors = carBrandAndModelSelectors;
    }

    public String getRentalCarServiceProvider() {
        return rentalCarServiceProvider;
    }

    public void setRentalCarServiceProvider(String rentalCarServiceProvider) {
        this.rentalCarServiceProvider = rentalCarServiceProvider;
    }

    /**
     * Scrapes website and returns brands list of HTML elements.
     * @params brandsSearchURL website search URL that has car brands
     * @params brandsElementCSSClasses car brand HTML elements identifier
     * @return brandsHTMLElements
     */
    public Elements scrapeBrandsHTMLElements (String brandsSearchURL, String brandsElementCSSClasses) {

        Elements brandsHTMLElements = null;
        try {
            // scrape brand names HTML elements
            brandsHTMLElements = scrapeWebsite(brandsSearchURL, brandsElementCSSClasses);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return brandsHTMLElements;
    }

    /**
     * Traverses scraped brands list of HTML elements and returns car brand names list.
     * Depends on nonBrandName as text to exclude from list of brands.
     * @return carBrands
     */
    public List<String> getCarBrands () {

        // scraped brand names HTML elements
        Elements brandsHTMLElements = scrapeBrandsHTMLElements(brandsSearchURL, brandsElementCSSClasses);

        // traverse scraped brand names HTML elements to return car brands in a list
        List<String> carBrands = new ArrayList<>();
        for(int i = 0; i < brandsHTMLElements.size(); ++i){
            // Get a car brand
            String carBrandName = brandsHTMLElements.get(i).text();
            if(!carBrandName.equals(nonBrandName)){
                carBrands.add(carBrandName);
            }
        }
        return carBrands;
    }

    /**
     * Scrapes website and returns car hire details list in HTML elements.
     * @params carsSearchURL website search URL that has list of cars for hire
     * @params carElementCSSClasses cars list HTML elements identifier
     * @return carsHTMLElements
     */
    public Elements scrapeCarsHTMLElements (String carsSearchURL, String carElementCSSClasses) {

        Elements carsHTMLElements = null;
        try {
            // scrape cars for hire HTML elements
            carsHTMLElements = scrapeWebsite(carsSearchURL, carElementCSSClasses);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return carsHTMLElements;
    }

    /**
     * Traverses website products listing HTML elements and returns car hire data list with
     * rentURL, imageURL, rentPerDay, carBrandAndModel, and serviceProvider for all cars
     * found on search page.
     * @params brandName
     * @return carsData
     */
    public List<List<String>> getCarsData (String brandName) {

        // append car brand to search URL
        String brandCarsSearchURL = carsSearchURL+brandName;

        // scraped cars for hire in HTML elements
        Elements carsHTMLElements = scrapeCarsHTMLElements(brandCarsSearchURL, carElementCSSClasses);

        List<List<String>> carsData = new ArrayList<>();

        for(int i = 0; i < carsHTMLElements.size(); ++i){
            List<String> carData = new ArrayList<>();

            // Get the car rent url
            Elements rentURLAnchorTag = carsHTMLElements.get(i).select(rentURLAnchorTagSelectors[0]);
            String rentURL = rentURLAnchorTag.attr(rentURLAnchorTagSelectors[1]);
            carData.add(rentURL);

            // Get the car image url
            Elements imageTag = carsHTMLElements.get(i).select(imageTagSelectors[0]);
            String imageURL = imageTag.attr(imageTagSelectors[1]);
            carData.add(imageURL);

            // Get the rent cost per day
            Elements rentPerDayTag = carsHTMLElements.get(i).select(rentPerDayTagSelectors[0]);
            String rentPerDay = rentPerDayTag.text().replaceAll("[^0-9.]", "");
            carData.add(rentPerDay);

            // Get the car brand and model
            Elements carBrandAndModelTag = carsHTMLElements.get(i).select(carBrandAndModelSelectors[0]);
            String carBrandAndModel = carBrandAndModelTag.text();
            carData.add(carBrandAndModel);

            // Car service provider
            carData.add(rentalCarServiceProvider);

            // Add car to cars list
            carsData.add(carData);
        }
        return carsData;
    }

    /**
     * Extracts model name from a string that has both brand and model name.
     * @params brandAndModelName string that contains both brand and model name
     * @params brandName car brand name
     * @return modelName
     */
    public String extractModelName (String brandAndModelName, String brandName) {

        String modelName = "";
        // case-insensitive way of checking whether brand and model name contains brand name
        boolean brandAndModelNameContainsBrandName = brandAndModelName.matches(".*(?i)"+ brandName +".*");
        // Only extract model name if brand and model name contains brand name
        if(brandAndModelNameContainsBrandName){
            // Split the brand and model name
            String[] brandAndModelNames = brandAndModelName.split(" ");
            // Iterate through brand and model names to keep only model name
            for (String name : brandAndModelNames) {
                if (!name.equalsIgnoreCase(brandName)) {
                    modelName += name + " ";
                }
            }
        }

        return modelName;
    }

    /**
     * Scrapes given website and returns web elements.
     * In our case it returns an html element with either car brands or cars list.
     * @param searchURL website search URL
     * @param elementCSSClasses used as car brand or cars list html elements identifier
     * @return htmlElements scraped html elements
     */
    public Elements scrapeWebsite(String searchURL, String elementCSSClasses) throws Exception{

        // Download HTML document from website
        Document htmlDoc = Jsoup.connect(searchURL).get();

        // Get all the elements on the page
        Elements htmlElements = htmlDoc.select(elementCSSClasses);

        return htmlElements;
    }

}
