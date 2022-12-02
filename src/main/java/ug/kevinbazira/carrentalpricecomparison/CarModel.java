package ug.kevinbazira.carrentalpricecomparison;

import javax.persistence.*;

/**
 * Represents a car model in the database.
 * Car model mapped to a database table called car_model_tbl.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name="car_model_tbl")
public class CarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne // neat trick to map foreign key :)
    @JoinColumn(name = "car_brand_id")
    private CarBrand carBrand;

    @Column(name = "image_url")
    private String imageURL;

    /**
     * Empty constructor
     */
    public CarModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CarBrand getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(CarBrand carBrand) {
        this.carBrand = carBrand;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * Returns a string representation of a car model
     */
    public String toString() {
        return "CarModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", carBrand=" + carBrand +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
