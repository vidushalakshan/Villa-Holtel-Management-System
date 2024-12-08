package org.hotel.villahotel.controller;

import lombok.RequiredArgsConstructor;
import org.hotel.villahotel.model.Room;
import org.hotel.villahotel.response.RoomResponse;
import org.hotel.villahotel.service.IRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class RoomController {
    private final IRoomService roomService;

    @PostMapping("addroom")
    public ResponseEntity<RoomResponse> addNewRoom (
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType")String roomType,
            @RequestParam("roomPrice")BigDecimal roomPrice) throws SQLException, IOException {

        Room savedRoom = roomService.addNewRoom(photo , roomType , roomPrice);
        RoomResponse response = new RoomResponse(savedRoom.getId(),savedRoom.getRoomType(),savedRoom.getRoomPrice());
        return ResponseEntity.ok(response);
    }

    @GetMapping("roomtype")
    public List<String> getRoomTypes () {
        return roomService.getAllRoomTypes();
    }

}
