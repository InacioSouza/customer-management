package br.com.system.i5.customer_management.records.form;

import br.com.system.i5.customer_management.model.ResourcesCustomer;
import br.com.system.i5.customer_management.model.enuns.ResourceTypeEnum;

import java.math.BigDecimal;
import java.util.UUID;

public record ResourcesCustomerForm(
        UUID customerID,
        ResourceTypeEnum resourceType,
        Integer totalQuantity,
        Integer totalBonus,
        BigDecimal totalAmountPaid
) {

    public ResourcesCustomer getEntity() {
        var resource = new ResourcesCustomer();
        resource.setResourceType(resourceType);
        resource.setTotalQuantity(totalQuantity);
        resource.setTotalBonus(totalBonus);
        resource.setTotalAmountPaid(totalAmountPaid);

        return resource;
    }
}
