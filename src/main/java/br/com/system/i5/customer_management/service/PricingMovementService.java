package br.com.system.i5.customer_management.service;

import br.com.system.i5.customer_management.model.PricingMovement;
import br.com.system.i5.customer_management.model.enuns.OperationTypeEnum;
import br.com.system.i5.customer_management.repository.PricingMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PricingMovementService {

    private final PricingMovementRepository pricingMovementRepository;
    private final CustomerService customerService;

    private void validate(PricingMovement pricingMovement) {

        if (pricingMovement.getCustomer() == null ||
                !customerService.existsById(pricingMovement.getCustomer().getId())) {
            throw new IllegalArgumentException("Provide a valid value for customer");
        }

        if (pricingMovement.getType() == null) {
            throw new IllegalArgumentException(
                    "Provide a valid value for Type");
        }

        if (pricingMovement.getOperationType() == null) {
            throw new IllegalArgumentException(
                    "Provide a valid value for OperatonType");
        }

        if (pricingMovement.getValueAdded() != null && pricingMovement.getValueRemoved() != null) {
            throw new IllegalArgumentException(
                    "The 'valueAdded' and 'valueRemoved' attributes" +
                            " must not both be populated within the same object");
        }

        if (pricingMovement.getValueAdded() != null &&
                pricingMovement.getValueAdded().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "The attribute 'valueAdded' cannot have a negative value");
        }

        if (pricingMovement.getValueRemoved() != null &&
                pricingMovement.getValueRemoved().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "The attribute 'valueRemoved' cannot have a negative value");
        }

        if (pricingMovement.getValueAdded() != null &&
                pricingMovement.getOperationType().equals(OperationTypeEnum.REMOVE)) {
            throw new IllegalArgumentException(
                    "The 'valueAdded' attribute cannot be populated when the value of OperationTypeEnum is 'REMOVE'.");
        }

        if (pricingMovement.getValueRemoved() != null && pricingMovement.getOperationType().equals(OperationTypeEnum.ADD)) {
            throw new IllegalArgumentException(
                    "The 'valueRemoved' attribute cannot be populated when the value of OperationTypeEnum is 'ADD'.");
        }

        if (pricingMovement.getValueRemoved() == null && pricingMovement.getValueAdded() == null) {
            throw new IllegalArgumentException(
                    "Populate the 'valueRemoved' or 'valueAdded' attribute according to the corresponding value from OperationTypeEnum.");
        }
    }

    public PricingMovement create(PricingMovement pricingMovement) {
        validate(pricingMovement);
        return pricingMovementRepository.save(pricingMovement);
    }
}
