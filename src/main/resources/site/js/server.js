// Import the express and url modules
const express = require('express');
const url = require("url");

// Status codes defined in external file
require('./http_status.js');

// The express module is a function. When it is executed it returns an app object
const app = express();

// Import the mysql module
const mysql = require('mysql');

// Create a connection object with the user details
const connectionPool = mysql.createPool({
    connectionLimit: 1,
    host: "localhost",
    user: "root",
    password: "",
    database: "car_rental_price_comparison_db",
    debug: false
});

// Set up the application to handle GET requests sent to the user path
// app.get('/cereals', handleGetRequest);
// app.get('/cereals/*', handleGetRequest);//Subfolders
// app.get('/cereals', handleGetRequest);

// Serve up static pages from public folder
// app.use(express.static('public'));

// Start the app listening on port 8080
app.listen(8080);


/** 
 * Handles GET requests sent to web service. 
 * Processes path and query string and calls appropriate functions to return the data.
 */  
const handleGetRequest = (request, response) => {
    // Parse the URL
    const urlObj = url.parse(request.url, true);

    // Extract object containing queries from URL object.
    const queries = urlObj.query;

    // Get the pagination properties if they have been set. Will be undefined if not set.
    const name = queries['name'];
    const numCars = queries['num-cars'];
    const offset = queries['offset'];

    // Split the path of the request into its components
    const pathArray = urlObj.pathname.split("/");

    // Get the last part of the path
    const pathEnd = pathArray[pathArray.length - 1];

    // If path ends with 'cars-data' we return all cars
    if(pathEnd === 'cars-data'){
        getCarsDataWithTotalCount(response, numCars, offset);
        return;
    }

    // If path ends with 'car-brand' we return all cars of a specified brand name
    if(pathEnd === 'car-brand'){
        if(!name){  
            // return error message if car brand name is not specified
            response.send("{error: 'Setting name query string is required to get cars in a car brand'}");
        } else {
            getCarBrandDataWithTotalCount(response, name, numCars, offset);
        }
        return;
    }

    // If path ends with 'car-brand-or-model' we return all cars of a specified brand or model name
    if(pathEnd === 'car-brand-or-model'){
        if(!name){  
            // return error message if car brand or model name is not specified
            response.send("{error: 'Setting name query string is required to get cars in a car brand or model'}");
        } else {
            getCarBrandOrModelDataWithTotalCount(response, name, numCars, offset);
        }
        return;
    }

    //The path is not recognized. Return an error message
    response.status(HTTP_STATUS.NOT_FOUND);
    response.send("{error: 'Path not recognized', url: " + request.url + "}");
}

// Set up the application to handle GET requests sent to the user path
app.get('/cars-data', handleGetRequest);
app.get('/car-brand', handleGetRequest);
app.get('/car-brand-or-model', handleGetRequest);


/** 
 * Returns all cars data appended to total number of cars.
 * Possibly limit on the total number of cars returned and the offset (for pagination). 
 * This function should be called in the callback of getCarsDataWithTotalCount. 
 */
