package com.hotel.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomData {

    private int premiumRoomsCount;
    private int economyRoomsCount;
}
