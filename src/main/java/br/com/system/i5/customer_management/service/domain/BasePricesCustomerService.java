package br.com.system.i5.customer_management.service.domain;

import br.com.system.i5.customer_management.model.BasePricesCustomer;
import br.com.system.i5.customer_management.records.form.BasePricesCustomerForm;
import br.com.system.i5.customer_management.repository.BasePricesCustomerRepository;
import br.com.system.i5.customer_management.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasePricesCustomerService {

    private final BasePricesCustomerRepository basePricesCustomerRepository;
    private final CustomerRepository customerRepository;

    public BasePricesCustomer create(BasePricesCustomerForm input) {
        if (basePricesCustomerRepository.existsBasePriceByCustomerId(input.customerId())) {
            throw new IllegalArgumentException(
                    "There can be only one BasePriceCustomer record for each customer");
        }
        var recordBasePrice = input.getEntity();

        var customer = customerRepository.findById(input.customerId()).orElse(null);
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

        var customer = customerRepository.findById(input.customerId()).orElse(null);
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

    public BasePricesCustomer saveOrUpdateEntity(BasePricesCustomer basePricesCustomer) {
        if (basePricesCustomer.getCustomer() == null ||
                basePricesCustomer.getCustomer().getId() == null ||
                !customerRepository.existsById(basePricesCustomer.getCustomer().getId())) {
            throw new IllegalArgumentException("Provide a valid customer record");
        }

        var basePricesCustomerFound = basePricesCustomerRepository
                .findRecordByCustomerId(basePricesCustomer.getCustomer().getId());

        if (basePricesCustomerFound == null) {
            basePricesCustomerFound = basePricesCustomer;
        }

        basePricesCustomerFound.setUserUnit(basePricesCustomer.getUserUnit());
        basePricesCustomerFound.setOfficialWhatsAppChannel(basePricesCustomer.getOfficialWhatsAppChannel());
        basePricesCustomerFound.setUnofficialWhatsAppChannel(basePricesCustomer.getUnofficialWhatsAppChannel());
        basePricesCustomerFound.setInstagramChannel(basePricesCustomer.getInstagramChannel());
        basePricesCustomerFound.setMessengerChannel(basePricesCustomer.getMessengerChannel());
        basePricesCustomerFound.setCustomer(basePricesCustomer.getCustomer());

        return basePricesCustomerRepository.save(basePricesCustomerFound);
    }
}