// Import the express and url modules
const express = require("express");
const sanitizer = require("string-sanitizer");
const url = require("url");

// Status codes defined in external file
require("./http_status.js");

// The express module is a function. When it is executed it returns an app object
const app = express();

// Import the mysql module
const mysql = require("mysql");

// Create a connection object with the user details
const connectionPool = mysql.createPool({
    connectionLimit: 1,
    host: "localhost",
    user: "root",
    password: "",
    database: "car_rental_price_comparison_db",
    debug: false
});

// Serve up static pages from site root directory
app.use(express.static("../"));

// Start the app listening on port 8080
app.listen(8080);


/**
 * Sanitize query string to avoid SQL injection.
 */
const sanitizeQueryString = (queryString) => {
    if (queryString !== undefined) {
        queryString = sanitizer.sanitize.keepSpace(queryString);
    }
    return queryString;
}


/** 
 * Handles GET requests sent to web service. 
 * Processes path and query string and calls appropriate functions to return the data.
 */  
const handleGetRequest = (request, response) => {
    // Parse the URL
    const urlObj = url.parse(request.url, true);

    // Extract object containing queries from URL object.
    const queries = urlObj.query;

    // Get query strings if they have been set. Sanitize them to avoid SQL injection.
    const carBrand = sanitizeQueryString(queries["car-brand"]);
    const carModel = sanitizeQueryString(queries["car-model"]);
    const searchTerm = sanitizeQueryString(queries["search-term"]);
    const numCars = sanitizeQueryString(queries["num-cars"]);
    const offset = sanitizeQueryString(queries["offset"]);

    // Split the path of the request into its components
    const pathArray = urlObj.pathname.split("/");

    // Get the last part of the path
    const pathEnd = pathArray[pathArray.length - 1];

    // If path ends with 'cars-data' we return all cars
    if(pathEnd === "cars-data"){
        getCarsDataWithTotalCount(response,  carBrand, carModel, searchTerm, numCars, offset);
        return;
    }

    // If path ends with 'car-brands' we return all car brands
    if(pathEnd === "car-brands"){
        getCarBrands(response);
        return;
    }

    // If path ends with 'car-models-in-brand' we return all cars in models of a specified brand name
    if(pathEnd === "car-models-in-brand"){
        if(!carBrand){  
            // return error message if car brand name is not specified
            response.send("{error: 'Setting car-brand query string is required to get cars in models of a specified brand'}");
        } else {
            getCarModelsInBrand(response, carBrand);
        }
        return;
    }

    //The path is not recognized. Return an error message
    response.status(HTTP_STATUS.NOT_FOUND);
    response.send("{error: 'Path not recognized', url: " + request.url + "}");
}

// Set up the application to handle GET requests sent to the user path
app.get("/cars-data", handleGetRequest);
app.get("/car-brands", handleGetRequest);
app.get("/car-models-in-brand", handleGetRequest);


/** 
 * Returns all cars data appended to total number of cars.
 * Possibly limit on the total number of cars returned and the offset (for pagination). 
 * This function should be called in the callback of getCarsDataWithTotalCount. 
 */
const getCarsData = (response, carBrand, carModel, searchTerm, totalNumCars, numCars, offset) => {
    // Select the cars data using WHERE to convert foreign keys into useful data.
    let slqCarsData = "SELECT a.id, c.name AS car_brand_name, b.name AS car_model_name, b.image_url, a.rent_per_day, d.name AS rental_service, a.rent_url, a.date_scraped FROM cars_data_tbl AS a, car_model_tbl AS b, car_brand_tbl AS c, rental_services_tbl AS d WHERE a.car_model_id = b.id AND b.car_brand_id = c.id AND a.rental_service_id = d.id";
   
    // Add car brand, model and search term to SQL query if provided
    if(searchTerm !== undefined){
        // If searchTerm exists
        slqCarsData += " AND (c.name LIKE '%" + searchTerm + "%' OR b.name LIKE '%" + searchTerm + "%')";
    } else if(carBrand !== undefined && carModel !== undefined){
        // If both carBrand and carModel exist
        slqCarsData += " AND c.name = '" + carBrand + "' AND b.name = '" + carModel + "'";
    } else if(carBrand !== undefined && carModel === undefined){
        // If only carBrand exists and carModel desn't
        slqCarsData += " AND c.name = '" + carBrand + "'";
    }
    
    // Limit the number of results returned, if this has been specified in the query string
    if(numCars !== undefined && offset !== undefined ){
        slqCarsData += " LIMIT " + numCars + " OFFSET " + offset;
    }

    // Execute the query
    connectionPool.query(slqCarsData, (err, result) => {

        // Check for errors
        if (err){
            // Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({"error": true, "message": + err});
            return;
        }

        // Create JavaScript object that combines total number of items with data
        const returnObj = {totalNumCars: totalNumCars};
        returnObj.carsData = result; //Array of data from database

        // Return results in JSON format
        response.json(returnObj);
    });
}


