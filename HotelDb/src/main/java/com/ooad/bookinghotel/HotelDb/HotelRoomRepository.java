package com.ooad.bookinghotel.HotelDb;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HotelRoomRepository  extends CrudRepository<HotelRoom, Integer> {

    List<HotelRoom> findByHotelIdAndRoomType(Integer hotelId, Integer roomType);

}
