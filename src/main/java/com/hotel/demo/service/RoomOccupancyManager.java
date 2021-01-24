package com.hotel.demo.service;

import com.hotel.demo.dto.RoomData;
import com.hotel.demo.dto.RoomOccupancyReport;
import com.hotel.demo.model.CustomerBid;
import com.hotel.demo.model.Room;
import com.hotel.demo.model.RoomType;
import com.hotel.demo.repository.CustomerBidRepository;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomOccupancyManager {

    private final CustomerBidRepository customerBidRepository;

    public RoomOccupancyReport generateReport(RoomData roomData) {
        List<Room> premiumRooms = Stream.generate(() -> Room.builder().type(RoomType.PREMIUM).build())
            .limit(roomData.getPremiumRoomsCount())
            .collect(Collectors.toList());
        List<Room> economyRooms = Stream.generate(() -> Room.builder().type(RoomType.ECONOMY).build())
            .limit(roomData.getEconomyRoomsCount())
            .collect(Collectors.toList());
        List<CustomerBid> customerBids = customerBidRepository.findAll();
        customerBids.sort((o1, o2) -> o2.getAmount().compareTo(o1.getAmount()));
        Iterator<Room> premiumRoomIterator = premiumRooms.iterator();
        Iterator<Room> economyRoomIterator = economyRooms.iterator();
        Iterator<CustomerBid> customerBidIterator = customerBids.iterator();

        while (customerBidIterator.hasNext()) {
            if (!economyRoomIterator.hasNext()) {
                break;
            }
            CustomerBid bid = customerBidIterator.next();
            if (bid.isPremiumBid() && premiumRoomIterator.hasNext()) {
                Room next = premiumRoomIterator.next();
                next.setOccupied(true);
                next.setCustomerBid(bid);
                continue;
            }
            if (!bid.isPremiumBid() && economyRoomIterator.hasNext()) {
                Room next = economyRoomIterator.next();
                next.setOccupied(true);
                next.setCustomerBid(bid);
            }


        }
        Collections.reverse(economyRooms);
        economyRoomIterator = economyRooms.iterator();
        Optional<Room> premiumRoom;
        for (premiumRoom = premiumRooms.stream().filter(e -> !e.isOccupied()).findFirst(); premiumRoom.isPresent() && customerBidIterator.hasNext() && economyRoomIterator.hasNext(); ) {
            Room economyRoom = economyRoomIterator.next();
            CustomerBid nextCustomerBid = customerBidIterator.next();
            premiumRoom.get().setCustomerBid(economyRoom.getCustomerBid());
            premiumRoom.get().setOccupied(true);
            economyRoom.setCustomerBid(nextCustomerBid);
        }
        return RoomOccupancyReport.of(
            premiumRooms.stream().filter(Room::isOccupied).count(),
            premiumRooms.stream().filter(Room::isOccupied)
                .map(Room::getCustomerBid).map(CustomerBid::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
            economyRooms.stream().filter(Room::isOccupied).count(),
            economyRooms.stream().filter(Room::isOccupied)
                .map(Room::getCustomerBid).map(CustomerBid::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }
}
