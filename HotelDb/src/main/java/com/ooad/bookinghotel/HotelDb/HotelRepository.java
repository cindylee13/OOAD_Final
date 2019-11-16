package com.ooad.bookinghotel.HotelDb;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface HotelRepository extends CrudRepository<Hotel, Integer> {

//    Query from View
//    @Query(nativeQuery = true, value = "SELECT * FROM vReport1_1 ORDER BY DATE_CREATED, AMOUNT")
//    List<R11Dto> getR11();
//
//    You can use pagination with a native query. It is documented here:
//    https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#_native_queries
//
//    "You can however use native queries for pagination by specifying the count query yourself: Example 59.
//    Declare native count queries for pagination at the query method using @Query"
//
//    public interface UserRepository extends JpaRepository<User, Long> {
//
//        @Query(value = "SELECT * FROM USERS WHERE LASTNAME = ?1",
//                countQuery = "SELECT count(*) FROM USERS WHERE LASTNAME = ?1",
//                nativeQuery = true)
//        Page<User> findByLastname(String lastname, Pageable pageable);
//    }

//            @Query(value = "select hotel.id, hotel.star, hotel.locality, hotel.address, hotel.json_file_id, hotel.name, " +
//                    "hotel.create_time, hotel_room.room_type, hotel_room.quantity, hotel_room.price " +
//                    "from hotel inner join hotel_room on hotel_id = hotel.json_File_id " +
//                    "WHERE hotel.locality = ?1",
//                countQuery = "SELECT count(*) FROM USERS WHERE LASTNAME = ?1",
//                nativeQuery = true)
//            Page<Hotel> searchHotel(@Param("bar") Bar bar,
//                                    @Param("goo") Optional<Goo> goo);

    List<Hotel> findByJsonFileId(Integer jsonFileId);



}