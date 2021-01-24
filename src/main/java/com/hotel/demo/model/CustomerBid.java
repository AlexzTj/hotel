package com.hotel.demo.model;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerBid {

    private BigDecimal amount;

    public static CustomerBid of(BigDecimal amount) {
        return CustomerBid.builder().amount(amount).build();
    }
}
