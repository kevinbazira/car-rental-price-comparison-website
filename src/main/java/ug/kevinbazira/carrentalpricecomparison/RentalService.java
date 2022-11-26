package ug.kevinbazira.carrentalpricecomparison;

import javax.persistence.*;

/**
 * Represents a rental service company in the database.
 * Rental service mapped to a database table called rental_services_tbl.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name="rental_services_tbl")
public class RentalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "website")
    private String website;

    /**
     * Empty constructor
     */
    public RentalService() {
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * Returns a string representation of a rental service
     */
    public String toString() {
        return "RentalServices{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
