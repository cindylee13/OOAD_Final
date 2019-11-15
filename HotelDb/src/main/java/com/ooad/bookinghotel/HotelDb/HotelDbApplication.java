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

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ooad.bookinghotel.Controller"} )
public class HotelDbApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(HotelDbApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(HotelDbApplication.class, args);
		log.info("hi, i am main.");
	}

	@Autowired // This means to get the bean called systemConfigRepository
	private SystemConfigRepository systemConfigRepository;

	@Autowired
	private HotelRepository hotelRepository;

//	@Autowired
//	private HotelRoomRepository hotelRoomRepository;


	@Override
	public void run(String... strings) throws Exception {

		log.info("Check Hotel List Update Time.");

		UpdateHotelList();
	}

	private void UpdateHotelList(){
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
				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");
				Date lastDbUpdate = formatter1.parse(hotelUpdateTime.getValue());
				Date fileUpdateTime = formatter1.parse(fileDate);
				updateData = fileUpdateTime.compareTo(lastDbUpdate) > 0;
			}

			if(updateData){
				ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>> ) obj.get("List");
				log.info(fileDate);

				for(Map<String, Object> hotel : list){
					Integer hotelId =  Integer.parseInt(hotel.get("HotelID").toString());
					Optional<Hotel> existHotel = hotelRepository.findById(hotelId);

					Hotel upsertHotel = null;
					if(existHotel.isPresent() == false){
						upsertHotel = new Hotel();
						upsertHotel.setId(hotelId);

					}else{
						upsertHotel = existHotel.get();
					}

					upsertHotel.setLocality(hotel.get("Locality").toString());
					upsertHotel.setAddress(hotel.get("Street-Address").toString());
					upsertHotel.setName(upsertHotel.getLocality()+upsertHotel.getId());
					Integer hotelStar = Integer.parseInt(hotel.get("HotelStar").toString());
					upsertHotel.setStar(hotelStar);

//					ArrayList<Map<String, Object>> rooms = (ArrayList<Map<String, Object>> )hotel.get("Rooms");

//					ArrayList<HotelRoom> updateRooms = new ArrayList<HotelRoom>();
//					for(Map<String, Object> room : rooms){
//						String roomType = room.get("RoomType").toString();
//						RoomType rtCode = RoomType.valueOf(roomType);
//
//						HotelRoom upsertRoom = null;
//						Optional<HotelRoom> existHotelRoom = hotelRoomRepository.findByType(hotelId, rtCode.number);
//						if(existHotelRoom.isPresent() == false){
//							upsertRoom = new HotelRoom();
//							upsertRoom.setRoomType(rtCode);
//							upsertRoom.setHotelId(hotelId);
//						}else {
//							upsertRoom = existHotelRoom.get();
//						}
//
//						upsertRoom.setPrice((Integer) room.get("RoomPrice"));
//						upsertRoom.setQuantity((Integer) room.get("Number"));
//
//						updateRooms.add(upsertRoom);
//					}
//					hotelRoomRepository.saveAll(updateRooms);
					hotelRepository.save(upsertHotel);
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

//	{
//		"HotelID": 0,
//			"HotelStar": 2,
//			"Locality": "台北",
//			"Street-Address": "民生東路一段28號",
//			"Rooms": [
//		{
//			"RoomType": "Single",
//				"RoomPrice": 518,
//				"Number": 29
//		},
//    ]
//	}

}
//
//class HotelObj{
//	public String HotelID;
//	public String HotelStar;
//	public String Locality;
//	public String Street-Address;
//
//}
