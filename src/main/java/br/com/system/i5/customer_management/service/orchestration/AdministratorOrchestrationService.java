package br.com.system.i5.customer_management.service.orchestration;

import br.com.system.i5.customer_management.model.Administrator;
import br.com.system.i5.customer_management.model.AdministratorCustomer;
import br.com.system.i5.customer_management.records.form.AdministratorForm;
import br.com.system.i5.customer_management.service.domain.AdministratorCustomerService;
import br.com.system.i5.customer_management.service.domain.AdministratorService;
import br.com.system.i5.customer_management.service.domain.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdministratorOrchestrationService {

    private final AdministratorService administratorService;
    private final AdministratorCustomerService administratorCustomerService;
    private final CustomerService customerService;

    public Administrator create(AdministratorForm input) {

        if (input.customerId() == null ||
                !customerService.existsById(input.customerId())) {
            throw new IllegalArgumentException(
                    "There is no customer record for the ID:" + input.customerId());
        }

        var administratorSaved = administratorService
                .save(new Administrator(input));

        var administratorCustomer = new AdministratorCustomer(
                customerService.findById(input.customerId()),
                administratorSaved
        );
        administratorCustomerService.save(administratorCustomer);

        return administratorSaved;
    }

    public Administrator update(UUID id, AdministratorForm input) {
        if (!administratorService.existsById(id)) {
            throw new IllegalArgumentException(
                    "There is no administrator record for the ID:" + id);
        }

        var administrator = administratorService.findById(id);
        administrator.setAdministratorName(input.administratorName());
        administrator.setAdministratorPhone(input.administratorPhone());

        if (!customerService.existsById(input.customerId())) {
            throw new IllegalArgumentException(
                    "There is no customer record for the ID:" + input.customerId());
        }

        AdministratorCustomer administratorCustomer =
                administratorCustomerService.findByAdministratorId(id);
        administratorCustomer.setAdministrator(administrator);
        administratorCustomer.setCustomer(customerService.findById(input.customerId()));
        administratorCustomerService.save(administratorCustomer);

        return administratorService.save(administrator);
    }
}
