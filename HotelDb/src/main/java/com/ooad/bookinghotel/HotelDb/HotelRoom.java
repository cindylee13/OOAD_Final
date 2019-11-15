package com.ooad.bookinghotel.HotelDb;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class HotelRoom extends BaseDbo{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column(columnDefinition = "varchar(255) default 0", nullable = false)
    private Integer hotelId;

    @Column(columnDefinition = "varchar(255) default 0", nullable = false)
    private Integer quantity;

    @Column(columnDefinition = "varchar(255) default 99999", nullable = false)
    private Integer price;

    @Column(columnDefinition = "varchar(255) default 0", nullable = false)
    private int roomType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPrice() {
        return price;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType.number;
    }

}

enum RoomType {
    Single(1, "Single"),
    Double(2, "Double"),
    Quad(4, "Quad");

    public final int number;
    public final String type;

    private RoomType(int number, String type) {
        this.number = number;
        this.type = type;
    }
}
