package com.ooad.bookinghotel.HotelDb;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SystemConfigRepository extends CrudRepository<SystemConfig, Integer> {

    List<SystemConfig> findByName(String name);

}
