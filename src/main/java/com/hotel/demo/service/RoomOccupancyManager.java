package com.hotel.demo.service;

import com.hotel.demo.dto.RoomData;
import com.hotel.demo.dto.RoomOccupancyReport;
import com.hotel.demo.model.Customer;
import com.hotel.demo.model.Room;
import com.hotel.demo.model.RoomType;
import com.hotel.demo.repository.CustomerRepository;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomOccupancyManager {

    private final CustomerRepository customerRepository;

    public RoomOccupancyReport occupy(RoomData roomData) {
        log.info("Received room data to occupy:{}", roomData);
        List<Room> premiumRooms = Stream.generate(() -> Room.builder().type(RoomType.PREMIUM).build())
            .limit(roomData.getPremiumRoomsCount())
            .collect(Collectors.toList());
        List<Room> economyRooms = Stream.generate(() -> Room.builder().type(RoomType.ECONOMY).build())
            .limit(roomData.getEconomyRoomsCount())
            .collect(Collectors.toList());
        List<Customer> customers = customerRepository.findAll();
        customers.sort((o1, o2) -> o2.getBid().compareTo(o1.getBid()));

        fillRooms(premiumRooms, economyRooms, customers);

        RoomOccupancyReport occupancyReport = RoomOccupancyReport.of(
            premiumRooms.stream().filter(Room::isOccupied).count(),
            premiumRooms.stream().filter(Room::isOccupied)
                .map(Room::getCustomerBid).map(Customer::getBid).reduce(BigDecimal.ZERO, BigDecimal::add),
            economyRooms.stream().filter(Room::isOccupied).count(),
            economyRooms.stream().filter(Room::isOccupied)
                .map(Room::getCustomerBid).map(Customer::getBid).reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        log.info("Optimization result:{}", occupancyReport);
        return occupancyReport;
    }

    private void fillRooms(List<Room> premiumRooms, List<Room> economyRooms, List<Customer> customers) {
        Iterator<Room> premiumRoomIterator = premiumRooms.iterator();
        Iterator<Room> economyRoomIterator = economyRooms.iterator();
        Iterator<Customer> customerIterator = customers.iterator();
        while (customerIterator.hasNext()) {
            if (!economyRoomIterator.hasNext() && !premiumRoomIterator.hasNext()) {
                break;
            }
            Customer customer = customerIterator.next();

            if (premiumRoomIterator.hasNext()
                //upgrade
                && (customer.hasPremiumBid() || customers.stream().filter(e -> !e.hasPremiumBid()).count() >= economyRooms.size())) {
                premiumRoomIterator.next().occupy(customer);
                customerIterator.remove();
                continue;

            }

            if (!customer.hasPremiumBid() && economyRoomIterator.hasNext()) {
                economyRoomIterator.next().occupy(customer);
                customerIterator.remove();

            }
        }
    }

}
