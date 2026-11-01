package br.com.system.i5.customer_management.service;

import br.com.system.i5.customer_management.model.BasePricesCustomer;
import br.com.system.i5.customer_management.records.form.BasePricesCustomerForm;
import br.com.system.i5.customer_management.repository.BasePricesCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasePricesCustomerService {

    private final BasePricesCustomerRepository basePricesCustomerRepository;
    private final CustomerService customerService;

    public BasePricesCustomer create(BasePricesCustomerForm input) {
        if (basePricesCustomerRepository.existsBasePriceByCustomerId(input.customerId())) {
            throw new IllegalArgumentException(
                    "There can be only one BasePriceCustomer record for each customer");
        }
        var recordBasePrice = input.getEntity();
        var customer = customerService.getCustomerByID(input.customerId());
        if (customer == null) {
            throw new IllegalArgumentException(
                    "There is no customer record for the Id: " + input.customerId());
        }
        recordBasePrice.setCustomer(customer);
        return basePricesCustomerRepository.save(recordBasePrice);
    }

    public BasePricesCustomer update(UUID id, BasePricesCustomerForm input) {
        if (!basePricesCustomerRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "There is no BasePricesCustomer record for the ID:" + id);
        }
        var basePricesCustomer = input.getEntity();
        var customer = customerService.getCustomerByID(input.customerId());
        if (customer == null) {
            throw new IllegalArgumentException(
                    "There is no customer record for the Id: " + input.customerId());
        }
        basePricesCustomer.setCustomer(customer);
        return basePricesCustomerRepository.save(basePricesCustomer);
    }

    public void delete(UUID id) {
        basePricesCustomerRepository.deleteById(id);
    }
}
