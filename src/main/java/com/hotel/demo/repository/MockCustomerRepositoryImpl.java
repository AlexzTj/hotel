package com.hotel.demo.repository;
import com.hotel.demo.model.Customer;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Repository;

@Repository
class MockCustomerRepositoryImpl implements CustomerRepository {

    @Override
    public List<Customer> findAll() {
        return Stream.of(23, 45, 155, 374, 22, 99, 100, 101, 115, 209)
            .map(BigDecimal::new)
            .map(Customer::of)
            .collect(Collectors.toList());
    }
}
