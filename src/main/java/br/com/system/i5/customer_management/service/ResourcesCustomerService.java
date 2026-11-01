package br.com.system.i5.customer_management.service;

import br.com.system.i5.customer_management.model.ResourcesCustomer;
import br.com.system.i5.customer_management.repository.ResourcesCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ResourcesCustomerService {

    private final ResourcesCustomerRepository resourcesCustomerRepository;
    private final CustomerService customerService;

    private void validate(ResourcesCustomer entity) {

        if (entity.getCustomer() == null || !customerService.existsById(entity.getCustomer().getId())) {
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

    public final ResourcesCustomer saveOrUpdateEntity(
            ResourcesCustomer entity
    ) {

        this.validate(entity);

        ResourcesCustomer resourcesCustomer = resourcesCustomerRepository
                .findResourceByTypeAndCustomer(
                        entity.getResourceType(),
                        entity.getCustomer().getId()
                );

        if (resourcesCustomer == null) {
            resourcesCustomer = entity;
        }

        var averageValueTotalQuantity = BigDecimal.ZERO;
        if (!BigDecimal.ZERO.equals(entity.getTotalAmountPaid())) {
            averageValueTotalQuantity = entity.getTotalAmountPaid()
                    .divide(
                            BigDecimal.valueOf(entity.getTotalQuantity()),
                            2,
                            RoundingMode.HALF_UP
                    );
        }

        var averageValueOnlyPaid = BigDecimal.ZERO;
        var nonBonusQuantity = BigDecimal.valueOf(entity.getTotalQuantity())
                .subtract(BigDecimal.valueOf(entity.getTotalBonus()));

        if (!BigDecimal.ZERO.equals(entity.getTotalAmountPaid())) {
            averageValueOnlyPaid = entity.getTotalAmountPaid()
                    .divide(
                            nonBonusQuantity,
                            2,
                            RoundingMode.HALF_UP
                    );
        }

        resourcesCustomer.setResourceType(entity.getResourceType());
        resourcesCustomer.setAverageValueTotalQuantity(averageValueTotalQuantity);
        resourcesCustomer.setAverageValueOnlyPaid(averageValueOnlyPaid);
        resourcesCustomer.setTotalAmountPaid(entity.getTotalAmountPaid());
        resourcesCustomer.setTotalQuantity(entity.getTotalQuantity());
        resourcesCustomer.setTotalBonus(entity.getTotalBonus());
        resourcesCustomer.setCustomer(customerService.getCustomerByID(entity.getCustomer().getId()));

        return resourcesCustomerRepository.save(resourcesCustomer);
    }
}
