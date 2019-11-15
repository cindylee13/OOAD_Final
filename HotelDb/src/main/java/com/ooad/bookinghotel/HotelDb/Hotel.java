package com.ooad.bookinghotel.HotelDb;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class Hotel extends BaseDbo {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column(columnDefinition = "varchar(255) default ''", nullable = false)
    private String name;

    @Column(columnDefinition = "varchar(255) default 0", nullable = false)
    private Integer star;

    @Column(columnDefinition = "varchar(255) default ''", nullable = false)
    private String locality;

    @Column(columnDefinition = "varchar(255) default ''", nullable = false)
    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public Integer getStar() {
        return star;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}