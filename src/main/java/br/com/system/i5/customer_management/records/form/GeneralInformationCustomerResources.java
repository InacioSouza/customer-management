package br.com.system.i5.customer_management.records.form;

import br.com.system.i5.customer_management.model.enuns.ResourceTypeEnum;

import java.math.BigDecimal;
import java.util.UUID;

public record GeneralInformationCustomerResources(
        UUID customerID,
        ResourceTypeEnum resourceType,
        Integer totalQuantity,
        BigDecimal totalAmountPaid,
        BigDecimal averageValueTotalQuantity,
        BigDecimal averageValueOnlyPaid
) {
}
