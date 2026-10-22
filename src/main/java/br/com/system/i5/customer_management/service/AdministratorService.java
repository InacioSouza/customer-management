package br.com.system.i5.customer_management.service;

import br.com.system.i5.customer_management.model.Administrator;
import br.com.system.i5.customer_management.model.AdministratorCustomer;
import br.com.system.i5.customer_management.records.form.AdministratorForm;
import br.com.system.i5.customer_management.repository.AdministratorCustomerRepository;
import br.com.system.i5.customer_management.repository.AdministratorRepository;
import br.com.system.i5.customer_management.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdministratorService {

    private final AdministratorRepository administratorRepository;
    private final AdministratorCustomerRepository administratorCustomerRepository;
    private final CustomerRepository customerRepository;

    public Administrator create(AdministratorForm input) {
        if (!customerRepository.existsById(input.customerId())) {
            throw new IllegalArgumentException("There is no customer record for the ID:" + input.customerId());

        }
        var administrator = new Administrator(input);
        var administratorCustomer = new AdministratorCustomer(
                customerRepository.findById(input.customerId()).get(),
                administrator
        );
        administratorCustomerRepository.save(administratorCustomer);
        return administratorRepository.save(administrator);
    }

    public Administrator update(UUID id, AdministratorForm input) {

        if (!administratorRepository.existsById(id)) {
            throw new IllegalArgumentException("There is no administrator record for the ID:" + id);
        }

        var administrator = administratorRepository.findById(id).get();
        administrator.setAdministratorName(input.administratorName());
        administrator.setAdministratorPhone(input.administratorPhone());

        if (!customerRepository.existsById(input.customerId())) {
            throw new IllegalArgumentException("There is no customer record for the ID:" + input.customerId());

        }
        AdministratorCustomer administratorCustomer = administratorCustomerRepository.findByAdministratorId(id);
        administratorCustomer.setAdministrator(administrator);
        administratorCustomer.setCustomer(customerRepository.findById(input.customerId()).get());
        administratorCustomerRepository.save(administratorCustomer);
        return administratorRepository.save(administrator);
    }
}
