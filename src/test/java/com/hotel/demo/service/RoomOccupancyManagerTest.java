package com.hotel.demo.service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.hotel.demo.dto.RoomData;
import com.hotel.demo.dto.RoomOccupancyReport;
import com.hotel.demo.model.CustomerBid;
import com.hotel.demo.repository.CustomerBidRepository;
import java.math.BigDecimal;
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
    @InjectMocks
    private RoomOccupancyManager target;

    private static Stream<Arguments> testData() {
        return Stream.of(
            Arguments.of(3, 3, RoomOccupancyReport.of(3L, BigDecimal.valueOf(738), 3L, BigDecimal.valueOf(167))),
            Arguments.of(7, 5, RoomOccupancyReport.of(6L, BigDecimal.valueOf(1054), 4L, BigDecimal.valueOf(189))),
            Arguments.of(2, 7, RoomOccupancyReport.of(2L, BigDecimal.valueOf(583), 4L, BigDecimal.valueOf(189))),
            Arguments.of(7, 1, RoomOccupancyReport.of(7L, BigDecimal.valueOf(1153), 1L, BigDecimal.valueOf(45)))
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
        RoomOccupancyReport roomOccupancyReport = target.generateReport(RoomData.builder()
            .economyRoomsCount(economyRoomsCount)
            .premiumRoomsCount(premiumRoomsCount)
            .build());

        assertThat(roomOccupancyReport).usingRecursiveComparison().isEqualTo(expectedReport);
    }
}