package br.com.system.i5.customer_management.service;

import br.com.system.i5.customer_management.model.AdministratorCustomer;
import br.com.system.i5.customer_management.records.form.AdministratorCustomerForm;
import br.com.system.i5.customer_management.repository.AdministratorCustomerRepository;
import br.com.system.i5.customer_management.repository.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdministratorCustomerService {

    private final AdministratorCustomerRepository administratorCustomerRepository;
    private final AdministratorRepository administratorRepository;
    private final CustomerService customerService;

    public AdministratorCustomer create(AdministratorCustomerForm input) {

        if (!administratorRepository.existsById(input.administratorID())) {
            throw new IllegalArgumentException(
                    "There is no administrator record for the ID: " + input.administratorID());
        }

        if (!customerService.existsById(input.customerId())) {
            throw new IllegalArgumentException(
                    "There is no customer record for the ID: " + input.customerId());
        }

        var administratorCustomer = new AdministratorCustomer(
                customerService.findById(input.customerId()),
                // Usando o repository para buscar o administrador
                administratorRepository.findById(input.administratorID()).orElse(null)
        );

        return administratorCustomerRepository.save(administratorCustomer);
    }

    public void delete(UUID id) {
        var administratorCustomerOptional = administratorCustomerRepository.findById(id);

        if (administratorCustomerOptional.isEmpty()) {
            throw new IllegalArgumentException(
                    "There is no Administrator for the ID: " + id);
        }

        if (administratorCustomerRepository
                .countAdministratorsForCustomerId(
                        administratorCustomerOptional.get().getCustomer().getId()
                ) == 1) {
            throw new IllegalArgumentException(
                    "You cannot remove this relationship! A customer must have at least one administrator.");
        }
        administratorCustomerRepository.deleteById(id);
    }

    public void save(AdministratorCustomer adminCustomer) {

        if (adminCustomer.getCustomer() == null) {
            throw new IllegalArgumentException("The Customer object cannot be null");
        }

        if (!customerService.existsById(adminCustomer.getCustomer().getId())) {
            throw new IllegalArgumentException(
                    "There is no Customer for the ID: " +
                            adminCustomer.getCustomer().getId()
            );
        }

        if (adminCustomer.getAdministrator() == null) {
            throw new IllegalArgumentException("The Administrator object cannot be null");
        }

        if (!administratorRepository.existsById(adminCustomer.getAdministrator().getId())) {
            throw new IllegalArgumentException(
                    "There is no Administrator for the ID: " +
                            adminCustomer.getAdministrator().getId()
            );
        }

        administratorCustomerRepository.save(adminCustomer);
    }

    public AdministratorCustomer findByAdministratorId(UUID id) {
        return administratorCustomerRepository.findByAdministratorId(id);
    }
}