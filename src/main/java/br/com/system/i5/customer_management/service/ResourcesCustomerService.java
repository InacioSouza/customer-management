package br.com.system.i5.customer_management.service;

import br.com.system.i5.customer_management.model.Customer;
import br.com.system.i5.customer_management.model.Movement;
import br.com.system.i5.customer_management.model.PricingMovement;
import br.com.system.i5.customer_management.model.ResourcesCustomer;
import br.com.system.i5.customer_management.model.enuns.OperationTypeEnum;
import br.com.system.i5.customer_management.model.enuns.ResourceTypeEnum;
import br.com.system.i5.customer_management.records.form.ResourcesCustomerUpdateForm;
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
    private final CustomerService customerService;
    private final PricingMovementService pricingMovementService;
    private final MovementService movementService;

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

    private BigDecimal calculatesAverageValue(
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

    public ResourcesCustomer saveOrUpdateEntity(
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
        resourcesCustomer.setCustomer(customerService.getCustomerByID(entity.getCustomer().getId()));

        return resourcesCustomerRepository.save(resourcesCustomer);
    }

    public ResourcesCustomer updateEntityWithMovements(
            UUID id,
            ResourcesCustomerUpdateForm input
    ) {

        if (!resourcesCustomerRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "No record of 'ResourcesCustomer' was found for the ID: " + id);
        }

        var resourcesCustomer = resourcesCustomerRepository.findById(id).get();
        resourcesCustomer.setTotalQuantity(input.totalQuantity());
        resourcesCustomer.setTotalBonus(input.totalBonus());
        resourcesCustomer.setTotalAmountPaid(input.totalAmountPaid());

        resourcesCustomer.setAverageValueTotalQuantity(
                calculatesAverageValue(
                        resourcesCustomer.getTotalAmountPaid(),
                        resourcesCustomer.getTotalQuantity()
                )
        );

        var quantityBonus = resourcesCustomer.getTotalBonus() != null ? resourcesCustomer.getTotalBonus() : 0;
        var nonBonusQuantity = resourcesCustomer.getTotalQuantity() - quantityBonus;

        resourcesCustomer.setAverageValueOnlyPaid(
                calculatesAverageValue(
                        resourcesCustomer.getTotalAmountPaid(),
                        nonBonusQuantity
                )
        );

        var resourcesCustomerUpdated = saveOrUpdateEntity(resourcesCustomer);

        int showsVariationValue = resourcesCustomerUpdated.getTotalAmountPaid()
                .subtract(resourcesCustomer.getTotalAmountPaid())
                .compareTo(BigDecimal.ZERO);

        BigDecimal value = resourcesCustomerUpdated.getTotalAmountPaid()
                .subtract(resourcesCustomer.getTotalAmountPaid());

        if (showsVariationValue > 0) {
            var pricingMovement = createPricingMovement(
                    resourcesCustomerUpdated.getResourceType(),
                    value,
                    OperationTypeEnum.ADD,
                    resourcesCustomerUpdated.getCustomer(),
                    input.notes(),
                    input.photo()
            );

            pricingMovementService.create(pricingMovement);

        } else if (showsVariationValue < 0) {
            var pricingMovement = createPricingMovement(
                    resourcesCustomerUpdated.getResourceType(),
                    value.multiply(BigDecimal.valueOf(-1)),
                    OperationTypeEnum.REMOVE,
                    resourcesCustomerUpdated.getCustomer(),
                    input.notes(),
                    input.photo()
            );
            pricingMovementService.create(pricingMovement);
        }

        int showVariationQuantity = (resourcesCustomerUpdated.getTotalQuantity() -
                resourcesCustomer.getTotalQuantity());

        if (showVariationQuantity > 0) {
            var movement = createMovement(
                    resourcesCustomerUpdated.getResourceType(),
                    showVariationQuantity,
                    OperationTypeEnum.ADD,
                    resourcesCustomerUpdated.getCustomer(),
                    input.notes(),
                    input.photo()
            );

            movementService.create(movement);

        } else if (showVariationQuantity < 0) {
            var movement = createMovement(
                    resourcesCustomerUpdated.getResourceType(),
                    (showVariationQuantity * -1),
                    OperationTypeEnum.REMOVE,
                    resourcesCustomerUpdated.getCustomer(),
                    input.notes(),
                    input.photo()
            );

            movementService.create(movement);
        }

        return resourcesCustomerUpdated;
    }

    private PricingMovement createPricingMovement(
            ResourceTypeEnum resourceType,
            BigDecimal value,
            OperationTypeEnum operationType,
            Customer customer,
            String notes,
            byte[] file
    ) {
        var pricingMovement = new PricingMovement();
        pricingMovement.setType(resourceType);
        pricingMovement.setCustomer(customer);
        pricingMovement.setOperationType(operationType);

        if (operationType.equals(OperationTypeEnum.ADD)) {
            pricingMovement.setValueAdded(value);
        } else if (operationType.equals(OperationTypeEnum.REMOVE)) {
            pricingMovement.setValueRemoved(value);
        }

        pricingMovement.setNotes(notes);
        pricingMovement.setPhoto(file);
        return pricingMovement;
    }

    private Movement createMovement(
            ResourceTypeEnum resourceType,
            Integer quantity,
            OperationTypeEnum operationType,
            Customer customer,
            String notes,
            byte[] file
    ) {
        var movement = new Movement();
        movement.setType(resourceType);
        movement.setCustomer(customer);
        movement.setOperationType(operationType);

        if (operationType.equals(OperationTypeEnum.ADD)) {
            movement.setQuantityAdded(quantity);
        } else if (operationType.equals(OperationTypeEnum.REMOVE)) {
            movement.setQuantityRemoved(quantity);
        }

        movement.setNotes(notes);
        movement.setPhoto(file);
        return movement;
    }

}
