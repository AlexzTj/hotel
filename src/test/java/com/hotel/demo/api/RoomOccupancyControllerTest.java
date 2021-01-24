package com.hotel.demo.api;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.demo.dto.RoomData;
import com.hotel.demo.dto.RoomOccupancyReport;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class RoomOccupancyControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void occupy() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
            .post("/occupancy")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(RoomData.builder()
                .premiumRoomsCount(3)
                .economyRoomsCount(3)
                .build())))
            .andExpect(status().isOk())
            .andReturn();
        RoomOccupancyReport report = objectMapper.readValue(result.getResponse().getContentAsString(), RoomOccupancyReport.class);

        assertThat(report).usingRecursiveComparison().isEqualTo(RoomOccupancyReport.of(3L, BigDecimal.valueOf(738), 3L, BigDecimal.valueOf(167)));
    }
}