package org.hotel.villahotel.service;

import org.hotel.villahotel.model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public interface IRoomService {
    Room addNewRoom(MultipartFile image, String roomType , BigDecimal roomPrice) throws IOException, SQLException;
}
