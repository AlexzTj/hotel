package com.hotel.demo.repository;

import com.hotel.demo.model.Customer;
import java.util.List;

public interface CustomerRepository {

    List<Customer> findAll();
}
