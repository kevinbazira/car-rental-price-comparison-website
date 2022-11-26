package ug.kevinbazira.carrentalpricecomparison;

import javax.persistence.*;

/**
 * Represents the car brand database table.
 * Java annotations map this class to a database table called car_brand_tbl.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name="car_brand_tbl")
public class CarBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    /**
     * Empty constructor
     */
    public CarBrand() {
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

    /**
     * Returns a string representation of a car brand
     */
    public String toString() {
        return "CarBrand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
