package br.com.system.i5.customer_management.service;

import br.com.system.i5.customer_management.model.Customer;
import br.com.system.i5.customer_management.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer getCustomerByID(UUID customerID) {
        var customerOptional = customerRepository.findById(customerID);
        return customerOptional.orElse(null);
    }
}
