package com.hotel.demo.service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.hotel.demo.dto.RoomData;
import com.hotel.demo.dto.RoomOccupancyReport;
import com.hotel.demo.model.CustomerBid;
import com.hotel.demo.model.Room;
import com.hotel.demo.model.RoomType;
import com.hotel.demo.repository.CustomerBidRepository;
import com.hotel.demo.repository.RoomRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomOccupancyManagerTest {

    @Mock
    private CustomerBidRepository customerBidRepository;
    @Mock
    private RoomRepository roomRepository;
    @InjectMocks
    private RoomOccupancyManager target;

    private static Stream<Arguments> testData() {
        return Stream.of(
            Arguments.of(3, 3, RoomOccupancyReport.of(3, BigDecimal.valueOf(738), 3, BigDecimal.valueOf(167))),
            Arguments.of(7, 5, RoomOccupancyReport.of(6, BigDecimal.valueOf(1054), 4, BigDecimal.valueOf(189))),
            Arguments.of(2, 7, RoomOccupancyReport.of(2, BigDecimal.valueOf(583), 4, BigDecimal.valueOf(189))),
            Arguments.of(7, 1, RoomOccupancyReport.of(7, BigDecimal.valueOf(1153), 1, BigDecimal.valueOf(45)))
        );
    }

    @BeforeEach
    void setUp() {
        when(customerBidRepository.findAll()).thenReturn(
            Stream.of(23, 45, 155, 374, 22, 99, 100, 101, 115, 209)
                .map(BigDecimal::new)
                .map(CustomerBid::of)
                .collect(Collectors.toList()));
    }

    @ParameterizedTest
    @MethodSource("testData")
    void generateReport(int premiumRoomsCount, int economyRoomsCount, RoomOccupancyReport expectedReport) {
        List<Room> premiumRooms = Collections.nCopies(premiumRoomsCount, Room.builder().type(RoomType.PREMIUM).build());
        List<Room> economyRooms = Collections.nCopies(premiumRoomsCount, Room.builder().type(RoomType.ECONOMY).build());
        List<Room> allRooms = new ArrayList<>(premiumRooms);
        allRooms.addAll(economyRooms);
        when(roomRepository.findAll()).thenReturn(allRooms);
        RoomOccupancyReport roomOccupancyReport = target.generateReport(RoomData.builder()
            .economyRoomsCount(economyRoomsCount)
            .premiumRoomsCount(premiumRoomsCount)
            .build());

        assertThat(roomOccupancyReport).usingRecursiveComparison().isEqualTo(expectedReport);
    }
}