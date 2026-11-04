package br.com.system.i5.customer_management.service.domain;

import br.com.system.i5.customer_management.model.Customer;
import br.com.system.i5.customer_management.records.filter.CustomerFilter;
import br.com.system.i5.customer_management.repository.CustomerRepository;
import br.com.system.i5.customer_management.repository.specifications.CustomerSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer getCustomerByID(UUID customerID) {
        return customerRepository.findById(customerID).orElse(null);
    }

    public boolean existsById(UUID id) {
        return customerRepository.existsById(id);
    }

    public Customer findById(UUID id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<String> getListCustomerNiches() {
        return customerRepository.getListCustomerNiches();
    }

    public Page<Customer> searchCustomers(CustomerFilter filter, Pageable pageable) {
        Specification<Customer> spec = CustomerSpecification.byFilter(filter);
        return customerRepository.findAll(spec, pageable);
    }
}
