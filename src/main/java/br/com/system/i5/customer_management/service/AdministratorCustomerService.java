package br.com.system.i5.customer_management.service;

import br.com.system.i5.customer_management.model.AdministratorCustomer;
import br.com.system.i5.customer_management.records.form.AdministratorCustomerForm;
import br.com.system.i5.customer_management.repository.AdministratorCustomerRepository;
import br.com.system.i5.customer_management.repository.AdministratorRepository;
import br.com.system.i5.customer_management.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdministratorCustomerService {

    private final AdministratorCustomerRepository administratorCustomerRepository;
    private final AdministratorRepository administratorRepository;
    private CustomerRepository customerRepository;

    public AdministratorCustomer create(AdministratorCustomerForm input) {

        if (!administratorCustomerRepository.existsById(input.administratorID())) {
            throw new IllegalArgumentException("There is no administrator record for the ID:" + input.administratorID());
        }

        if (!customerRepository.existsById(input.customerId())) {
            throw new IllegalArgumentException("There is no customer record for the ID:" + input.customerId());
        }

        var administratorCustomer = new AdministratorCustomer(
                customerRepository.findById(input.customerId()).get(),
                administratorRepository.findById(input.administratorID()).get()
        );

        return administratorCustomerRepository.save(administratorCustomer);
    }


    public void delete(UUID id) {
        administratorCustomerRepository.deleteById(id);
    }
}
