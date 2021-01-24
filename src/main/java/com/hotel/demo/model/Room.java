package com.hotel.demo.model;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Room {

    private RoomType type;
}
