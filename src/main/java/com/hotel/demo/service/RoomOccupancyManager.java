package com.hotel.demo.service;

import com.hotel.demo.dto.RoomData;
import com.hotel.demo.dto.RoomOccupancyReport;
import com.hotel.demo.model.Customer;
import com.hotel.demo.model.Room;
import com.hotel.demo.model.RoomType;
import com.hotel.demo.repository.CustomerRepository;
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

    private final CustomerRepository customerRepository;

    public RoomOccupancyReport occupy(RoomData roomData) {
        List<Room> premiumRooms = Stream.generate(() -> Room.builder().type(RoomType.PREMIUM).build())
            .limit(roomData.getPremiumRoomsCount())
            .collect(Collectors.toList());
        List<Room> economyRooms = Stream.generate(() -> Room.builder().type(RoomType.ECONOMY).build())
            .limit(roomData.getEconomyRoomsCount())
            .collect(Collectors.toList());
        List<Customer> customers = customerRepository.findAll();
        customers.sort((o1, o2) -> o2.getBid().compareTo(o1.getBid()));
        Iterator<Room> premiumRoomIterator = premiumRooms.iterator();
        Iterator<Room> economyRoomIterator = economyRooms.iterator();
        Iterator<Customer> customerIterator = customers.iterator();

        fillRooms(premiumRoomIterator, economyRoomIterator, customerIterator);
        upgradeRooms(premiumRooms, economyRooms, customers);

        return RoomOccupancyReport.of(
            premiumRooms.stream().filter(Room::isOccupied).count(),
            premiumRooms.stream().filter(Room::isOccupied)
                .map(Room::getCustomerBid).map(Customer::getBid).reduce(BigDecimal.ZERO, BigDecimal::add),
            economyRooms.stream().filter(Room::isOccupied).count(),
            economyRooms.stream().filter(Room::isOccupied)
                .map(Room::getCustomerBid).map(Customer::getBid).reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }

    private void fillRooms(Iterator<Room> premiumRoomIterator, Iterator<Room> economyRoomIterator, Iterator<Customer> customerIterator) {
        while (customerIterator.hasNext()) {
            if (!economyRoomIterator.hasNext() && !premiumRoomIterator.hasNext()) {
                break;
            }
            Customer customer = customerIterator.next();
            if (!customer.hasPremiumBid() && economyRoomIterator.hasNext()) {
                economyRoomIterator.next().occupy(customer);
                customerIterator.remove();
                continue;
            }
            if (customer.hasPremiumBid() && premiumRoomIterator.hasNext()) {
                premiumRoomIterator.next().occupy(customer);
                customerIterator.remove();
            }

        }
    }

    private void upgradeRooms(List<Room> premiumRooms, List<Room> economyRooms, List<Customer> customers) {
        Collections.reverse(economyRooms);
        Iterator<Room> economyRoomIterator = economyRooms.iterator();
        Iterator<Customer> customerIterator = customers.iterator();
        Optional<Room> premiumRoom = premiumRooms.stream().filter(e -> !e.isOccupied()).findFirst();
        for (; premiumRoom.isPresent() && customerIterator.hasNext(); premiumRoom = premiumRooms.stream().filter(e -> !e.isOccupied()).findFirst()) {
            if (economyRooms.isEmpty()) {
                //upgrade low bids to premium rooms
                Customer nextCustomer = customerIterator.next();
                premiumRoom.get().occupy(nextCustomer);
            } else if (economyRoomIterator.hasNext()) {
                //upgrade from economy to premium
                Room economyRoom = economyRoomIterator.next();
                Customer nextCustomerBid = customerIterator.next();
                premiumRoom.get().occupy(economyRoom.getCustomerBid());
                economyRoom.occupy(nextCustomerBid);
            }
        }
    }
}
