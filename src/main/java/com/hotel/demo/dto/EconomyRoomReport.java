package com.hotel.demo.dto;

import java.math.BigDecimal;

public class EconomyRoomReport extends RoomReport {

    EconomyRoomReport(BigDecimal expectedRevenue, Integer usage) {
        super(expectedRevenue, usage);
    }
}
