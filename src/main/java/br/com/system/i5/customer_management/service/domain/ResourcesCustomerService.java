package br.com.system.i5.customer_management.service.domain;

import br.com.system.i5.customer_management.model.ResourcesCustomer;
import br.com.system.i5.customer_management.repository.CustomerRepository;
import br.com.system.i5.customer_management.repository.ResourcesCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResourcesCustomerService {

    private final ResourcesCustomerRepository resourcesCustomerRepository;
    private final CustomerRepository customerRepository;

    private void validate(ResourcesCustomer entity) {

        if (entity.getCustomer() == null || !customerRepository.existsById(entity.getCustomer().getId())) {
            throw new IllegalArgumentException("Provide a valid value for customer");
        }

        if (entity.getResourceType() == null) {
            throw new IllegalArgumentException(
                    "Provide a valid value for ResourceTypeEnum");
        }

        if (entity.getTotalQuantity() == null) {
            throw new IllegalArgumentException(
                    "Enter the total amount of resources");
        }

        if (entity.getTotalAmountPaid() == null) {
            throw new IllegalArgumentException(
                    "Enter the total Amount Paid of resources");
        }

        if (entity.getTotalBonus() == null) {
            throw new IllegalArgumentException(
                    "Enter the total amount of bonus resources");
        }

        if (entity.getTotalQuantity() < entity.getTotalBonus()) {
            throw new IllegalArgumentException(
                    "The total number of resources cannot be less than the number of bonus resources.");
        }
    }

    public BigDecimal calculatesAverageValue(
            BigDecimal totalAmountPaid,
            Integer totalQuantity
    ) {
        var averageValueTotalQuantity = BigDecimal.ZERO;
        if (!BigDecimal.ZERO.equals(totalAmountPaid)) {
            averageValueTotalQuantity = totalAmountPaid
                    .divide(
                            BigDecimal.valueOf(totalQuantity),
                            2,
                            RoundingMode.HALF_UP
                    );
        }
        return averageValueTotalQuantity;
    }

    public ResourcesCustomer saveOrUpdateEntity(ResourcesCustomer entity) {

        this.validate(entity);

        ResourcesCustomer resourcesCustomer = resourcesCustomerRepository
                .findResourceByTypeAndCustomer(
                        entity.getResourceType(),
                        entity.getCustomer().getId()
                );

        if (resourcesCustomer == null) {
            resourcesCustomer = entity;
        }

        var averageValueTotalQuantity = calculatesAverageValue(
                entity.getTotalAmountPaid(),
                entity.getTotalQuantity()
        );

        var quantityBonus = entity.getTotalBonus() != null ? entity.getTotalBonus() : 0;
        var nonBonusQuantity = entity.getTotalQuantity() - quantityBonus;

        var averageValueOnlyPaid = calculatesAverageValue(
                entity.getTotalAmountPaid(),
                nonBonusQuantity
        );

        resourcesCustomer.setResourceType(entity.getResourceType());
        resourcesCustomer.setAverageValueTotalQuantity(averageValueTotalQuantity);
        resourcesCustomer.setAverageValueOnlyPaid(averageValueOnlyPaid);
        resourcesCustomer.setTotalAmountPaid(entity.getTotalAmountPaid());
        resourcesCustomer.setTotalQuantity(entity.getTotalQuantity());
        resourcesCustomer.setTotalBonus(entity.getTotalBonus());
        resourcesCustomer.setCustomer(customerRepository.findById(entity.getCustomer().getId()).orElse(null));

        return resourcesCustomerRepository.save(resourcesCustomer);
    }

    public ResourcesCustomer findById(UUID id) {
        return resourcesCustomerRepository.findById(id).orElse(null);
    }

    public boolean existsById(UUID id) {
        return resourcesCustomerRepository.existsById(id);
    }
}
