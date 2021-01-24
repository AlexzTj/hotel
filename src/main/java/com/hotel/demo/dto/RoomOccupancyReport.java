package com.hotel.demo.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomOccupancyReport {

    private RoomReport premiumRoomData;
    private RoomReport economyRoomData;

    public static RoomOccupancyReport of(Integer premiumUsage, BigDecimal premiumRevenue, Integer economyUsage, BigDecimal economyRevenue) {
        return RoomOccupancyReport.builder()
            .economyRoomData(RoomReport.builder().expectedRevenue(economyRevenue).usage(economyUsage).build())
            .premiumRoomData(RoomReport.builder().expectedRevenue(premiumRevenue).usage(premiumUsage).build())
            .build();
    }
}
