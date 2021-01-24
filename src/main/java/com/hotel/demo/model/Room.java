package com.hotel.demo.model;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Room {

    private RoomType type;
    private boolean occupied;
    private Customer customerBid;

    public void occupy(Customer bid) {
        this.occupied = true;
        this.customerBid = bid;
    }
}
