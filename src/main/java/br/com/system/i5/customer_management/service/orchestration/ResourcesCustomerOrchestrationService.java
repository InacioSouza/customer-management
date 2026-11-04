package br.com.system.i5.customer_management.service.orchestration;

import br.com.system.i5.customer_management.model.Customer;
import br.com.system.i5.customer_management.model.Movement;
import br.com.system.i5.customer_management.model.PricingMovement;
import br.com.system.i5.customer_management.model.ResourcesCustomer;
import br.com.system.i5.customer_management.model.enuns.OperationTypeEnum;
import br.com.system.i5.customer_management.model.enuns.ResourceTypeEnum;
import br.com.system.i5.customer_management.records.form.ResourcesCustomerUpdateForm;
import br.com.system.i5.customer_management.service.domain.MovementService;
import br.com.system.i5.customer_management.service.domain.PricingMovementService;
import br.com.system.i5.customer_management.service.domain.ResourcesCustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResourcesCustomerOrchestrationService {

    private final ResourcesCustomerService resourcesCustomerService;
    private final PricingMovementService pricingMovementService;
    private final MovementService movementService;

    @Transactional
    public ResourcesCustomer updateEntityWithMovements(
            UUID id,
            ResourcesCustomerUpdateForm input
    ) {
        if (!resourcesCustomerService.existsById(id)) {
            throw new IllegalArgumentException(
                    "No record of 'ResourcesCustomer' was found for the ID: " + id);
        }

        var resourcesCustomer = resourcesCustomerService.findById(id);
        resourcesCustomer.setTotalQuantity(input.totalQuantity());
        resourcesCustomer.setTotalBonus(input.totalBonus());
        resourcesCustomer.setTotalAmountPaid(input.totalAmountPaid());

        resourcesCustomer.setAverageValueTotalQuantity(
                resourcesCustomerService.calculatesAverageValue(
                        resourcesCustomer.getTotalAmountPaid(),
                        resourcesCustomer.getTotalQuantity()
                )
        );

        var quantityBonus = resourcesCustomer.getTotalBonus() != null ? resourcesCustomer.getTotalBonus() : 0;
        var nonBonusQuantity = resourcesCustomer.getTotalQuantity() - quantityBonus;

        resourcesCustomer.setAverageValueOnlyPaid(
                resourcesCustomerService.calculatesAverageValue(
                        resourcesCustomer.getTotalAmountPaid(),
                        nonBonusQuantity
                )
        );

        var resourcesCustomerUpdated = resourcesCustomerService.saveOrUpdateEntity(resourcesCustomer);

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