/** 
 * When retrieving all cars data we start by retrieving the total number of cars.
 * The database callback function will then call the function to get the cars data
 * with pagination.
 */
const getCarsDataWithTotalCount = (response, carBrand, carModel, searchTerm, numCars, offset) => {
    let slqCarsData = "SELECT a.id, c.name AS car_brand_name, b.name AS car_model_name, b.image_url, a.rent_per_day, d.name AS rental_service, a.rent_url, a.date_scraped FROM cars_data_tbl AS a, car_model_tbl AS b, car_brand_tbl AS c, rental_services_tbl AS d WHERE a.car_model_id = b.id AND b.car_brand_id = c.id AND a.rental_service_id = d.id";
    let sqlCount = "SELECT COUNT(*) FROM ";

    // Add car brand, model and search term to SQL query if provided
    if(searchTerm !== undefined){
        // If searchTerm exists
        sqlCount += "(" + slqCarsData + " AND (c.name LIKE '%" + searchTerm + "%' OR b.name LIKE '%" + searchTerm + "%')) as cars_data";
    } else if(carBrand !== undefined && carModel !== undefined){
        // If both carBrand and carModel exist
        sqlCount += "(" + slqCarsData + " AND c.name = '" + carBrand + "' AND b.name = '" + carModel + "') as cars_data";
    } else if(carBrand !== undefined && carModel === undefined){
        // If only carBrand exists and carModel desn't
        sqlCount += "(" + slqCarsData + " AND c.name = '" + carBrand + "') as cars_data";
    } else {
        sqlCount += "(" + slqCarsData + ") as cars_data";
    }

    // Execute the query and call the anonymous callback function.
    connectionPool.query(sqlCount, (err, result) => {

        // Check for errors
        if (err){
            // Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({"error": true, "message": + err});
            return;
        }

        // Get the total number of items from the result
        const totalNumCars = result[0]['COUNT(*)'];

        // Call the function that retrieves cars data
        getCarsData(response, carBrand, carModel, searchTerm, totalNumCars, numCars, offset);
    });
}


/** 
 * Returns all car brands.
 */
 const getCarBrands = (response) => {
    // Select the car brands data
    let sqlCarBrands = "SELECT id, name FROM car_brand_tbl";

    // Execute the query
    connectionPool.query(sqlCarBrands, (err, result) => {

        // Check for errors
        if (err){
            // Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({"error": true, "message": + err});
            return;
        }

        // Return results in JSON format
        response.json(result);
    });
}


/** 
 * Returns all car models in a specified brand.
 */
 const getCarModelsInBrand = (response, carBrand) => {
    // Select the car brands data
    let sqlCarModelsInBrand = "SELECT a.id, a.name AS model_name, a.car_brand_id, b.name AS brand_name, a.image_url FROM car_model_tbl AS a, car_brand_tbl AS b WHERE a.car_brand_id = b.id AND b.name='" + carBrand + "'";

    // Execute the query
    connectionPool.query(sqlCarModelsInBrand, (err, result) => {

        // Check for errors
        if (err){
            // Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({"error": true, "message": + err});
            return;
        }

        // Return results in JSON format
        response.json(result);
    });
}



