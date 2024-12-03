package org.hotel.villahotel.service;

import org.hotel.villahotel.model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface IRoomService {
    Room addNewRoom(MultipartFile image, String roomType , BigDecimal roomPrice);
}
