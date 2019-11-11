package com.ooad.bookinghotel.HotelDb;

import org.springframework.data.repository.CrudRepository;

public interface HotelRepository extends CrudRepository<Hotel, Integer> {
}