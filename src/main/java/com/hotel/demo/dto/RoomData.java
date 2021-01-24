package com.hotel.demo.dto;

import javax.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomData {

    @Min(0)
    private int premiumRoomsCount;
    @Min(0)
    private int economyRoomsCount;
}
