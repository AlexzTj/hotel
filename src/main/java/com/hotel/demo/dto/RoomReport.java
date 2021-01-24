package com.hotel.demo.dto;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomReport {

    private BigDecimal expectedRevenue;
    private Integer usage;
}
