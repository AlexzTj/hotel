package com.hotel.demo.repository;

import com.hotel.demo.model.CustomerBid;
import java.util.List;

public interface CustomerBidRepository {

    List<CustomerBid> findAll();
}
