package ug.kevinbazira.carrentalpricecomparison;

import javax.persistence.*;

/**
 * Represents a car brand.
 * Java annotations map this class to a database table called car_brand_tbl.
 * @author Kevin Bazira
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name="car_brand_tbl")
public class CarBrandTbl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "product_type_id")
    private int productTypeId;
}
