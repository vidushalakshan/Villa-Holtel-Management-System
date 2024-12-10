package org.hotel.villahotel.controller;

import lombok.RequiredArgsConstructor;
import org.hotel.villahotel.exception.PhotoRetrievalExcetion;
import org.hotel.villahotel.model.BookedRoom;
import org.hotel.villahotel.model.Room;
import org.hotel.villahotel.response.BookingRoomResponse;
import org.hotel.villahotel.response.RoomResponse;
import org.hotel.villahotel.service.BookingService;
import org.hotel.villahotel.service.IRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class RoomController {
    private final IRoomService roomService;
    private final BookingService bookingService;

    @PostMapping("addroom")
    public ResponseEntity<RoomResponse> addNewRoom (
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType")String roomType,
            @RequestParam("roomPrice")BigDecimal roomPrice) throws SQLException, IOException {

        if (photo == null || photo.isEmpty()) {
            throw new IllegalArgumentException("Photo is required");
        }

        Room savedRoom = roomService.addNewRoom(photo , roomType , roomPrice);
        RoomResponse response = new RoomResponse(savedRoom.getId(),savedRoom.getRoomType(),savedRoom.getRoomPrice());
        return ResponseEntity.ok(response);
    }

    @GetMapping("roomtype")
    public List<String> getRoomTypes () {
        return roomService.getAllRoomTypes();
    }

    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = new ArrayList<>();

        for (Room room : rooms) {
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.getEncoder().encodeToString(photoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setImage(base64Photo);
                roomResponses.add(roomResponse);
            }
        }

        return ResponseEntity.ok(roomResponses);
    }

    @GetMapping("allrooms")
    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> booking = getAllBookingsByRoomId(room.getId());
        List<BookingRoomResponse> bookingInfo = booking
                .stream()
                .map(bookings -> new BookingRoomResponse(bookings.getBookingId(),
                        bookings.getCheckInDate(),
                        bookings.getCheckOutDate()
                )).toList();
        byte[] photoByte = null;
        Blob photoBlob = room.getImage();
        if (photoBlob != null) {
             try{
                 photoByte = photoBlob.getBytes(1, (int) photoBlob.length());
             }catch (SQLException e) {
                 throw new PhotoRetrievalExcetion("Error retrieving photo");
             }
        }
        return new RoomResponse(room.getId(),
                room.getRoomType(),
                room.getRoomPrice(),
                room.isBooked(), photoByte, bookingInfo
        );
    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingService.getAllBookingsByRoomId(roomId);
    }

}
