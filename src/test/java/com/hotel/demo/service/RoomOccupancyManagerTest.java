package com.hotel.demo.service;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.hotel.demo.dto.RoomData;
import com.hotel.demo.dto.RoomOccupancyReport;
import com.hotel.demo.model.Customer;
import com.hotel.demo.repository.CustomerRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    private CustomerRepository customerRepository;
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
        when(customerRepository.findAll()).thenReturn(
            Stream.of(23, 45, 155, 374, 22, 99, 100, 101, 115, 209)
                .map(BigDecimal::new)
                .map(Customer::of)
                .collect(Collectors.toList()));
    }

    @ParameterizedTest
    @MethodSource("testData")
    void occupy(int premiumRoomsCount, int economyRoomsCount, RoomOccupancyReport expectedReport) {
        RoomOccupancyReport roomOccupancyReport = target.occupy(RoomData.builder()
            .economyRoomsCount(economyRoomsCount)
            .premiumRoomsCount(premiumRoomsCount)
            .build());

        assertThat(roomOccupancyReport).usingRecursiveComparison().isEqualTo(expectedReport);
    }

    @Test
    void givenAllBidsAreLowAndAllRoomsArePremium_whenOccupy_thenUpgradeAll() {
        when(customerRepository.findAll()).thenReturn(
            Stream.of(50)
                .map(BigDecimal::new)
                .map(Customer::of)
                .collect(Collectors.toList()));
        RoomOccupancyReport roomOccupancyReport = target.occupy(RoomData.builder()
            .economyRoomsCount(0)
            .premiumRoomsCount(1)
            .build());

        assertThat(roomOccupancyReport).usingRecursiveComparison().isEqualTo(RoomOccupancyReport.of(1L, BigDecimal.valueOf(50), 0L, BigDecimal.valueOf(0)));
    }

    @Test
    void givenAllBidsAreHighAndAllRoomsAreEconomy_whenOccupy_thenOccupyNone() {
        when(customerRepository.findAll()).thenReturn(
            Stream.of(1000)
                .map(BigDecimal::new)
                .map(Customer::of)
                .collect(Collectors.toList()));
        RoomOccupancyReport roomOccupancyReport = target.occupy(RoomData.builder()
            .economyRoomsCount(1)
            .premiumRoomsCount(0)
            .build());

        assertThat(roomOccupancyReport).usingRecursiveComparison().isEqualTo(RoomOccupancyReport.of(0L, BigDecimal.valueOf(0), 0L, BigDecimal.valueOf(0)));
    }

    @Test
    void givenCustomersAndNoRooms_whenOccupy_thenOccupyNone() {
        when(customerRepository.findAll()).thenReturn(
            Stream.of(1000)
                .map(BigDecimal::new)
                .map(Customer::of)
                .collect(Collectors.toList()));
        RoomOccupancyReport roomOccupancyReport = target.occupy(RoomData.builder()
            .economyRoomsCount(0)
            .premiumRoomsCount(0)
            .build());

        assertThat(roomOccupancyReport).usingRecursiveComparison().isEqualTo(RoomOccupancyReport.of(0L, BigDecimal.valueOf(0), 0L, BigDecimal.valueOf(0)));
    }

    @Test
    void givenRoomsAndNoCustomers_whenOccupy_thenOccupyNone() {
        when(customerRepository.findAll()).thenReturn(new ArrayList<>());
        RoomOccupancyReport roomOccupancyReport = target.occupy(RoomData.builder()
            .economyRoomsCount(10)
            .premiumRoomsCount(10)
            .build());

        assertThat(roomOccupancyReport).usingRecursiveComparison().isEqualTo(RoomOccupancyReport.of(0L, BigDecimal.valueOf(0), 0L, BigDecimal.valueOf(0)));
    }
}