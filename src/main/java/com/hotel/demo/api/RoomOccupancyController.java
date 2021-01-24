package com.hotel.demo.api;

import com.hotel.demo.dto.RoomData;
import com.hotel.demo.dto.RoomOccupancyReport;
import com.hotel.demo.service.RoomOccupancyManager;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomOccupancyController {

    private final RoomOccupancyManager roomOccupancyManager;

    @RequestMapping(value = "/occupancy", method = RequestMethod.POST)
    public ResponseEntity<RoomOccupancyReport> setPassword(@RequestBody @Valid RoomData roomData) {
        RoomOccupancyReport report = roomOccupancyManager.occupy(roomData);
        return ResponseEntity.ok().body(report);
    }
}
