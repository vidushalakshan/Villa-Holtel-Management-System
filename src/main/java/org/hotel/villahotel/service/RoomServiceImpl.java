package org.hotel.villahotel.service;

import lombok.RequiredArgsConstructor;
import org.hotel.villahotel.model.Room;
import org.hotel.villahotel.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService{

    private final RoomRepository roomRepository;

    @Override
    public Room addNewRoom(MultipartFile image, String roomType, BigDecimal roomPrice) throws IOException, SQLException {
       Room room = new Room();
       room.setRoomType(roomType);
       room.setRoomPrice(roomPrice);
       if (!image.isEmpty()) {
           byte[] photoBytes = image.getBytes();
           Blob photoBlob = new SerialBlob(photoBytes);
           room.setImage(photoBlob);
       }
       return roomRepository.save(room);
    }
}
