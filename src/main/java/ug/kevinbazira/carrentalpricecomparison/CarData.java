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

    @ManyToOne // neat trick to map foreign key :)
    @JoinColumn(name = "car_model_id")
    private CarModel carModel;

    @Column(name = "rent_per_day")
    private float rentPerDay;

    @ManyToOne // neat trick to map foreign key :)
    @JoinColumn(name = "rental_service_id")
    private RentalService rentalService;

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

    public CarModel getCarModel() {
        return carModel;
    }

    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }

    public float getRentPerDay() {
        return rentPerDay;
    }

    public void setRentPerDay(float rentPerDay) {
        this.rentPerDay = rentPerDay;
    }

    public RentalService getRentalService() {
        return rentalService;
    }

    public void setRentalService(RentalService rentalService) {
        this.rentalService = rentalService;
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
                ", carModel=" + carModel +
                ", rentPerDay=" + rentPerDay +
                ", rentalService=" + rentalService +
                ", rentURL='" + rentURL + '\'' +
                ", dateScraped=" + dateScraped +
                '}';
    }
}
