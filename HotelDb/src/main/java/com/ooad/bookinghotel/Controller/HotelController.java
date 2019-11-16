package com.ooad.bookinghotel.Controller;

import com.ooad.bookinghotel.HotelDb.Hotel;
import com.ooad.bookinghotel.HotelDb.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/Hotel") // This means URL's start with /demo (after Application path)
public class HotelController {

    @Autowired
    private HotelRepository hotelRepository;

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Hotel> getAllUsers() {
        // This returns a JSON or XML with the users
        return hotelRepository.findAll();
    }

}