package br.com.system.i5.customer_management.service;

import br.com.system.i5.customer_management.model.*;
import br.com.system.i5.customer_management.model.enuns.OperationTypeEnum;
import br.com.system.i5.customer_management.model.enuns.ResourceTypeEnum;
import br.com.system.i5.customer_management.records.dto.CustomerDto;
import br.com.system.i5.customer_management.records.form.CustomerForm;
import br.com.system.i5.customer_management.records.form.ProofOfResource;
import br.com.system.i5.customer_management.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BasePricesService basePricesService;
    private final BasePricesCustomerService basePricesCustomerService;
    private final AdministratorService administratorService;
    private final AdministratorCustomerService administratorCustomerService;
    private final ResourcesCustomerService resourcesCustomerService;
    private final MovementService movementService;
    private final PricingMovementService pricingMovementService;

    public Customer getCustomerByID(UUID customerID) {
        var customerOptional = customerRepository.findById(customerID);
        return customerOptional.orElse(null);
    }

    @Transactional
    public CustomerDto create(
            CustomerForm input,
            List<ProofOfResource> listProofOfResource
    ) {

        var customerSaved = customerRepository.save(input.getEntity());

        List<UUID> listIdAdminNotExist = administratorService
                .findIdsNotIn(input.listAdministratorId());

        if (!listIdAdminNotExist.isEmpty()) {
            throw new IllegalArgumentException(
                    "There are no administrators for the IDs:" +
                            listIdAdminNotExist.stream().map(UUID::toString)
            );
        }

        for (UUID idAdmin : input.listAdministratorId()) {
            var adminCustomer = new AdministratorCustomer();
            adminCustomer.setCustomer(customerSaved);
            adminCustomer.setAdministrator(new Administrator(idAdmin));
            this.administratorCustomerService.save(adminCustomer);
        }

        createBasePricesCustomer(input, customerSaved);
        List<ResourcesCustomer> listResourcesCustomer = createResourcesCustomer(input);

        createMovementsAndPriceMovements(
                customerSaved,
                listResourcesCustomer,
                listProofOfResource
        );

        return new CustomerDto(customerSaved);
    }

    private void createMovementsAndPriceMovements(
            Customer customerSaved,
            List<ResourcesCustomer> listResourcesCustomer,
            List<ProofOfResource> listProofOfResource
    ) {
        for (ResourcesCustomer resourceCustomer : listResourcesCustomer) {

            ProofOfResource proofOfResource = listProofOfResource
                    .stream()
                    .filter(proof -> Objects.equals(
                            resourceCustomer.getResourceType(),
                            proof.type()
                    ))
                    .findFirst()
                    .orElse(null);

            movementService.create(
                    createMovement(
                            customerSaved,
                            resourceCustomer,
                            proofOfResource
                    )
            );

            pricingMovementService.create(
                    createPricingMovement(
                            customerSaved,
                            resourceCustomer,
                            proofOfResource
                    )
            );
        }
    }

    private Movement createMovement(
            Customer customerSaved,
            ResourcesCustomer resourceCustomer,
            ProofOfResource proofOfResource
    ) {

        var movement = new Movement();
        movement.setType(resourceCustomer.getResourceType());
        movement.setOperationType(OperationTypeEnum.ADD);
        movement.setQuantityAdded(resourceCustomer.getTotalQuantity());
        movement.setCustomer(customerSaved);
        movement.setNotes(proofOfResource != null ? proofOfResource.notes() : null);
        movement.setPhoto(proofOfResource != null ? proofOfResource.file() : null);
        return movementService.create(movement);
    }

    private PricingMovement createPricingMovement(
            Customer customerSaved,
            ResourcesCustomer resourceCustomer,
            ProofOfResource proofOfResource
    ) {
        var pricingMovement = new PricingMovement();
        pricingMovement.setType(resourceCustomer.getResourceType());
        pricingMovement.setOperationType(OperationTypeEnum.ADD);
        pricingMovement.setValueAdded(resourceCustomer.getTotalAmountPaid());
        pricingMovement.setCustomer(customerSaved);
        pricingMovement.setNotes(proofOfResource != null ? proofOfResource.notes() : null);
        pricingMovement.setPhoto(proofOfResource != null ? proofOfResource.file() : null);
        return pricingMovement;
    }

    private List<ResourcesCustomer> createResourcesCustomer(CustomerForm input) {
        var quantityResources = ResourceTypeEnum.values().length;
        if (input.listGeneralResources() == null ||
                input.listGeneralResources().size() != quantityResources) {
            throw new IllegalArgumentException(
                    "The number of entries in the `listGeneralResources` " +
                            "list must be equal to the number of distinct values for `ResourceType`."
            );
        }

        List<ResourcesCustomer> listResourcesCustomerSaved = new ArrayList<>();
        Set<ResourceTypeEnum> resourceTypes = new HashSet<>();
        input.listGeneralResources().forEach(resource -> {
            if (!resourceTypes.add(resource.resourceType())) {
                throw new IllegalArgumentException("There should be only 1 ResourceTypeEnum per resource");
            }
            listResourcesCustomerSaved.add(resourcesCustomerService.saveOrUpdateEntity(resource.getEntity()));
        });

        return listResourcesCustomerSaved;
    }

    private void createBasePricesCustomer(CustomerForm input, Customer customerSaved) {
        BasePrices basePricesDefault = null;
        if (input.useDefaultPrices()) {
            basePricesDefault = basePricesService.getUnique();
        }

        var basePricesCustomer = new BasePricesCustomer();
        basePricesCustomer.setCustomer(customerSaved);
        if (basePricesDefault != null) {
            basePricesCustomer.setUserUnit(basePricesDefault.getUserUnit());
            basePricesCustomer.setOfficialWhatsAppChannel(basePricesDefault.getOfficialWhatsAppChannel());
            basePricesCustomer.setUnofficialWhatsAppChannel(basePricesDefault.getUnofficialWhatsAppChannel());
            basePricesCustomer.setInstagramChannel(basePricesDefault.getInstagramChannel());
            basePricesCustomer.setMessengerChannel(basePricesDefault.getMessengerChannel());
        } else {
            basePricesCustomer.setUserUnit(input.userUnit());
            basePricesCustomer.setOfficialWhatsAppChannel(input.officialWhatsAppChannel());
            basePricesCustomer.setUnofficialWhatsAppChannel(input.unofficialWhatsAppChannel());
            basePricesCustomer.setInstagramChannel(input.instagramChannel());
            basePricesCustomer.setMessengerChannel(input.messengerChannel());
        }
        basePricesCustomerService.saveOrUpdateEntity(basePricesCustomer);
    }

    public boolean existsById(UUID id) {
        return customerRepository.existsById(id);
    }

    public Customer findById(UUID id) {
        return customerRepository.findById(id).orElse(null);
    }

}
