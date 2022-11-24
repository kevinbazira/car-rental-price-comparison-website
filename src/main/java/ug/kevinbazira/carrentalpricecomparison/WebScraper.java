package ug.kevinbazira.carrentalpricecomparison;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Represents a web scraper.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
public class WebScraper {

    /**
     * Scrapes given website and returns web elements with products data.
     * @param searchURL website search URL
     * @param brandName brand name of car that we want to scrape
     * @param cssClasses used as product element identifier
     * @return products
     */
    public static Elements scrapeWebsite(String searchURL, String brandName, String cssClasses) throws Exception{

        //Download HTML document from website
        Document htmlDoc = Jsoup.connect(searchURL + brandName).get();

        //Get all the products on the page
        Elements products = htmlDoc.select(cssClasses);

        return products;

        //Work through the products
        /*for(int i=0; i<products.size(); ++i){

            //Get the car image url
            Elements imageTag = products.get(i).select(".car_image>img");
            String imageURL = imageTag.attr("src");

            //Get the car rent url
            Elements rentURLAnchorTag = products.get(i).select(".car_link");
            String rentURL = rentURLAnchorTag.attr("href");

            //Get the car brand and model
            String carBrandAndModel = rentURLAnchorTag.text();

            //Get the rent cost per day
            Elements rentPerDaySpanTag = products.get(i).select(".single_car_price");
            String rentPerDay = rentPerDaySpanTag.text();

            // rentURL, imageURL, rentPerDay, serviceProvider, carBrandAndModel
            System.out.println("Brand and Model: " + carBrandAndModel);
        }*/
    }

    public static void main(String[] args) {
        // TODO
        String searchURL = "https://xcarrental.com/rent-a-car-dubai/?brand=";
        String brandName = "Lamborghini";
        String cssClasses = ".element.grid";

        try{
            System.out.println(scrapeWebsite(searchURL, brandName, cssClasses));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
