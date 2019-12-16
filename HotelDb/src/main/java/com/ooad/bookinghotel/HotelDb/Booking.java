package com.ooad.bookinghotel.HotelDb;


import javax.persistence.*;
import java.util.Date;

@Entity // This tells Hibernate to make a table out of this class
public class Booking extends BaseDbo {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column(updatable = false, nullable = false)
    private Integer HotelId;

    @Column(updatable = false, nullable = false)
    private Integer HotelRoomId;

    @Column(updatable = false, nullable = false)
    private Integer OrderId;

    @Column(updatable = false, nullable = false)
    private Date StartDate;

    @Column(updatable = false, nullable = false)
    private Date EndDate;

    @Column(updatable = false, nullable = false)
    private Boolean IsDisabled;



    public Integer getId() {
        return id;
    }

    public Integer getHotelId() {
        return HotelId;
    }

    public void setHotelId(Integer HotelId) {
        this.HotelId = HotelId;
    }

    public Integer getHotelRoomId() {
        return HotelRoomId;
    }

    public void setHotelRoomId(Integer HotelRoomId) {
        this.HotelRoomId = HotelRoomId;
    }

    public Integer getOrderId() {
        return OrderId;
    }

    public void setOrderId(Integer HotelId) {
        this.OrderId = OrderId;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date StartDate) {
        this.StartDate = StartDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date EndDate) {
        this.EndDate = EndDate;
    }

    public Boolean getIsDisabled() {
        return IsDisabled;
    }

    public void setIsDisabled(Boolean IsDisabled) {
        this.IsDisabled = IsDisabled;
    }
}