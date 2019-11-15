package com.ooad.bookinghotel.HotelDb;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class SystemConfig extends BaseDbo{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column(columnDefinition = "varchar(255) default '' ", nullable = false)
    private String name;

    @Column(columnDefinition = "varchar(255) default ''", nullable = false)
    private String explanation;

    @Column(columnDefinition = "varchar(255) default ''", nullable = false)
    private String value;


    public Integer getId() {
        return id;
    }

//    public void setId(Integer id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