const getCarsData = (response, totalNumCars, numCars, offset) => {
    // Select the cars data using JOIN to convert foreign keys into useful data.
    let sql = "SELECT `id`, `car_model_id`, `rent_per_day`, `rental_service_id`, `rent_url`, `date_scraped` FROM `cars_data_tbl`";

    
    // Limit the number of results returned, if this has been specified in the query string
    if(numCars !== undefined && offset !== undefined ){
        sql += "LIMIT " + numCars + " OFFSET " + offset;
    }

    // Execute the query
    connectionPool.query(sql, (err, result) => {

        // Check for errors
        if (err){
            // Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
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
const getCarsDataWithTotalCount = (response, numCars, offset) => {
    let sql = "SELECT COUNT(*) FROM cars_data_tbl";

    // Execute the query and call the anonymous callback function.
    connectionPool.query(sql, (err, result) => {

        // Check for errors
        if (err){
            // Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }

        // Get the total number of items from the result
        const totalNumCars = result[0]['COUNT(*)'];
        
        // Call the function that retrieves all cereals
        getCarsData(response, totalNumCars, numCars, offset);
    });
}


/** 
 * Returns all cars data of a specified brand appended to total number of cars.
 * Possibly limit on the total number of cars returned and the offset (for pagination). 
 * This function should be called in the callback of getCarBrandDataWithTotalCount. 
 */
 const getCarBrandData = (response, name, totalNumCars, numCars, offset) => {
    // Select the cars data using JOIN to convert foreign keys into useful data.
    let sql = "SELECT `id`, `car_model_id`, `rent_per_day`, `rental_service_id`, `rent_url`, `date_scraped` FROM `cars_data_tbl` WHERE `car_model_id` IN (SELECT `id` FROM `car_model_tbl` WHERE `car_brand_id` = (SELECT `id` FROM `car_brand_tbl` WHERE `name` = '" + name + "'))";

    
    // Limit the number of results returned, if this has been specified in the query string
    if(numCars !== undefined && offset !== undefined ){
        sql += "LIMIT " + numCars + " OFFSET " + offset;
    }

    // Execute the query
    connectionPool.query(sql, (err, result) => {

        // Check for errors
        if (err){
            // Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
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
 * When retrieving all cars data in a specified brand we start by retrieving the total number of cars.
 * The database callback function will then call the function to get the cars data
 * with pagination.
 */
const getCarBrandDataWithTotalCount = (response, name, numCars, offset) => {
    let sql = "SELECT COUNT(*) FROM (SELECT `id`, `car_model_id`, `rent_per_day`, `rental_service_id`, `rent_url`, `date_scraped` FROM `cars_data_tbl` WHERE `car_model_id` IN (SELECT `id` FROM `car_model_tbl` WHERE `car_brand_id` = (SELECT `id` FROM `car_brand_tbl` WHERE `name` = '" + name + "'))) AS count_tbl";

    // Execute the query and call the anonymous callback function.
    connectionPool.query(sql, (err, result) => {

        // Check for errors
        if (err){
            // Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }

        // Get the total number of items from the result
        const totalNumCars = result[0]['COUNT(*)'];
        
        // Call the function that retrieves all cars of a specified brand name
        getCarBrandData(response, name, totalNumCars, numCars, offset);
    });
}

/** 
 * Returns all cars data of a specified brand or model appended to total number of cars.
 * Possibly limit on the total number of cars returned and the offset (for pagination). 
 * This function should be called in the callback of getCarBrandDataWithTotalCount. 
 */
 const getCarBrandOrModelData = (response, name, totalNumCars, numCars, offset) => {
    // Select the cars data using JOIN to convert foreign keys into useful data.
    let sql = "SELECT `id`, `car_model_id`, `rent_per_day`, `rental_service_id`, `rent_url`, `date_scraped` FROM `cars_data_tbl` WHERE `car_model_id` IN (SELECT `id` FROM `car_model_tbl` WHERE `car_brand_id` = (SELECT `id` FROM `car_brand_tbl` WHERE `name` = '" + name + "'))";

    
    // Limit the number of results returned, if this has been specified in the query string
    if(numCars !== undefined && offset !== undefined ){
        sql += "LIMIT " + numCars + " OFFSET " + offset;
    }

    // Execute the query
    connectionPool.query(sql, (err, result) => {

        // Check for errors
        if (err){
            // Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
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
 * When retrieving all cars data in a specified brand we start by retrieving the total number of cars.
 * The database callback function will then call the function to get the cars data
 * with pagination.
 */
const getCarBrandOrModelDataWithTotalCount = (response, name, numCars, offset) => {
    let sql = "SELECT COUNT(*) FROM (SELECT `id`, `car_model_id`, `rent_per_day`, `rental_service_id`, `rent_url`, `date_scraped` FROM `cars_data_tbl` WHERE `car_model_id` IN (SELECT `id` FROM `car_model_tbl` WHERE `car_brand_id` = (SELECT `id` FROM `car_brand_tbl` WHERE `name` = '" + name + "'))) AS count_tbl";

    // Execute the query and call the anonymous callback function.
    connectionPool.query(sql, (err, result) => {

        // Check for errors
        if (err){
            // Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }

        // Get the total number of items from the result
        const totalNumCars = result[0]['COUNT(*)'];
        
        // Call the function that retrieves all cars of a specified brand name
        getCarBrandOrModelData(response, name, totalNumCars, numCars, offset);
    });
}


