package com.hotel.demo.model;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {

    private final BigDecimal bid;

    public static Customer of(BigDecimal amount) {
        return Customer.builder().bid(amount).build();
    }

    public boolean hasPremiumBid() {
        return bid.compareTo(BigDecimal.valueOf(100)) >= 0;
    }
}
