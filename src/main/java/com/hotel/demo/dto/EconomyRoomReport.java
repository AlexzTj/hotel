package com.hotel.demo.dto;

import java.math.BigDecimal;

public class EconomyRoomReport extends RoomReport {

    EconomyRoomReport(BigDecimal expectedRevenue, Long usage) {
        super(expectedRevenue, usage);
    }
}
