package ug.kevinbazira.carrentalpricecomparison;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Represents a car's data in the database.
 * Car data mapped to a database table called cars_data_tbl.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name="cars_data_tbl")
public class CarData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "car_model_id")
    private int carModelID;

    @Column(name = "rent_per_day")
    private float rentPerDay;

    @Column(name = "rental_service_id")
    private int rentalServiceID;

    @Column(name = "rent_url")
    private String rentURL;

    @Column(name = "date_scraped")
    private Timestamp dateScraped;

    /**
     * Empty constructor
     */
    public CarData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarModelID() {
        return carModelID;
    }

    public void setCarModelID(int carModelID) {
        this.carModelID = carModelID;
    }

    public float getRentPerDay() {
        return rentPerDay;
    }

    public void setRentPerDay(float rentPerDay) {
        this.rentPerDay = rentPerDay;
    }

    public int getRentalServiceID() {
        return rentalServiceID;
    }

    public void setRentalServiceID(int rentalServiceID) {
        this.rentalServiceID = rentalServiceID;
    }

    public String getRentURL() {
        return rentURL;
    }

    public void setRentURL(String rentURL) {
        this.rentURL = rentURL;
    }

    public Timestamp getDateScraped() {
        return dateScraped;
    }

    public void setDateScraped(Timestamp dateScraped) {
        this.dateScraped = dateScraped;
    }

    /**
     * Returns a string representation of a car's data
     */
    public String toString() {
        return "CarData{" +
                "id=" + id +
                ", carModelID=" + carModelID +
                ", rentPerDay=" + rentPerDay +
                ", rentalServiceID=" + rentalServiceID +
                ", rentURL='" + rentURL + '\'' +
                ", dateScraped=" + dateScraped +
                '}';
    }
}
