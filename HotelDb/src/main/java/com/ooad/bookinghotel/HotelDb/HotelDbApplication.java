package com.ooad.bookinghotel.HotelDb;

import org.apache.tomcat.util.json.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ooad.bookinghotel.Controller"} )
public class HotelDbApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(HotelDbApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(HotelDbApplication.class, args);
		log.info("hi, i am main.");
	}

//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/Hotel").allowedOrigins("http://localhost:8081");
//			}
//		};
//	}

	@Autowired // This means to get the bean called systemConfigRepository
	private SystemConfigRepository systemConfigRepository;

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private HotelRoomRepository hotelRoomRepository;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... strings) throws Exception {

		UpdateSchema();

//		UpdateHotelList();
		UpdateHotelListBySqlScript();
	}

	private void UpdateHotelListBySqlScript(){
		log.info("Check Hotel List Update Time.");

		List<SystemConfig> hotelListUpdateTimeList = systemConfigRepository.findByName(SystemConstant.ConfigHotelListUpdateTime);
		SystemConfig hotelUpdateTime = null;
		boolean updateData = true;

		try {
			File resource = new ClassPathResource("systemFile/HotelList.json").getFile();
			String HotelListString = new String(Files.readAllBytes(resource.toPath()));

			JSONParser jsonParser = new JSONParser(HotelListString);
			Map<String, Object> obj = jsonParser.parseObject();
			String fileDate =  obj.get("UpdateTime").toString();

			if(hotelListUpdateTimeList.size()>0){
				hotelUpdateTime = hotelListUpdateTimeList.get(0);
				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				Date lastDbUpdate = formatter1.parse(hotelUpdateTime.getValue());
				Date fileUpdateTime = formatter1.parse(fileDate);
				updateData = fileUpdateTime.compareTo(lastDbUpdate) > 0;
			}

			if(updateData){
				log.info("Prepare update.");

				ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>> ) obj.get("List");
				ArrayList<Object[]> HotelRooms = new ArrayList<Object[]>() {};

				List<Object[]> Hotels = list.stream().map(hotel -> {
					Integer hotelId =  Integer.parseInt(hotel.get("HotelID").toString());
					Integer hotelStar =  Integer.parseInt(hotel.get("HotelStar").toString());
					String locality = hotel.get("Locality").toString();
					String address = hotel.get("Street-Address").toString();
					Object[] newHotel = new Object[]{
							address,
							hotelId,
							locality,
							locality + hotelId,
							hotelStar
					};

					ArrayList<Map<String, Object>>  rooms = (ArrayList<Map<String, Object>> )hotel.get("Rooms");
					for(Map<String, Object> room : rooms){
						String roomType = room.get("RoomType").toString();
						RoomType rtCode = RoomType.valueOf(roomType);
						Integer price = Integer.parseInt( room.get("RoomPrice").toString());
						Integer quantity = Integer.parseInt( room.get("Number").toString());
						HotelRooms.add(new Object[]{
								hotelId,
								price,
								quantity,
								rtCode.number
						});
					}

					return newHotel;
				}).collect(Collectors.toList());

				log.info("room size: "+HotelRooms.size());
				log.info("hotel size: "+Hotels.size());
				log.info(fileDate);

				//如果檔案重複，就放棄新增
//				jdbcTemplate.batchUpdate("INSERT INTO hotel (create_time, update_time, address, json_file_id, locality, name, star) " +
//						"VALUES (current_timestamp, current_timestamp, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE id=id ;", Hotels);

				jdbcTemplate.batchUpdate("INSERT INTO hotel_room (hotel_id, price, quantity, room_type, create_time, update_time) " +
						"VALUES (?, ?, ?, ?, current_timestamp, current_timestamp) ON DUPLICATE KEY UPDATE id=id ;", HotelRooms);

				log.info("DONE.");
			}

			if(hotelUpdateTime == null){
				hotelUpdateTime = new SystemConfig();
				hotelUpdateTime.setName(SystemConstant.ConfigHotelListUpdateTime);
				hotelUpdateTime.setExplanation("上次旅館更新時間");
			}

			hotelUpdateTime.setValue(fileDate);
			systemConfigRepository.save(hotelUpdateTime);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void UpdateSchema(){
		log.info("Update Schema.");

		jdbcTemplate.execute("CREATE OR REPLACE VIEW hotel_info  AS " +
				" select " +
				" hotel.id, hotel.star, hotel.locality, hotel.address, hotel.json_file_id, hotel.name, " +
				" sum(CASE WHEN hotel_room.room_type =1 THEN hotel_room.quantity ELSE 0 END) AS SingleRoom, " +
				" sum(CASE WHEN hotel_room.room_type =1 THEN hotel_room.price ELSE 0 END) AS SingleRoomPrice, " +
				" sum(CASE WHEN hotel_room.room_type =2 THEN hotel_room.quantity ELSE 0 END) AS DoubleRoom,  " +
				" sum(CASE WHEN hotel_room.room_type =2 THEN hotel_room.price ELSE 0 END) AS DoubleRoomPrice, " +
				" sum(CASE WHEN hotel_room.room_type =4 THEN hotel_room.quantity ELSE 0 END) AS QuadRoom, " +
				" sum(CASE WHEN hotel_room.room_type =4 THEN hotel_room.price ELSE 0 END) AS QuadRoomPrice " +
				" from hotel  " +
				" inner join hotel_room on hotel_id = hotel.json_File_id " +
				" where hotel_room.quantity > 0 " +
				" group by hotel.id, hotel.star, hotel.locality, hotel.address, hotel.json_file_id, hotel.name ");

		try{
			jdbcTemplate.execute("ALTER TABLE hotel DROP INDEX hotel_unique_index;");
		}catch (Exception e){
			e.printStackTrace();
			log.info("hotel_unique_index DOES NOT EXIST.");
		}

		try{
			jdbcTemplate.execute("ALTER TABLE hotel_room DROP INDEX hotel_room_unique_index;");
		}catch (Exception e){
			e.printStackTrace();
			log.info("hotel_room_unique_index DOES NOT EXIST.");
		}

//		add hotel unique key
		jdbcTemplate.execute("ALTER TABLE hotel ADD CONSTRAINT hotel_unique_index UNIQUE (json_File_id);");
		jdbcTemplate.execute("ALTER TABLE hotel_room ADD CONSTRAINT hotel_room_unique_index UNIQUE (hotel_id, room_type);");

		jdbcTemplate.execute("ALTER TABLE hotel MODIFY id INT NOT NULL AUTO_INCREMENT;");
		jdbcTemplate.execute("ALTER TABLE hotel_room MODIFY id INT NOT NULL AUTO_INCREMENT;");
		jdbcTemplate.execute("ALTER TABLE system_config MODIFY id INT NOT NULL AUTO_INCREMENT;");
		jdbcTemplate.execute("ALTER TABLE booking MODIFY id INT NOT NULL AUTO_INCREMENT;");

	}

	// 太慢了
	private void UpdateHotelList(){
		log.info("Check Hotel List Update Time.");

		List<SystemConfig> hotelListUpdateTimeList = systemConfigRepository.findByName(SystemConstant.ConfigHotelListUpdateTime);
		SystemConfig hotelUpdateTime = null;
		boolean updateData = true;

		try {
			File resource = new ClassPathResource("systemFile/HotelList.json").getFile();
			String HotelListString = new String(Files.readAllBytes(resource.toPath()));

			JSONParser jsonParser = new JSONParser(HotelListString);
			Map<String, Object> obj = jsonParser.parseObject();
			String fileDate =  obj.get("UpdateTime").toString();

			if(hotelListUpdateTimeList.size()>0){
				hotelUpdateTime = hotelListUpdateTimeList.get(0);
				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				Date lastDbUpdate = formatter1.parse(hotelUpdateTime.getValue());
				Date fileUpdateTime = formatter1.parse(fileDate);
				updateData = fileUpdateTime.compareTo(lastDbUpdate) > 0;
			}

			if(updateData){
				ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>> ) obj.get("List");
				log.info(fileDate);

				for(Map<String, Object> hotel : list){
					Integer hotelId =  Integer.parseInt(hotel.get("HotelID").toString());
					log.info("Json hotel id:"+hotelId);
					List<Hotel> existHotel = hotelRepository.findByJsonFileId(hotelId);

					Hotel upsertHotel = null;
					if(existHotel.size() <= 0){
						upsertHotel = new Hotel();
						upsertHotel.setJsonFileId(hotelId);
						log.info("new hotel id:"+hotelId);

					}else{
						upsertHotel = existHotel.get(0);
						log.info("existHotel id:"+hotelId);
					}

					upsertHotel.setLocality(hotel.get("Locality").toString());
					upsertHotel.setAddress(hotel.get("Street-Address").toString());
					upsertHotel.setName(upsertHotel.getLocality()+hotelId);
					Integer hotelStar = Integer.parseInt(hotel.get("HotelStar").toString());
					upsertHotel.setStar(hotelStar);

					ArrayList<Map<String, Object>> rooms = (ArrayList<Map<String, Object>> )hotel.get("Rooms");

					ArrayList<HotelRoom> updateRooms = new ArrayList<HotelRoom>();
					for(Map<String, Object> room : rooms){
						String roomType = room.get("RoomType").toString();
						RoomType rtCode = RoomType.valueOf(roomType);

						HotelRoom upsertRoom = null;
						List<HotelRoom> existHotelRoom = hotelRoomRepository.findByHotelIdAndRoomType(hotelId, rtCode.number);

						if(existHotelRoom.size() <= 0){
							upsertRoom = new HotelRoom();
							upsertRoom.setRoomType(rtCode);
							upsertRoom.setHotelId(hotelId);
						}else {
							upsertRoom = existHotelRoom.get(0);
						}
						Integer price = Integer.parseInt( room.get("RoomPrice").toString());
						upsertRoom.setPrice(price);
						Integer quantity = Integer.parseInt( room.get("Number").toString());
						upsertRoom.setQuantity(quantity);

						updateRooms.add(upsertRoom);
					}

					upsertHotel = hotelRepository.save(upsertHotel);
					hotelRoomRepository.saveAll(updateRooms);

					log.info("add/update one hotel, id:"+ upsertHotel.getId());
				}
			}

			if(hotelUpdateTime == null){
				hotelUpdateTime = new SystemConfig();
				hotelUpdateTime.setName(SystemConstant.ConfigHotelListUpdateTime);
				hotelUpdateTime.setExplanation("上次旅館更新時間");
			}

			hotelUpdateTime.setValue(fileDate);
			systemConfigRepository.save(hotelUpdateTime);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}