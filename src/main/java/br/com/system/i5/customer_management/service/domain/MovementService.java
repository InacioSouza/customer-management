package br.com.system.i5.customer_management.service.domain;

import br.com.system.i5.customer_management.model.Movement;
import br.com.system.i5.customer_management.model.enuns.OperationTypeEnum;
import br.com.system.i5.customer_management.repository.CustomerRepository;
import br.com.system.i5.customer_management.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovementService {

    private final MovementRepository movementRepository;
    private final CustomerRepository customerRepository;

    private void validate(Movement movement) {

        if (movement.getCustomer() == null ||
                !customerRepository.existsById(movement.getCustomer().getId())) {
            throw new IllegalArgumentException("Provide a valid value for customer");
        }

        if (movement.getType() == null) {
            throw new IllegalArgumentException(
                    "Provide a valid value for Type");
        }

        if (movement.getOperationType() == null) {
            throw new IllegalArgumentException(
                    "Provide a valid value for OperatonType");
        }

        if (movement.getQuantityAdded() != null && movement.getQuantityRemoved() != null) {
            throw new IllegalArgumentException(
                    "The 'quantityAdded' and 'quantityRemoved' attributes " +
                            " must not both be populated within the same object");
        }

        if (movement.getQuantityAdded() != null && movement.getQuantityAdded() < 0) {
            throw new IllegalArgumentException(
                    "The attribute 'quantityAdded' cannot have a negative value");
        }

        if (movement.getQuantityRemoved() != null && movement.getQuantityRemoved() < 0) {
            throw new IllegalArgumentException(
                    "The attribute 'quantityRemoved' cannot have a negative value");
        }

        if (movement.getQuantityAdded() != null && movement.getOperationType().equals(OperationTypeEnum.REMOVE)) {
            throw new IllegalArgumentException(
                    "The 'quantityAdded' attribute cannot be populated when the value of OperationTypeEnum is 'REMOVE'.");
        }

        if (movement.getQuantityRemoved() != null && movement.getOperationType().equals(OperationTypeEnum.ADD)) {
            throw new IllegalArgumentException(
                    "The 'quantityRemoved' attribute cannot be populated when the value of OperationTypeEnum is 'ADD'.");
        }

        if (movement.getQuantityRemoved() == null && movement.getQuantityAdded() == null) {
            throw new IllegalArgumentException(
                    "Populate the 'quantityRemoved' or 'quantityAdded' attribute according to the corresponding value from OperationTypeEnum.");
        }
    }

    public Movement create(Movement movement) {
        validate(movement);
        return movementRepository.save(movement);
    }
}
