package br.com.system.i5.customer_management.service;

import br.com.system.i5.customer_management.model.Administrator;
import br.com.system.i5.customer_management.model.AdministratorCustomer;
import br.com.system.i5.customer_management.records.form.AdministratorForm;
import br.com.system.i5.customer_management.repository.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdministratorService {

    private final AdministratorRepository administratorRepository;
    private final AdministratorCustomerService administratorCustomerService;
    private final CustomerService customerService;

    public Administrator create(AdministratorForm input) {
        if (!customerService.existsById(input.customerId())) {
            throw new IllegalArgumentException("There is no customer record for the ID:" + input.customerId());

        }
        var administrator = new Administrator(input);
        var administratorCustomer = new AdministratorCustomer(
                customerService.findById(input.customerId()),
                administrator
        );
        administratorCustomerService.save(administratorCustomer);
        return administratorRepository.save(administrator);
    }

    public Administrator update(UUID id, AdministratorForm input) {

        if (!administratorRepository.existsById(id)) {
            throw new IllegalArgumentException("There is no administrator record for the ID:" + id);
        }

        var administrator = administratorRepository.findById(id).get();
        administrator.setAdministratorName(input.administratorName());
        administrator.setAdministratorPhone(input.administratorPhone());

        if (!customerService.existsById(input.customerId())) {
            throw new IllegalArgumentException("There is no customer record for the ID:" + input.customerId());

        }
        AdministratorCustomer administratorCustomer = administratorCustomerService.findByAdministratorId(id);
        administratorCustomer.setAdministrator(administrator);
        administratorCustomer.setCustomer(customerService.findById(input.customerId()));
        administratorCustomerService.save(administratorCustomer);
        return administratorRepository.save(administrator);
    }

    public List<UUID> findIdsNotIn(List<UUID> listIds) {
        return administratorRepository.findIdsNotIn(listIds);
    }

    public Administrator findById(UUID id) {
        var optional = administratorRepository.findById(id);
        return optional.orElse(null);
    }

    public boolean existsById(UUID id) {
        return administratorRepository.existsById(id);
    }
}
