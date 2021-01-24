package com.hotel.demo.dto;

import java.math.BigDecimal;

public class PremiumRoomReport extends RoomReport {

    PremiumRoomReport(BigDecimal expectedRevenue, Integer usage) {
        super(expectedRevenue, usage);
    }
}
