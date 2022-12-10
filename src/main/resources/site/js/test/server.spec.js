// Import node modules
const expect  = require("chai").expect;
const request = require("request");
const server = require("../server");

// Test REST API endpoints
describe("TEST REST API ENDPOINTS", () => {
    describe("GET /cars-data", () => {
        it("should respond with JSON object containing all cars data", (done) => {
            request("http://localhost:8080/cars-data" , (error, response, body) => {
                expect(body).to.not.be.empty
                done();
            });
        });
    });
    
    describe("GET /cars-data?search-term=Audi", () => {
        it("should respond with JSON object containing cars data based on the given search term", (done) => {
            request("http://localhost:8080/cars-data?search-term=Audi" , (error, response, body) => {
                expect(body).to.not.be.empty
                done();
            });
        });
    });
    
    describe("GET /cars-data?num-cars=12&offset=0&car-brand=Audi", () => {
        it("should respond with JSON object containing cars data based on the numCars limit and offset values", (done) => {
            request("http://localhost:8080/cars-data?num-cars=12&offset=0&car-brand=Audi" , (error, response, body) => {
                expect(body).to.not.be.empty
                done();
            });
        });
    });
    
    describe("GET /car-brands", () => {
        it("should respond with JSON object containing all car brands", (done) => {
            request("http://localhost:8080/car-brands" , (error, response, body) => {
                expect(body).to.not.be.empty
                done();
            });
        });
    });
    
    describe("GET /car-models-in-brand", () => {
        it("should respond with JSON object containing car models of a specified brand name", (done) => {
            request("http://localhost:8080//car-models-in-brand?car-brand=Audi" , (error, response, body) => {
                expect(body).to.not.be.empty
                done();
            });
        });
    });
});