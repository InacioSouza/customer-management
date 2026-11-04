package br.com.system.i5.customer_management.service.orchestration;

import br.com.system.i5.customer_management.model.*;
import br.com.system.i5.customer_management.model.enuns.OperationTypeEnum;
import br.com.system.i5.customer_management.model.enuns.ResourceTypeEnum;
import br.com.system.i5.customer_management.records.dto.CustomerDto;
import br.com.system.i5.customer_management.records.form.CustomerForm;
import br.com.system.i5.customer_management.records.form.CutomerUpdateForm;
import br.com.system.i5.customer_management.records.form.ResourcesCustomerForm;
import br.com.system.i5.customer_management.service.domain.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerOrchestrationService {

    private final CustomerService customerService;
    private final BasePricesService basePricesService;
    private final BasePricesCustomerService basePricesCustomerService;
    private final AdministratorService administratorService;
    private final AdministratorCustomerService administratorCustomerService;
    private final ResourcesCustomerService resourcesCustomerService;
    private final MovementService movementService;
    private final PricingMovementService pricingMovementService;

    @Transactional
    public CustomerDto updateBasicInformation(
            UUID id,
            CutomerUpdateForm input
    ) {
        if (!customerService.existsById(id)) {
            throw new IllegalArgumentException(
                    "There is no Customer for the Id: " + id);
        }

        var customer = customerService.findById(id);
        BigDecimal differencePlatformPrice = input.platformCost()
                .subtract(customer.getPlatformCost());

        input.updateAttributes(customer);

        var comparisonResultPlatform = differencePlatformPrice
                .compareTo(BigDecimal.ZERO);

        if (comparisonResultPlatform > 0) {
            var pricingMovement = new PricingMovement();
            pricingMovement.setOperationType(OperationTypeEnum.ADD);
            pricingMovement.setType(ResourceTypeEnum.PLATFORM);
            pricingMovement.setValueAdded(differencePlatformPrice);
            pricingMovement.setCustomer(customer);
            pricingMovement.setNotes(input.notes());
            pricingMovement.setPhoto(input.photo());
            pricingMovementService.create(pricingMovement);

        } else if (comparisonResultPlatform < 0) {
            var pricingMovement = new PricingMovement();
            pricingMovement.setOperationType(OperationTypeEnum.REMOVE);
            pricingMovement.setType(ResourceTypeEnum.PLATFORM);
            pricingMovement.setValueAdded(
                    differencePlatformPrice.multiply(BigDecimal.valueOf(-1))
            );
            pricingMovement.setCustomer(customer);
            pricingMovement.setNotes(input.notes());
            pricingMovement.setPhoto(input.photo());
            pricingMovementService.create(pricingMovement);
        }

        BigDecimal differenceAiPrice = input.amountChargedAI()
                .subtract(customer.getAmountChargedAI());

        var comparisonResultAi = differenceAiPrice
                .compareTo(BigDecimal.ZERO);

        if (comparisonResultAi > 0) {
            pricingMovementService.create(
                    new PricingMovement(
                            customer,
                            ResourceTypeEnum.AI,
                            OperationTypeEnum.ADD,
                            differenceAiPrice,
                            input.notes(),
                            input.photo()

                    )
            );

        } else if (comparisonResultAi < 0) {

            pricingMovementService.create(
                    new PricingMovement(
                            customer,
                            ResourceTypeEnum.AI,
                            OperationTypeEnum.REMOVE,
                            differenceAiPrice.multiply(BigDecimal.valueOf(-1)),
                            input.notes(),
                            input.photo()

                    )
            );
        }

        return new CustomerDto(customerService.save(customer));
    }

    @Transactional
    public CustomerDto create(
            CustomerForm input
    ) {
        var customer = input.getEntity();
        if (customer.getEntryDate() != null &&
                customer.getDepartureDate() != null &&
                (customer.getEntryDate().after(customer.getDepartureDate()))) {
            throw new IllegalArgumentException(
                    "The 'entryDate' attribute must be earlier than 'departureDate'");
        }

        if (customer.getEntryDate() != null &&
                customer.getDepartureDate() != null &&
                (customer.getDepartureDate().before(customer.getEntryDate()))) {
            throw new IllegalArgumentException(
                    "The 'departureDate' attribute must be later than 'entryDate'");
        }
        var customerSaved = customerService.save(customer);

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
            administratorCustomerService.save(adminCustomer);
        }

        createBasePricesCustomer(input, customerSaved);
        List<ResourcesCustomer> listResourcesCustomer = createResourcesCustomer(input);

        createMovementsAndPriceMovements(
                customerSaved,
                listResourcesCustomer,
                input.listGeneralResources()
        );

        return new CustomerDto(customerSaved);
    }

    private void createMovementsAndPriceMovements(
            Customer customerSaved,
            List<ResourcesCustomer> listResourcesCustomerSaved,
            List<ResourcesCustomerForm> listResourcesCustomerForms
    ) {
        for (ResourcesCustomer resourceCustomer : listResourcesCustomerSaved) {

            var resourceCustomerForm = listResourcesCustomerForms
                    .stream()
                    .filter(rc -> {
                                return rc.resourceType() != null &&
                                        resourceCustomer.getResourceType()
                                                .equals(rc.resourceType());
                            }
                    ).findFirst()
                    .get();

            movementService.create(
                    new Movement(
                            customerSaved,
                            resourceCustomer.getResourceType(),
                            OperationTypeEnum.ADD,
                            resourceCustomer.getTotalQuantity(),
                            resourceCustomerForm.notes(),
                            resourceCustomerForm.file()
                    )
            );

            pricingMovementService.create(
                    new PricingMovement(
                            customerSaved,
                            resourceCustomer.getResourceType(),
                            OperationTypeEnum.ADD,
                            resourceCustomer.getTotalAmountPaid(),
                            resourceCustomerForm.notes(),
                            resourceCustomerForm.file()
                    )
            );
        }
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
}
